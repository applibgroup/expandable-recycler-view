[![Build](https://github.com/applibgroup/expandable-recycler-view/actions/workflows/main.yml/badge.svg)](https://github.com/applibgroup/expandable-recycler-view/actions/workflows/main.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=applibgroup_expandable-recycler-view&metric=alert_status)](https://sonarcloud.io/dashboard?id=applibgroup_expandable-recycler-view)

# ExpandableRecyclerView
This is an HarmonyOS library with Custom BaseItemProvider for expanding and collapsing groups.

# Source
This library is inspired by version 1.5 of [ExpandableRecyclerView](https://github.com/thoughtbot/expandable-recycler-view) library.

# Features
This library allows us to add child items within the group items in a List View. It also demonstrates how we can add our favourite, single checker and multichecker child Items.

# Dependency
1. For using ExpandableRecyclerView module in sample app, include the source code and add the below dependencies in entry/build.gradle to generate hap/expandablerecyclerview.har.
``` java
dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar', '*.har'])
    testImplementation 'junit:junit:4.13'
    implementation project(':expandablerecyclerview')
}
```
2. For using ExpandableRecyclerView in separate application using har file, add the har file in the entry/libs folder and add the dependencies in entry/build.gradle file.
``` java
dependencies {
	implementation fileTree(dir: 'libs', include: ['*.har'])
	testImplementation 'junit:junit:4.13'
}
```

# Usage
Let's say you are a rock star 🎸 and you want to build an app to show a list of your favorite Genres with a list of their top Artists.

First create the Ability Slice and call the helper class by passing view and context as arguments and then call the `initViews` method of the helper class.
``` java
public class ExpandAbilitySlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        ComponentContainer rootView = (ComponentContainer) LayoutScatter.getInstance(this)
                .parse(ResourceTable.Layout_ability_expandlist, null, false);
        super.setUIContent(rootView);
        ExpandAbilityHelper helper = new ExpandAbilityHelper(this, rootView);
        helper.initViews();
    }
}
```
Inside the helper class, Initialize the view components by `initViews` method.
``` java
public void initViews() {
	getGroupItems();
	getGroupIcons();
	tooglebtn = (Button) rootView.findComponentById(ResourceTable.Id_toogle);
	ScrollView parentLayout = (ScrollView) rootView.findComponentById(ResourceTable.Id_root_expand);
	parentLayout.setBackground(getShapeElement(ResUtil.getColor(context, ResourceTable.Color_white)));
	mGroupContainer = (ExpandableListContainer) rootView.findComponentById(ResourceTable.Id_lcGroupItems_expand);
	prepareExpandableListAdapter();
}
```
Then, Add all the Genre items in `mGroupNameItem` and their corresponding image in `mGroupImageItem `
``` java
private void getGroupItems() {
        mGroupNameItem.add(ResUtil.getString(context, ResourceTable.String_item_Rock));
        mGroupNameItem.add(ResUtil.getString(context, ResourceTable.String_item_Jazz));
        mGroupNameItem.add(ResUtil.getString(context, ResourceTable.String_item_Classic));
        mGroupNameItem.add(ResUtil.getString(context, ResourceTable.String_item_Salsa));
        mGroupNameItem.add(ResUtil.getString(context, ResourceTable.String_item_Bluegrass));
        mFinalGroupNameItem.addAll(mGroupNameItem);
}
```
``` java
private void getGroupIcons() {
	mGroupImageItem.add(ResourceTable.Media_rock);
	mGroupImageItem.add(ResourceTable.Media_jazz);
	mGroupImageItem.add(ResourceTable.Media_classic);
	mGroupImageItem.add(ResourceTable.Media_salsa);
	mGroupImageItem.add(ResourceTable.Media_bluegrass);
}
```
Now the binding of the data to the view is done inside the `prepareExpandableListAdapter` method. Here, we will decide whether the item is a child or a group item by checking whether it is present in `mTempChildNameItem` or not.
``` java
ExpandableListAdapter<String> expandableListAdapter = new ExpandableListAdapter<String>(context,
                mGroupNameItem, mGroupImageItem, ResourceTable.Layout_ability_listview_item) {
    @Override
    protected void bind(ViewHolder holder, String text, Integer image, int position) {
	if (!mTempChildNameItem.contains(text)) {
	    // Set green background for parent/Group
	    holder.makeInvisibleButton(ResourceTable.Id_checkbtn);
	    holder.makeInvisibleImage(ResourceTable.Id_childstar);
	    holder.setGroupItemBackground(ResourceTable.Id_groupContainer, ResourceTable.Color_white);
	    holder.setText(ResourceTable.Id_tvGroupTitle, text, Color.GRAY,
		    ResUtil.getIntDimen(context, ResourceTable.Float_group_text_size));
	    holder.setGroupImage(ResourceTable.Id_ivGroupIcon, image,
		    ShapeElement.RECTANGLE, Image.ScaleMode.STRETCH, ResourceTable.Color_white);
	    if (!mTempGroupNameItem.contains(text)) {
		// Set divider & arrow down icon
		holder.setGroupImage(ResourceTable.Id_ArrowIcon, ResourceTable.Media_arrow_Down,
			ShapeElement.OVAL, Image.ScaleMode.CENTER, ResourceTable.Color_white);
	    } else {
		// Remove divider & arrow up icon
		holder.setGroupImage(ResourceTable.Id_ArrowIcon, ResourceTable.Media_arrow_Up,
			ShapeElement.OVAL, Image.ScaleMode.CENTER, ResourceTable.Color_white);
	    }
	} else {
	    // Add child items to list
	    holder.makeInvisibleButton(ResourceTable.Id_checkbtn);
	    holder.makeInvisibleImage(ResourceTable.Id_checkbtn);
	    holder.setText(ResourceTable.Id_tvGroupTitle, text, Color.GRAY,
		    ResUtil.getIntDimen(context, ResourceTable.Float_child_text_size));
	}
    }
};
mGroupContainer.setItemProvider(expandableListAdapter);
```
Then we set the onItemClickListener and call the `checkChild` method to check if the clickedItem is a Group item (i.e. Genre) or the Child item (i.e. Artist).
``` java
expandableListAdapter.setOnItemClickListener((component, position) -> {
    String clickedItem = mGroupNameItem.get(position);
    checkChild(clickedItem, expandableListAdapter);
});
```
`mTempGroupNameItem` contains all the Group item that are in expand state and `mTempChildNameItem` will contains child of such GroupItems.
While collapsing the group, we will remove the group items from `mTempGroupNameItem` and their child items from `mTempChildNameItem`.
``` java
private void checkChild(String clickedItem, ExpandableListAdapter<String> expandableListAdapter) {
	if (!mTempChildNameItem.contains(clickedItem)) {
	    if (mTempGroupNameItem.contains(clickedItem)) {
		int actualItemPosition = mFinalGroupNameItem.indexOf(clickedItem);
		removeChildItems(actualItemPosition, clickedItem);
		mTempGroupNameItem.remove(clickedItem);
	    } else {
		int actualItemPosition = mFinalGroupNameItem.indexOf(clickedItem);
		addChildItems(actualItemPosition, clickedItem);
		mTempGroupNameItem.add(clickedItem);
	    }
	    expandableListAdapter.setData(mGroupNameItem);
	} else {
	    showToast();
	}
}
```
If it is the Group item and it is not there in `mTempGroupNameItem`, that means we have to expand it and then add it to `mTempGroupNameItem` and their corresponding child items to `mTempChildNameItem`.
``` java
private void addChildItems(int actualPosition, String clickedItem) {
        String[] childItems = childItems().get(actualPosition);
        int itemPositionFromGroup = mGroupNameItem.indexOf(clickedItem);
        for (String item : childItems) {
            itemPositionFromGroup = itemPositionFromGroup + 1;
            mGroupNameItem.add(itemPositionFromGroup, item);
            mTempChildNameItem.add(item);
            mGroupImageItem.add(itemPositionFromGroup, ResourceTable.Media_star);
        }
}
``` 
If the `clickedItem` is already there in `mTempGroupNameItem`, then we have to collapse it and then remove it from `mTempGroupNameItem` and the 
corresponding child items from `mTempChildNameItem`.
``` java
private void removeChildItems(int position, String clickedItem) {
        String[] items = childItems().get(position);
        int itemPositionFromGroup = mGroupNameItem.indexOf(clickedItem);
        for (String name : items) {
            mGroupNameItem.remove(itemPositionFromGroup + 1);
            mGroupImageItem.remove(itemPositionFromGroup + 1);
            mTempChildNameItem.remove(name);
        }
}
``` 
### FavouriteItems
We can maintain our favorite child items, i.e. favourite Artists, by adding them to an ArrayList `mFavouriteItem`.
``` java
private void getFavouriteItems() {
        mFavouriteItem.add(ResUtil.getString(context, ResourceTable.String_item_child_Rock1));
        mFavouriteItem.add(ResUtil.getString(context, ResourceTable.String_item_child_Rock4));
        mFavouriteItem.add(ResUtil.getString(context, ResourceTable.String_item_child_Jazz1));
        mFavouriteItem.add(ResUtil.getString(context, ResourceTable.String_item_child_Jazz2));
        mFavouriteItem.add(ResUtil.getString(context, ResourceTable.String_item_child_Classic2));
        mFavouriteItem.add(ResUtil.getString(context, ResourceTable.String_item_child_Salsa1));
        mFavouriteItem.add(ResUtil.getString(context, ResourceTable.String_item_child_Bluegrass1));
}
```

### CheckableChildItems
We are maintaining mSelectedChild, which is a HashMap, and will map the checked item to its parent item. We are updating this HashMap whenever any list item is getting clicked. 
``` java
expandableListAdapter.setOnItemClickListener((component, position) -> {
    ParentChild value = mGroupNameItem.get(position);
    String clickedItem = value.getChildItem();
    if (!mTempChildNameItem.contains(clickedItem)) {
	if (mTempGroupNameItem.contains(clickedItem)) {
	    int actualItemPosition = mFinalGroupNameItem.indexOf(clickedItem);
	    removeChildItems(actualItemPosition, position);
	    mTempGroupNameItem.remove(clickedItem);
	} else {
	    int actualItemPosition = mFinalGroupNameItem.indexOf(clickedItem);
	    addChildItems(actualItemPosition, clickedItem, position);
	    mTempGroupNameItem.add(clickedItem);
	}
	expandableListAdapter.setData(mGroupNameItem);
    } else {
	String parentGroup = value.getParentItem();
	if (mSelectedChild.containsKey(parentGroup)) {
	    mSelectedChild.remove(parentGroup);
	}
	mSelectedChild.put(parentGroup, clickedItem);
	expandableListAdapter.setData(mGroupNameItem);
    }
});
```
To handle the case where a child item can be under multiple groups, we have created a class `ParentChild` that will store both the parent and the child name for each of the list items. While rendering the view, we will check only those items whose parent child pair is present in `mSelectedChild` HashMap.
``` java
if (mSelectedChild.containsKey(text.getParentItem()) && mSelectedChild.get(text.getParentItem())
	    .equals(text.getChildItem())) {
	holder.setChecked(ResourceTable.Id_checkbtn);
} else {
	holder.setUnChecked(ResourceTable.Id_checkbtn);
}
```

### Expand List
This will have a list of Group Items (i.e. Genre) and on clicking on it, the group will expand and their corresponding child items (i.e Artist) will be visible.
we also have `TOGGLE CLASSIC GROUP` button that can automatically expand and collapse the Classic group item.

![expand](https://user-images.githubusercontent.com/77639268/124965177-74149700-e03f-11eb-8cb2-496a2076aba5.gif)

### MultiType List
In this, we will have our favourite child items. While rendering the view we can add the star image in front of each of those items that are present in `mFavouriteItem`.

![multitype](https://user-images.githubusercontent.com/77639268/124965356-aa521680-e03f-11eb-9258-701c99451682.gif)

### SingleCheck List
This will allow the us to select one Artist among the other artists of a particulat Genre. It also provides us with the `CLEAR SELECTIONS` button that will clear all our previous choices. 

![singleCheck](https://user-images.githubusercontent.com/77639268/124965380-afaf6100-e03f-11eb-8fbe-613fa32c181d.gif)

### MultiCheck List
This will allow the us to select multiple Artist of a particulat Genre. It also provides us with the `PROGRAMMATICALLY CHECK BOSTON` button that will automatically check the boston Artist of Rock Genre.

![multicheck](https://user-images.githubusercontent.com/77639268/124965392-b5a54200-e03f-11eb-8676-7bd730c11796.gif)

# License
ExpandableRecyclerView is Copyright (c) 2016 thoughtbot, inc. It is free software, and may be redistributed under the terms specified in the [LICENSE](https://github.com/thoughtbot/expandable-recycler-view/blob/master/LICENSE) file.
