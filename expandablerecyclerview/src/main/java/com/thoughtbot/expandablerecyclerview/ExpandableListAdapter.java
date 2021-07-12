package com.thoughtbot.expandablerecyclerview;

import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_CONTENT;
import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_PARENT;
import static ohos.agp.utils.LayoutAlignment.TOP;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Checkbox;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DependentLayout;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Image;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.app.Context;
import ohos.media.image.PixelMap;
import com.thoughtbot.expandablerecyclerview.util.ResUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Expandable List Adapter class.
 */
public abstract class ExpandableListAdapter<T> extends BaseItemProvider {
    private Context mContext;
    private List<T> mNames;
    private List<Integer> mImages;
    private int mLayoutId;
    private OnItemClickListener mOnItemClickListener;

    /**
     * Constructor to get data from parent class.
     *
     * @param context context of view
     * @param layoutId resource layout to be drawn
     * @param names group item name to be displayed
     * @param images group item images to be displayed
     */
    protected ExpandableListAdapter(Context context, List<T> names, List<Integer> images, int layoutId) {
        this.mContext = context;
        this.mNames = names;
        this.mImages = images;
        this.mLayoutId = layoutId;
    }

    /**
     * This method is used to set updated data to the view.
     *
     * @param data Updated data after collapse & expand the view
     */
    public void setData(List<T> data) {
        this.mNames = data;
        notifyDataChanged();
    }

    @Override
    public int getCount() {
        if (mNames != null) {
            return mNames.size();
        } else {
            return 0;
        }
    }

    @Override
    public T getItem(int position) {
        if (mNames == null || mNames.size() <= position) {
            return null;
        } else {
            return mNames.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Component getComponent(int position, Component tempComponent, ComponentContainer parent) {
        Component convertComponent = new DirectionalLayout(mContext);
        ((DirectionalLayout) convertComponent).setOrientation(Component.HORIZONTAL);
        ComponentContainer.LayoutConfig layoutConfig = convertComponent.getLayoutConfig();
        layoutConfig.width = MATCH_PARENT;
        layoutConfig.height = MATCH_CONTENT;
        convertComponent.setLayoutConfig(layoutConfig);

        DirectionalLayout dlItemParent = new DirectionalLayout(mContext);
        dlItemParent.setLayoutConfig(new DirectionalLayout.LayoutConfig(0,
                MATCH_CONTENT, TOP, 1));
        Component childConvertComponent = LayoutScatter.getInstance(mContext)
                .parse(mLayoutId, null, false);

        childConvertComponent.setClickedListener(component -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(component, position);
            }
        });
        ViewHolder viewHolder;
        dlItemParent.addComponent(childConvertComponent);
        ((ComponentContainer) convertComponent).addComponent(dlItemParent);
        viewHolder = new ViewHolder(childConvertComponent);
        bind(viewHolder, getItem(position), mImages.get(position), position);
        return convertComponent;
    }

    /**
     * This method is used to provide item click listener.
     *
     * @param onItemClickListener object of our interface
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    protected abstract void bind(ViewHolder holder, T s, Integer image, int position);

    /**
     * creating the viewHolder and its method to set the attribute.
     */
    public static class ViewHolder {
        HashMap<Integer, Component> mViews = new HashMap<>();
        Component itemView;

        ViewHolder(Component component) {
            this.itemView = component;
        }

        /**
         * This method is used to set text of the group & child items.
         *
         * @param viewId resource id of text
         * @param text string value to be displayed for group & child item
         * @param color text color for group& child item
         * @param textSize text size for group& child item
         */
        public void setText(int viewId, String text, Color color, int textSize) {
            if (getView(viewId) instanceof Text) {
                Text tvName = getView(viewId);
                tvName.setText(text);
                tvName.setTextColor(color);
                tvName.setTextSize(textSize);
            }
        }

        /**
         * This method is to hide the check box.
         *
         * @param viewId resource id of check button
         */
        public void makeInvisibleButton(int viewId) {
            if (getView(viewId) instanceof Checkbox) {
                Checkbox radioButton = getView(viewId);
                radioButton.setVisibility(Component.HIDE);
            }
        }

