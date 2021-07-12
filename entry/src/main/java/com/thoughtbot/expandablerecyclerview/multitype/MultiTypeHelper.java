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

package com.thoughtbot.expandablerecyclerview.multitype;

import ohos.agp.components.ComponentContainer;
import ohos.agp.components.Image;
import ohos.agp.components.ScrollView;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.app.Context;
import com.thoughtbot.expandablerecyclerview.ExpandableListAdapter;
import com.thoughtbot.expandablerecyclerview.ExpandableListContainer;
import com.thoughtbot.expandablerecyclerview.ResourceTable;
import com.thoughtbot.expandablerecyclerview.entities.CommonHelper;
import com.thoughtbot.expandablerecyclerview.entities.ParentChild;
import com.thoughtbot.expandablerecyclerview.util.ResUtil;

/**
 * Helper class for MultiTypeAbilitySlice.
 */
public class MultiTypeHelper extends CommonHelper {
    public MultiTypeHelper(Context context, ComponentContainer rootView) {
        this.context = context;
        this.rootView = rootView;
    }

    /**
     * This method is used to init view components.
     */
    public void initViews() {
        getFavouriteItems();
        getGroupItems();
        getGroupIcons();
        ScrollView parentLayout = (ScrollView) rootView.findComponentById(ResourceTable.Id_root_multitype);
        parentLayout.setBackground(getShapeElement(ResUtil.getColor(context, ResourceTable.Color_white)));
        mGroupContainer = (ExpandableListContainer)
                rootView.findComponentById(ResourceTable.Id_lcGroupItems_multitype);
        prepareExpandableListAdapter();
    }

    /**
     * This method is used to prepare adapter for holding data.
     */
    private void prepareExpandableListAdapter() {
        ExpandableListAdapter<ParentChild> expandableListAdapter = new ExpandableListAdapter<ParentChild>(context,
                mGroupNameItem, mGroupImageItem, ResourceTable.Layout_ability_listview_item) {
            @Override
            protected void bind(ViewHolder holder, ParentChild text, Integer image, int position) {
                handleListItem(holder, text, image);
            }
        };
        //setting the adapter and Clicklistener
        setterfunction(expandableListAdapter);
    }

    /**
     * For handling the child item in the list.
     *
     * @param holder adapter holding the date of the list.
     * @param text group/child text item to be displayed.
     */
    public void handleChildItem(ExpandableListAdapter.ViewHolder holder, ParentChild text) {
        // Add child items to list
        holder.makeInvisibleButton(ResourceTable.Id_checkbtn);
        holder.setText(ResourceTable.Id_tvGroupTitle, text.getChildItem(), Color.GRAY,
                ResUtil.getIntDimen(context, ResourceTable.Float_child_text_size));
        //to add image only for the favourite items
        if (mFavouriteItem.contains(text.getChildItem())) {
            holder.setGroupImage(ResourceTable.Id_childstar, ResourceTable.Media_star,
                    ShapeElement.OVAL, Image.ScaleMode.CENTER, ResourceTable.Color_white);
        }
    }

    /**
     * for handling the clicked item.
     *
     * @param expandableListAdapter adapter holding the list data.
     * @param position position of the clicked item.
     */
    public void handleClickedItem(ExpandableListAdapter<ParentChild> expandableListAdapter, int position) {
        ParentChild value = mGroupNameItem.get(position);
        String clickedItem = value.getChildItem();
        if (!mTempChildNameItem.contains(clickedItem)) {
            checkChild(position);
            expandableListAdapter.setData(mGroupNameItem);
        } else {
            showToast();
        }
    }
}
