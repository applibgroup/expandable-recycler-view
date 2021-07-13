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

package com.thoughtbot.expandablerecyclerview.expand;

import ohos.agp.components.Button;
import ohos.agp.components.ComponentContainer;
import ohos.agp.utils.Color;
import ohos.app.Context;
import com.thoughtbot.expandablerecyclerview.ExpandableListAdapter;
import com.thoughtbot.expandablerecyclerview.ResourceTable;
import com.thoughtbot.expandablerecyclerview.entities.ParentChild;
import com.thoughtbot.expandablerecyclerview.helper.CommonHelper;
import com.thoughtbot.expandablerecyclerview.util.ResUtil;

/**
 * Controller class for ExpandAbilitySlice.
 */
public class ExpandAbilityController extends CommonHelper {
    private Button toggleBtn;

    public ExpandAbilityController(Context context, ComponentContainer rootView) {
        this.context = context;
        this.rootView = rootView;
    }

    /**
     * This method is used to init view components.
     */
    public void initViews() {
        super.initializeViews(ResourceTable.Id_root_expand, ResourceTable.Id_lcGroupItems_expand);
        toggleBtn = (Button) rootView.findComponentById(ResourceTable.Id_toogle);
        this.prepareExpandableListAdapter();
    }

    @Override
    protected void prepareExpandableListAdapter() {
        super.prepareExpandableListAdapter();
        //To toggle the Classic Group Item
        setToggleListener(expandableListAdapter);
    }

    private void setToggleListener(ExpandableListAdapter<ParentChild> expandableListAdapter) {
        toggleBtn.setClickedListener(component -> {
            String valueClassic = ResUtil.getString(context, ResourceTable.String_item_Classic);
            int position = 0;
            for (int i = 0; i < mGroupNameItem.size(); i++) {
                if (mGroupNameItem.get(i).getParentItem() == null && mGroupNameItem.get(i)
                        .getChildItem().equals(valueClassic)) {
                    position = i;
                    break;
                }
            }
            super.handleClickedItem(expandableListAdapter, position);
        });
    }

    @Override
    protected void handleChildItem(ExpandableListAdapter.ViewHolder holder, ParentChild text) {
        // Add child items to list
        holder.makeInvisibleButton(ResourceTable.Id_checkbtn);
        holder.makeInvisibleImage(ResourceTable.Id_ArrowIcon);
        holder.setText(ResourceTable.Id_tvGroupTitle, text.getChildItem(), Color.GRAY,
                ResUtil.getIntDimen(context, ResourceTable.Float_child_text_size));
    }

    @Override
    protected void handleClickedChildItem(ExpandableListAdapter<ParentChild> expandableListAdapter, ParentChild value) {
        super.showToast();
    }
}
