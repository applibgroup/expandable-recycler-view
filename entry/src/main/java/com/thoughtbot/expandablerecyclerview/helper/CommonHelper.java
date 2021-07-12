/*
 * Copyright (C) 2020-21 Application Library Engineering Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.thoughtbot.expandablerecyclerview.helper;

import ohos.agp.colors.RgbColor;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.Image;
import ohos.agp.components.ScrollView;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.TextAlignment;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;
import com.thoughtbot.expandablerecyclerview.ExpandableListAdapter;
import com.thoughtbot.expandablerecyclerview.ExpandableListContainer;
import com.thoughtbot.expandablerecyclerview.ResourceTable;
import com.thoughtbot.expandablerecyclerview.entities.ParentChild;
import com.thoughtbot.expandablerecyclerview.util.ResUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Parent helper class for each Controller.
 */
public abstract class CommonHelper {
    protected ExpandableListContainer mGroupContainer;
    protected ArrayList<ParentChild> mGroupNameItem = new ArrayList<>();
    protected ArrayList<String> mTempGroupNameItem = new ArrayList<>();
    protected ArrayList<String> mTempChildNameItem = new ArrayList<>();
    protected ArrayList<String> mFinalGroupNameItem = new ArrayList<>();
    protected ArrayList<Integer> mGroupImageItem = new ArrayList<>();
    protected ArrayList<String> mFavouriteItem = new ArrayList<>();
    protected ExpandableListAdapter<ParentChild> expandableListAdapter;
    protected Context context;
    protected ComponentContainer rootView;

    /**
     * Initialize the view components.
     */
    protected void initializeViews(int rootViewId, int containerId) {
        ScrollView parentLayout = (ScrollView) rootView.findComponentById(rootViewId);
        parentLayout.setBackground(getShapeElement(ResUtil.getColor(context, ResourceTable.Color_white)));
        mGroupContainer = (ExpandableListContainer) rootView.findComponentById(containerId);
        getGroupItems();
        getGroupIcons();

    }

    /**
     * This method is used to prepare group items.
     */
    private void getGroupItems() {
        mGroupNameItem.add(new ParentChild(null, ResUtil.getString(context, ResourceTable.String_item_Rock)));
        mGroupNameItem.add(new ParentChild(null, ResUtil.getString(context, ResourceTable.String_item_Jazz)));
        mGroupNameItem.add(new ParentChild(null, ResUtil.getString(context, ResourceTable.String_item_Classic)));
        mGroupNameItem.add(new ParentChild(null, ResUtil.getString(context, ResourceTable.String_item_Salsa)));
        mGroupNameItem.add(new ParentChild(null, ResUtil.getString(context, ResourceTable.String_item_Bluegrass)));
        mFinalGroupNameItem.add(ResUtil.getString(context, ResourceTable.String_item_Rock));
        mFinalGroupNameItem.add(ResUtil.getString(context, ResourceTable.String_item_Jazz));
        mFinalGroupNameItem.add(ResUtil.getString(context, ResourceTable.String_item_Classic));
        mFinalGroupNameItem.add(ResUtil.getString(context, ResourceTable.String_item_Salsa));
        mFinalGroupNameItem.add(ResUtil.getString(context, ResourceTable.String_item_Bluegrass));
    }

    /**
     * This method is used to prepare group items images.
     */
    private void getGroupIcons() {
        mGroupImageItem.add(ResourceTable.Media_rock);
        mGroupImageItem.add(ResourceTable.Media_jazz);
        mGroupImageItem.add(ResourceTable.Media_classic);
        mGroupImageItem.add(ResourceTable.Media_salsa);
        mGroupImageItem.add(ResourceTable.Media_bluegrass);
    }

    /**
     * This method is used to prepare Favourite child items.
     */
    protected void getFavouriteItems() {
        mFavouriteItem.add(ResUtil.getString(context, ResourceTable.String_item_child_Rock1));
        mFavouriteItem.add(ResUtil.getString(context, ResourceTable.String_item_child_Rock4));
        mFavouriteItem.add(ResUtil.getString(context, ResourceTable.String_item_child_Jazz1));
        mFavouriteItem.add(ResUtil.getString(context, ResourceTable.String_item_child_Jazz2));
        mFavouriteItem.add(ResUtil.getString(context, ResourceTable.String_item_child_Classic2));
        mFavouriteItem.add(ResUtil.getString(context, ResourceTable.String_item_child_Salsa1));
        mFavouriteItem.add(ResUtil.getString(context, ResourceTable.String_item_child_Bluegrass1));
    }

