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

package com.thoughtbot.expandablerecyclerview.multicheck;

import ohos.agp.components.Button;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.ScrollView;
import ohos.agp.utils.Color;
import ohos.agp.utils.TextAlignment;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;
import com.thoughtbot.expandablerecyclerview.ExpandableListAdapter;
import com.thoughtbot.expandablerecyclerview.ExpandableListContainer;
import com.thoughtbot.expandablerecyclerview.ResourceTable;
import com.thoughtbot.expandablerecyclerview.entities.CommonHelper;
import com.thoughtbot.expandablerecyclerview.entities.ParentChild;
import com.thoughtbot.expandablerecyclerview.util.ResUtil;
import java.util.ArrayList;

/**
 * Helper class for MultiCheckAbilitySlice.
 */
public class MultiCheckHelper extends CommonHelper {
    private ArrayList<ParentChild> mSelectedChild = new ArrayList<>();
    private Button clearbtn;
    private Button bostonbtn;

    public MultiCheckHelper(Context context, ComponentContainer rootView) {
        this.context = context;
        this.rootView = rootView;
    }

    /**
     * This method is used to init view components.
     */
    public void initViews() {
        clearbtn = (Button) rootView.findComponentById(ResourceTable.Id_clearbtn2);
        bostonbtn = (Button) rootView.findComponentById(ResourceTable.Id_checkboston);
        getGroupItems();
        getGroupIcons();
        ScrollView parentLayout = (ScrollView) rootView.findComponentById(ResourceTable.Id_root_mutlicheck);
        parentLayout.setBackground(getShapeElement(ResUtil.getColor(context, ResourceTable.Color_white)));
        mGroupContainer = (ExpandableListContainer) rootView
                .findComponentById(ResourceTable.Id_lcGroupItems_multicheck);
        prepareExpandableListAdapter();
    }

    /**
     * This method is used to prepare adapter for holding the data.
     */
    private void prepareExpandableListAdapter() {
        ExpandableListAdapter<ParentChild> expandableListAdapter = new ExpandableListAdapter<ParentChild>(context,
                mGroupNameItem, mGroupImageItem, ResourceTable.Layout_ability_listview_item) {
            @Override
            protected void bind(ViewHolder holder, ParentChild text, Integer image, int position) {
                handleListItem(holder, text, image);
            }
        };
        //set the adapter and the Clicklistener
        setterfunction(expandableListAdapter);

        //To clear the all the selected items
        clearbtn.setClickedListener(component -> {
            mSelectedChild.clear();
            expandableListAdapter.setData(mGroupNameItem);
        });

        //To automatically check boston child item under Parent Rock
        bostonbtn.setClickedListener(component -> {
            boolean isPresent = false;
            String valueRock = ResUtil.getString(context, ResourceTable.String_item_Rock);
            String valueBoston = ResUtil.getString(context, ResourceTable.String_item_child_Rock4);
            ParentChild value = new ParentChild(valueRock, valueBoston);
            for (int i = 0; i < mSelectedChild.size(); i++) {
                if (mSelectedChild.get(i).isEquals(value)) {
                    isPresent = true;
                    break;
                }
            }
            if (!isPresent) {
                mSelectedChild.add(new ParentChild(valueRock, valueBoston));
                expandableListAdapter.setData(mGroupNameItem);
            } else {
                showToastForBoston();
            }
        });
    }

    /**
     * For rendering the child item in the list.
     *
     * @param holder adapter for holding the data of the list.
     * @param text group/child text item to be displayed.
     */
    public void handleChildItem(ExpandableListAdapter.ViewHolder holder, ParentChild text) {
        // Add child items to list
        holder.makeInvisibleImage(ResourceTable.Id_ArrowIcon);
        holder.setText(ResourceTable.Id_tvGroupTitle, text.getChildItem(), Color.BLACK,
                ResUtil.getIntDimen(context, ResourceTable.Float_child_text_size));
        //if the current list item is present in mSelectedChild, then check it
        boolean isPresent = false;
        for (int i = 0; i < mSelectedChild.size(); i++) {
            if (mSelectedChild.get(i).isEquals(text)) {
                isPresent = true;
                break;
            }
        }
        if (!isPresent) {
            holder.setUnChecked(ResourceTable.Id_checkbtn);
        } else {
            holder.setChecked(ResourceTable.Id_checkbtn);
        }
    }

    /**
     * to handle the clicked item.
     *
     * @param expandableListAdapter adapter for holding the list data.
     * @param position position of the clicked item.
     */
    public void handleClickedItem(ExpandableListAdapter<ParentChild> expandableListAdapter, int position) {
        ParentChild value = mGroupNameItem.get(position);
        String clickedItem = value.getChildItem();
        if (!mTempChildNameItem.contains(clickedItem)) {
            checkChild(position);
            expandableListAdapter.setData(mGroupNameItem);
        } else {
            int index = -1;
            for (int i = 0; i < mSelectedChild.size(); i++) {
                if (mSelectedChild.get(i).isEquals(value)) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                mSelectedChild.remove(index);
            } else {
                mSelectedChild.add(value);
            }
            expandableListAdapter.setData(mGroupNameItem);
        }
    }

    /**
     * This method is used to show toast dialog if boston is checked already.
     */
    public void showToastForBoston() {
        ToastDialog toastDialog = new ToastDialog(context);
        toastDialog.setAlignment(TextAlignment.BOTTOM);
        toastDialog.setText(ResUtil.getString(context, ResourceTable.String_clicked_on_Boston_item));
        toastDialog.show();
    }
}