        /**
         * This method is to hide the Image.
         *
         * @param viewId resource id of the image
         */
        public void makeInvisibleImage(int viewId) {
            if (getView(viewId) instanceof Image) {
                Image img = getView(viewId);
                img.setVisibility(Component.HIDE);
            }
        }

        /**
         * This method is to check the Checkbox.
         *
         * @param viewId resource id of check button
         */
        public void setChecked(int viewId) {
            if (getView(viewId) instanceof Checkbox) {
                Checkbox radioButton = getView(viewId);
                radioButton.setChecked(true);
            }
        }

        /**
         * This method is to uncheck the Checkbox.
         *
         * @param viewId resource id of check button
         */
        public void setUnChecked(int viewId) {
            if (getView(viewId) instanceof  Checkbox) {
                Checkbox radioButton = getView(viewId);
                radioButton.setChecked(false);
            }
        }

        /**
         * This method is used to set image for group items.
         *
         * @param viewId view id if image
         * @param imageResId image resource id to be displayed in image
         * @param shape background shape for image
         * @param scaleMode scale mode set on image
         * @param groupImageColor color of the image
         */
        public void setGroupImage(int viewId, Integer imageResId,
                                  int shape, Image.ScaleMode scaleMode, int groupImageColor) {
            if (getView(viewId) instanceof Image) {
                Image imageView = getView(viewId);
                ShapeElement shapeElement = new ShapeElement();
                shapeElement.setRgbColor(RgbColor.fromArgbInt(ResUtil
                        .getColor(imageView.getContext(), groupImageColor)));
                shapeElement.setShape(shape);
                imageView.setBackground(shapeElement);
                Optional<PixelMap> groupIconPixelMap = ResUtil.getPixelMap(imageView.getContext(), imageResId);
                groupIconPixelMap.ifPresent(imageView::setPixelMap);
                imageView.setScaleMode(scaleMode);
            }
        }

        /**
         * This method is used to set child item image.
         *
         * @param viewId view id for image
         * @param imageResId image resource id to be set on image
         * @param height height of the image
         * @param margin margin for the image
         * @param width width of the image
         */
        public void setChildImage(int viewId, Integer imageResId, int width, int height, int margin) {
            if (getView(viewId) instanceof Image) {
                Image imageView = getView(viewId);
                imageView.setWidth(ResUtil.getIntDimen(itemView.getContext(), width));
                imageView.setHeight(ResUtil.getIntDimen(itemView.getContext(), height));
                imageView.setMarginLeft(ResUtil.getIntDimen(itemView.getContext(), margin));
                Optional<PixelMap> groupIconPixelMap = ResUtil.getPixelMap(imageView.getContext(), imageResId);
                groupIconPixelMap.ifPresent(imageView::setPixelMap);
                imageView.setScaleMode(Image.ScaleMode.STRETCH);
            }
        }

        /**
         * This method is used to set background of group items.
         *
         * @param groupItemLayoutId Layout id
         * @param groupItemColor layout color
         */
        public void setGroupItemBackground(int groupItemLayoutId, int groupItemColor) {
            if (getView(groupItemLayoutId) instanceof DependentLayout) {
                DependentLayout groupContainer = (DependentLayout) itemView.findComponentById(groupItemLayoutId);
                ShapeElement containerElement = new ShapeElement();
                containerElement.setShape(ShapeElement.RECTANGLE);
                containerElement.setRgbColor(RgbColor.fromArgbInt(ResUtil
                        .getColor(groupContainer.getContext(), groupItemColor)));
                groupContainer.setBackground(containerElement);
            }
        }

        /**
         * This method will return the view based on id.
         *
         * @param viewId view id of component
         * @param <E> component to be return based on id
         * @return component
         */
        <E extends Component> E getView(int viewId) {
            E view = (E) mViews.get(viewId);
            if (view == null) {
                view = (E) itemView.findComponentById(viewId);
                mViews.put(viewId, view);
            }
            return view;
        }
    }

    /**
     *  interface for OnItemClick.
     */
    public interface OnItemClickListener {
        void onItemClick(Component component, int position);
    }
}