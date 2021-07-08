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

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.thoughtbot.expandablerecyclerview.util.TextUtils;

public class TextUtilsTest {

    @Test
    public void testIsEmptyWithNull() {
        boolean val1= TextUtils.isEmpty(null);
        assertTrue(val1);
    }

    @Test
    public void testIsEmptyWithEmpty() {
        boolean val1= TextUtils.isEmpty("");
        assertTrue(val1);
    }

    @Test
    public void testIsEmptyWithNotNull() {
        boolean val1= TextUtils.isEmpty("a12b%&c");
        assertFalse(val1);
    }
}