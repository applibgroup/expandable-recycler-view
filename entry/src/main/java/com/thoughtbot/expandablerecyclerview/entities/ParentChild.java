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

package com.thoughtbot.expandablerecyclerview.entities;

/**
 * To store the Parent and Child of each List Item.
 */
public class ParentChild {
    String parentItem;
    String childItem;

    public ParentChild(String parentItem, String childItem) {
        this.parentItem = parentItem;
        this.childItem = childItem;
    }

    public String getParentItem() {
        return parentItem;
    }

    public void setParentItem(String parentItem) {
        this.parentItem = parentItem;
    }

    public String getChildItem() {
        return childItem;
    }

    public void setChildItem(String childItem) {
        this.childItem = childItem;
    }

    public boolean isEquals(ParentChild obj) {
        return (this.parentItem.equals(obj.getParentItem()) && this.childItem.equals(obj.getChildItem()));
    }
}
