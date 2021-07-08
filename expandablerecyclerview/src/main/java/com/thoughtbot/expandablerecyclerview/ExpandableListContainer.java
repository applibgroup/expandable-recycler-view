package com.thoughtbot.expandablerecyclerview;

import ohos.agp.components.AttrSet;
import ohos.agp.components.ListContainer;
import ohos.app.Context;

/**
 * Expandable List Container.
 */
public class ExpandableListContainer extends ListContainer {
    private ScrolledListener mOnScrollListener;

    public ExpandableListContainer(Context context) {
        super(context);
        init();
    }

    public ExpandableListContainer(Context context, AttrSet attrs) {
        super(context, attrs);
        init();
    }

    public ExpandableListContainer(Context context, AttrSet attrs, int defStyle) {
        super(context, attrs, "");
        init();
    }

    private void init() {
        super.setScrolledListener((component, i, i1, i2, i3) -> {
            if (mOnScrollListener != null) {
                mOnScrollListener.onContentScrolled(component, i, i1, i2, i3);
            }
        });
    }
}

