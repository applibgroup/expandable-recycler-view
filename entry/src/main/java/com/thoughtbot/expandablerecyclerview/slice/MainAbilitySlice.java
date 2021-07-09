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

package com.thoughtbot.expandablerecyclerview.slice;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import com.thoughtbot.expandablerecyclerview.ResourceTable;

/**
 * MainAbility that calls each.
 * of the different type of Ability
 */
public class MainAbilitySlice extends AbilitySlice {

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);

        Button expand = (Button) findComponentById(ResourceTable.Id_expand_button);
        expand.setClickedListener(list -> present(new ExpandAbilitySlice(), new Intent()));

        Button multitype = (Button) findComponentById(ResourceTable.Id_multitype_button);
        multitype.setClickedListener(list -> present(new MultiTypeAbilitySlice(), new Intent()));

        Button multicheck = (Button) findComponentById(ResourceTable.Id_multicheck_button);
        multicheck.setClickedListener(list -> present(new MultiCheckAbilitySlice(), new Intent()));

        Button singlecheck = (Button) findComponentById(ResourceTable.Id_singlecheck_button);
        singlecheck.setClickedListener(list -> present(new SingleCheckAbilitySlice(), new Intent()));

        Button multitypecheck = (Button) findComponentById(ResourceTable.Id_multitypecheck_button);
        multitypecheck.setClickedListener(list -> present(new MultiTypeAndCheckAbilitySlice(), new Intent()));
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
