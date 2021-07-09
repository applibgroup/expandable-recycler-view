package com.thoughtbot.expandablerecyclerview;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import com.thoughtbot.expandablerecyclerview.slice.MainAbilitySlice;

/**
 * Main Ability.
 */
public class MainAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());
    }
}
