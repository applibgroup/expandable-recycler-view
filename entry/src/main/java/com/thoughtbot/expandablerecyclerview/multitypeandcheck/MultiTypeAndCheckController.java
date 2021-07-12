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
import ohos.agp.utils.Color;
import ohos.app.Context;
import com.thoughtbot.expandablerecyclerview.ExpandableListAdapter;
import com.thoughtbot.expandablerecyclerview.ResourceTable;
import com.thoughtbot.expandablerecyclerview.entities.ParentChild;
import com.thoughtbot.expandablerecyclerview.helper.CommonHelper;
import com.thoughtbot.expandablerecyclerview.util.ResUtil;
import java.util.HashMap;

/**
 * Helper class for MultiTypeAndCheckAbilitySlice.
 */
public class MultiTypeAndCheckController extends CommonHelper {
    private HashMap<String, String> mSelectedChild = new HashMap<>();
    private Button clearbtn;

    public MultiTypeAndCheckController(Context context, ComponentContainer rootView) {
        this.context = context;
        this.rootView = rootView;
    }

    /**
     * This method is used to init view components.
     */
    public void initViews() {
        super.initializeViews(ResourceTable.Id_root_singlecheck, ResourceTable.Id_lcGroupItems_singlecheck);
        clearbtn = (Button) rootView.findComponentById(ResourceTable.Id_clearbtn);
        super.getFavouriteItems();
        this.prepareExpandableListAdapter();
    }

    @Override
    protected void prepareExpandableListAdapter() {
        super.prepareExpandableListAdapter();
        //To clear the all the selected items
        clearCheckedItem(expandableListAdapter);
    }

    @Override
    protected void handleChildItem(ExpandableListAdapter.ViewHolder holder, ParentChild text) {
        // Add child items to the list
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

    @Override
    protected void handleClickedChildItem(ExpandableListAdapter<ParentChild> expandableListAdapter, ParentChild value) {
        String clickedItem = value.getChildItem();
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

    private void clearCheckedItem(ExpandableListAdapter<ParentChild> expandableListAdapter) {
        clearbtn.setClickedListener(component -> {
            mSelectedChild.clear();
            expandableListAdapter.setData(mGroupNameItem);
        });
    }
}
