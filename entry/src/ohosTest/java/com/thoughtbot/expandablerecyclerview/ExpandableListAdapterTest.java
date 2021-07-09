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
package com.thoughtbot.expandablerecyclerview;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import ohos.app.Context;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class ExpandableListAdapterTest {
    public Context context;
    public List<String> names;
    public List<Integer> images;
    public int mLayoutId;
    public ExpandableListAdapter items;

    @Test
    public void testgetCountwithNull(){
        
        names= new ArrayList<>();
        items= new ExpandableListAdapter<String >(context, names, images, mLayoutId) {
            @Override
            protected void bind(ViewHolder holder, String s, Integer image, int position) {
            }
        };
        int res= items.getCount();
        assertEquals(0,res);
    }

    @Test
    public void testgetCount(){
        names= new ArrayList<>();
        names.add("Item1");
        names.add("Item2");
        items= new ExpandableListAdapter<String >(context, names, images, mLayoutId) {
            @Override
            protected void bind(ViewHolder holder, String s, Integer image, int position) {
            }
        };
        int res= items.getCount();
        assertEquals(2,res);
    }

    @Test
    public void testgetItemWithNull(){
        names= new ArrayList<>();
        items= new ExpandableListAdapter<String >(context, names, images, mLayoutId) {
            @Override
            protected void bind(ViewHolder holder, String s, Integer image, int position) {
            }
        };
        String res= (String) items.getItem(5);
        assertNull(res);
    }

    @Test
    public void testgetItem(){
        names= new ArrayList<>();
        names.add("Item1");
        names.add("Item2");
        items= new ExpandableListAdapter<String >(context, names, images, mLayoutId) {
            @Override
            protected void bind(ViewHolder holder, String s, Integer image, int position) {
            }
        };
        String res= (String) items.getItem(0);
        assertEquals("Item1",res);
    }

    @Test
    public void testgetItemWithOutOfBound(){
        names= new ArrayList<>();
        names.add("Item1");
        names.add("Item2");
        items= new ExpandableListAdapter<String >(context, names, images, mLayoutId) {
            @Override
            protected void bind(ViewHolder holder, String s, Integer image, int position) {
            }
        };
        String res= (String) items.getItem(5);
        assertNull(res);
    }
}
