package com.thoughtbot.expandablerecyclerview.slice;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import com.thoughtbot.expandablerecyclerview.ResourceTable;
import com.thoughtbot.expandablerecyclerview.multitype.MultiTypeController;

/**
 * Multi Type Ability Slice.
 */
public class MultiTypeAbilitySlice extends AbilitySlice {

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        ComponentContainer rootView = (ComponentContainer) LayoutScatter.getInstance(this)
                .parse(ResourceTable.Layout_ability_multitype_list, null, false);
        super.setUIContent(rootView);
        MultiTypeController controller = new MultiTypeController(this, rootView);
        controller.initViews();
    }
}