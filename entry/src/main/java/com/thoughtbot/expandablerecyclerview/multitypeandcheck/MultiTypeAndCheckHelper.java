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

package com.thoughtbot.expandablerecyclerview.multitypeandcheck;

import ohos.agp.components.Button;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.ScrollView;
import ohos.agp.utils.Color;
import ohos.app.Context;
import com.thoughtbot.expandablerecyclerview.ExpandableListAdapter;
import com.thoughtbot.expandablerecyclerview.ExpandableListContainer;
import com.thoughtbot.expandablerecyclerview.ResourceTable;
import com.thoughtbot.expandablerecyclerview.entities.CommonHelper;
import com.thoughtbot.expandablerecyclerview.entities.ParentChild;
import com.thoughtbot.expandablerecyclerview.util.ResUtil;
import java.util.HashMap;

/**
 * Helper class for MultiTypeAndCheckAbilitySlice.
 */
public class MultiTypeAndCheckHelper extends CommonHelper {
    private HashMap<String, String> mSelectedChild = new HashMap<>();
    private Button clearbtn;

    public MultiTypeAndCheckHelper(Context context, ComponentContainer rootView) {
        this.context = context;
        this.rootView = rootView;
    }

    /**
     * This method is used to init view components.
     */
    public void initViews() {
        getGroupItems();
        getFavouriteItems();
        getGroupIcons();
        clearbtn = (Button) rootView.findComponentById(ResourceTable.Id_clearbtn);
        ScrollView parentLayout = (ScrollView) rootView.findComponentById(ResourceTable.Id_root_singlecheck);
        parentLayout.setBackground(getShapeElement(ResUtil.getColor(context, ResourceTable.Color_white)));
        mGroupContainer = (ExpandableListContainer)
                rootView.findComponentById(ResourceTable.Id_lcGroupItems_singlecheck);
        prepareExpandableListAdapter();
    }

    /**
     * This method is used to prepare list adapter for holding data.
     */
    private void prepareExpandableListAdapter() {
        ExpandableListAdapter<ParentChild> expandableListAdapter = new ExpandableListAdapter<ParentChild>(context,
                mGroupNameItem, mGroupImageItem, ResourceTable.Layout_ability_listview_item) {
            @Override
            protected void bind(ViewHolder holder, ParentChild text, Integer image, int position) {
                handleListItem(holder, text, image);
            }
        };
        //set the adapter and Clicklistener
        setterfunction(expandableListAdapter);

        //To clear the all the selected items.
        clearbtn.setClickedListener(component -> {
            mSelectedChild.clear();
            expandableListAdapter.setData(mGroupNameItem);
        });
    }

    /**
     * Render the child item in the list.
     *
     * @param holder adapter holding the data of the list item.
     * @param text group/child text item to be displayed.
     */
    public void handleChildItem(ExpandableListAdapter.ViewHolder holder, ParentChild text) {
        // Add child items to list
        holder.makeInvisibleImage(ResourceTable.Id_ArrowIcon);
        if (mFavouriteItem.contains(text.getChildItem())) {
            holder.setText(ResourceTable.Id_tvGroupTitle, text.getChildItem(), Color.BLACK,
                    ResUtil.getIntDimen(context, ResourceTable.Float_child_text_size));
            if (mSelectedChild.containsKey(text.getParentItem()) && mSelectedChild
                    .get(text.getParentItem()).equals(text.getChildItem())) {
                holder.setChecked(ResourceTable.Id_checkbtn);
            } else {
                holder.setUnChecked(ResourceTable.Id_checkbtn);
            }
        } else {
            holder.setText(ResourceTable.Id_tvGroupTitle, text.getChildItem(), Color.GRAY,
                    ResUtil.getIntDimen(context, ResourceTable.Float_child_text_size));
            holder.makeInvisibleButton(ResourceTable.Id_checkbtn);
        }
    }

    /**
     * to handle clicked item.
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
            if (mFavouriteItem.contains(clickedItem)) {
                String parentGroup = value.getParentItem();
                if (mSelectedChild.containsKey(parentGroup)) {
                    mSelectedChild.remove(parentGroup);
                }
                mSelectedChild.put(parentGroup, clickedItem);
                expandableListAdapter.setData(mGroupNameItem);
            } else {
                showToast();
            }
        }
    }
}