    /**
     * This method is used to prepare list adapter for holding data.
     */
    protected void prepareExpandableListAdapter() {
        expandableListAdapter = new ExpandableListAdapter<ParentChild>(context,
                mGroupNameItem, mGroupImageItem, ResourceTable.Layout_ability_listview_item) {
            @Override
            protected void bind(ViewHolder holder, ParentChild text, Integer image, int position) {
                handleListItem(holder, text, image);
            }
        };
        //setting the adapter
        setAdapter(expandableListAdapter);
        //setting the OnItemClickListener
        setListener(expandableListAdapter);
    }

    private void setAdapter(ExpandableListAdapter<ParentChild> expandableListAdapter) {
        mGroupContainer.setItemProvider(expandableListAdapter);
    }

    private void setListener(ExpandableListAdapter<ParentChild> expandableListAdapter) {
        expandableListAdapter.setOnItemClickListener((component, position) ->
                handleClickedItem(expandableListAdapter, position));
    }

    /**
     * for handling both parent and child item inside the bind method.
     *
     * @param holder holding the view.
     * @param text group/child text item to be displayed.
     * @param image group image to be displayed.
     */
    private void handleListItem(ExpandableListAdapter.ViewHolder holder, ParentChild text, Integer image) {
        if (!mTempChildNameItem.contains(text.getChildItem())) {
            handleParentItem(holder, text, image);
        } else {
            handleChildItem(holder, text);
        }
    }

    /**
     * for rendering the parent/groupitem in the list.
     *
     * @param holder holding the view.
     * @param text group/child text item to be displayed.
     * @param image group image to be displayed.
     */
    private void handleParentItem(ExpandableListAdapter.ViewHolder holder, ParentChild text, Integer image) {
        // Set background for parent/Group
        holder.makeInvisibleButton(ResourceTable.Id_checkbtn);
        holder.makeInvisibleImage(ResourceTable.Id_childstar);
        holder.setGroupItemBackground(ResourceTable.Id_groupContainer, ResourceTable.Color_white);
        holder.setText(ResourceTable.Id_tvGroupTitle, text.getChildItem(), Color.GRAY,
                ResUtil.getIntDimen(context, ResourceTable.Float_group_text_size));
        holder.setGroupImage(ResourceTable.Id_ivGroupIcon, image,
                ShapeElement.RECTANGLE, Image.ScaleMode.STRETCH, ResourceTable.Color_white);
        if (!mTempGroupNameItem.contains(text.getChildItem())) {
            // Set arrow down icon
            holder.setGroupImage(ResourceTable.Id_ArrowIcon, ResourceTable.Media_arrow_Down,
                    ShapeElement.OVAL, Image.ScaleMode.CENTER, ResourceTable.Color_white);
        } else {
            // Set arrow up icon
            holder.setGroupImage(ResourceTable.Id_ArrowIcon, ResourceTable.Media_arrow_Up,
                    ShapeElement.OVAL, Image.ScaleMode.CENTER, ResourceTable.Color_white);
        }
    }

    /**
     * For rendering the child item in the list.
     *
     * @param holder adapter for holding the list data.
     * @param text group/child text item to be displayed.
     */
    protected abstract void handleChildItem(ExpandableListAdapter.ViewHolder holder, ParentChild text);

    /**
     * to handle the clicked item.
     *
     * @param expandableListAdapter adapter holding the list data.
     * @param position position of the clicked item.
     */
    protected void handleClickedItem(ExpandableListAdapter<ParentChild> expandableListAdapter, int position) {
        ParentChild value = mGroupNameItem.get(position);
        String clickedItem = value.getChildItem();
        if (!mTempChildNameItem.contains(clickedItem)) {
            handleClickedParentItem(expandableListAdapter, position);
        } else {
            handleClickedChildItem(expandableListAdapter, value);
        }
    }

    private void handleClickedParentItem(ExpandableListAdapter<ParentChild> expandableListAdapter, int position) {
        checkChild(position);
        expandableListAdapter.setData(mGroupNameItem);
    }

    /**
     * to handle the clicked child item.
     *
     * @param expandableListAdapter adapter holding the list data.
     * @param value the value of the clicked item
     */
    protected abstract void handleClickedChildItem(ExpandableListAdapter<ParentChild> expandableListAdapter,
                                                   ParentChild value);

