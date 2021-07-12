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
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.app.Context;
import com.thoughtbot.expandablerecyclerview.ExpandableListAdapter;
import com.thoughtbot.expandablerecyclerview.ResourceTable;
import com.thoughtbot.expandablerecyclerview.entities.ParentChild;
import com.thoughtbot.expandablerecyclerview.helper.CommonHelper;
import com.thoughtbot.expandablerecyclerview.util.ResUtil;

/**
 * Helper class for MultiTypeAbilitySlice.
 */
public class MultiTypeController extends CommonHelper {
    public MultiTypeController(Context context, ComponentContainer rootView) {
        this.context = context;
        this.rootView = rootView;
    }

    /**
     * This method is used to init view components.
     */
    public void initViews() {
        super.initializeViews(ResourceTable.Id_root_multitype, ResourceTable.Id_lcGroupItems_multitype);
        super.getFavouriteItems();
        super.prepareExpandableListAdapter();
    }

    @Override
    protected void handleChildItem(ExpandableListAdapter.ViewHolder holder, ParentChild text) {
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

    @Override
    protected void handleClickedChildItem(ExpandableListAdapter<ParentChild> expandableListAdapter, ParentChild value) {
        showToast();
    }
}