    /**
     * To check whether the child is to be added or removed.
     *
     * @param position its position in the mGroupNameItem list
     */
    private void checkChild(int position) {
        ParentChild value = mGroupNameItem.get(position);
        String clickedItem = value.getChildItem();
        if (mTempGroupNameItem.contains(clickedItem)) {
            int actualItemPosition = mFinalGroupNameItem.indexOf(clickedItem);
            removeChildItems(actualItemPosition, position);
            mTempGroupNameItem.remove(clickedItem);
        } else {
            int actualItemPosition = mFinalGroupNameItem.indexOf(clickedItem);
            addChildItems(actualItemPosition, clickedItem, position);
            mTempGroupNameItem.add(clickedItem);
        }
    }

    /**
     * This method is used to add child items in list.
     *
     * @param index index of the group item.
     * @param clickedItem name of clicked/group item.
     * @param itemPositionFromGroup positon after which child item is to be added.
     */
    private void addChildItems(int index, String clickedItem, int itemPositionFromGroup) {
        String[] childItems = childItems().get(index);
        for (String item : childItems) {
            itemPositionFromGroup = itemPositionFromGroup + 1;
            mGroupNameItem.add(itemPositionFromGroup, new ParentChild(clickedItem, item));
            mTempChildNameItem.add(item);
            mGroupImageItem.add(itemPositionFromGroup, ResourceTable.Media_star);
        }
    }

    /**
     * This method is used to remove child item.
     *
     * @param position position of group item.
     * @param itemPositionFromGroup positon after which child item is to be removed.
     */
    private void removeChildItems(int position, int itemPositionFromGroup) {
        String[] items = childItems().get(position);
        for (String name : items) {
            mGroupNameItem.remove(itemPositionFromGroup + 1);
            mGroupImageItem.remove(itemPositionFromGroup + 1);
            mTempChildNameItem.remove(name);
        }
    }

    /**
     * This method is used to prepare map based on group item index.
     *
     * @return Map
     */
    private Map<Integer, String[]> childItems() {
        HashMap<Integer, String[]> map = new HashMap<>();
        map.put(0, new String[]{
                ResUtil.getString(context, ResourceTable.String_item_child_Rock1),
                ResUtil.getString(context, ResourceTable.String_item_child_Rock2),
                ResUtil.getString(context, ResourceTable.String_item_child_Rock3),
                ResUtil.getString(context, ResourceTable.String_item_child_Rock4)});
        map.put(1, new String[]{
                ResUtil.getString(context, ResourceTable.String_item_child_Jazz1),
                ResUtil.getString(context, ResourceTable.String_item_child_Jazz2),
                ResUtil.getString(context, ResourceTable.String_item_child_Jazz3)});
        map.put(2, new String[]{
                ResUtil.getString(context, ResourceTable.String_item_child_Classic1),
                ResUtil.getString(context, ResourceTable.String_item_child_Classic2),
                ResUtil.getString(context, ResourceTable.String_item_child_Classic3),
                ResUtil.getString(context, ResourceTable.String_item_child_Classic4)});
        map.put(3, new String[]{
                ResUtil.getString(context, ResourceTable.String_item_child_Salsa1),
                ResUtil.getString(context, ResourceTable.String_item_child_Salsa2),
                ResUtil.getString(context, ResourceTable.String_item_child_Salsa3),
                ResUtil.getString(context, ResourceTable.String_item_child_Salsa4)});
        map.put(4, new String[]{
                ResUtil.getString(context, ResourceTable.String_item_child_Bluegrass1),
                ResUtil.getString(context, ResourceTable.String_item_child_Bluegrass2),
                ResUtil.getString(context, ResourceTable.String_item_child_Bluegrass3),
                ResUtil.getString(context, ResourceTable.String_item_child_Bluegrass4)});
        return map;
    }

    /**
     * This method is used to show toast dialog based on the child Item.
     */
    protected void showToast() {
        ToastDialog toastDialog = new ToastDialog(context);
        toastDialog.setAlignment(TextAlignment.BOTTOM);
        toastDialog.setText(ResUtil.getString(context, ResourceTable.String_clicked_on_child_item));
        toastDialog.show();
    }

    /**
     * This method is used to prepare the background shape element based on color.
     *
     * @param color color for the shape element.
     * @return ShapeElement.
     */
    private ShapeElement getShapeElement(int color) {
        ShapeElement shapeElement = new ShapeElement();
        shapeElement.setShape(ShapeElement.RECTANGLE);
        shapeElement.setRgbColor(RgbColor.fromArgbInt(color));
        return shapeElement;
    }
}
