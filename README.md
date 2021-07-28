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
3. For using Diffre from a remote repository in separate application, add the below dependencies in entry/build.gradle file.
``` java
dependencies {
	implementation 'dev.applibgroup:expandablerecyclerview:1.0.0'
	testCompile 'junit:junit:4.13'
}
```

# Usage
Let's say you are a rock star 🎸 and you want to build an app to show a list of your favorite Genres with a list of their top Artists.

First create the Ability Slice and call the respective controller class by passing view and context as arguments and then call the `initViews` method of that class.
``` java
public class ExpandAbilitySlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        ComponentContainer rootView = (ComponentContainer) LayoutScatter.getInstance(this)
                .parse(ResourceTable.Layout_ability_expandlist, null, false);
        super.setUIContent(rootView);
        ExpandAbilityController controller = new ExpandAbilityController(this, rootView);
        controller.initViews();
    }
}
```
Each controller class extends the common helper class. Inside the controller class, `initViews` method will call `initializeViews` method of the parent class to initialize the view components.
``` java
public void initViews() {
        super.initializeViews(ResourceTable.Id_root_expand, ResourceTable.Id_lcGroupItems_expand);
        toggleBtn = (Button) rootView.findComponentById(ResourceTable.Id_toogle);
        this.prepareExpandableListAdapter();
}
```
```java
protected void initializeViews(int rootViewId, int containerId) {
	ScrollView parentLayout = (ScrollView) rootView.findComponentById(rootViewId);
	parentLayout.setBackground(getShapeElement(ResUtil.getColor(context, ResourceTable.Color_white)));
	mGroupContainer = (ExpandableListContainer) rootView.findComponentById(containerId);
	getGroupItems();
        getGroupIcons();
}
```
Then, Add all the Genre items in `mGroupNameItem` and their corresponding image in `mGroupImageItem `. We are maintaining a copy of the group items for finding the child items of each groups from a Hashmap by using their index value in `mFinalGroupNameItem` list.
``` java
private void getGroupItems() {
        mGroupNameItem.add(new ParentChild(null, ResUtil.getString(context, ResourceTable.String_item_Rock)));
        mGroupNameItem.add(new ParentChild(null, ResUtil.getString(context, ResourceTable.String_item_Jazz)));
        mGroupNameItem.add(new ParentChild(null, ResUtil.getString(context, ResourceTable.String_item_Classic)));
        mGroupNameItem.add(new ParentChild(null, ResUtil.getString(context, ResourceTable.String_item_Salsa)));
        mGroupNameItem.add(new ParentChild(null, ResUtil.getString(context, ResourceTable.String_item_Bluegrass)));
        mFinalGroupNameItem.add(ResUtil.getString(context, ResourceTable.String_item_Rock));
        mFinalGroupNameItem.add(ResUtil.getString(context, ResourceTable.String_item_Jazz));
        mFinalGroupNameItem.add(ResUtil.getString(context, ResourceTable.String_item_Classic));
        mFinalGroupNameItem.add(ResUtil.getString(context, ResourceTable.String_item_Salsa));
        mFinalGroupNameItem.add(ResUtil.getString(context, ResourceTable.String_item_Bluegrass));
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
Now the binding of the data to the view is done inside the `prepareExpandableListAdapter` method. Here, we will decide whether the item is a child or a group item by calling `handleListItem` and checking whether it is present in `mTempChildNameItem` or not. After this, we will set the adapter and OnItemClicklistener.
``` java
protected void prepareExpandableListAdapter() {
	expandableListAdapter = new ExpandableListAdapter<ParentChild>(context,
		mGroupNameItem, mGroupImageItem, ResourceTable.Layout_ability_listview_item) {
	    @Override
	    protected void bind(ViewHolder holder, ParentChild text, Integer image, int position) {
		handleListItem(holder, text, image);
	    }
	};
	//setting the adapter
	setAdapter(expandableListAdapter);
	//setting the OnItemClickListener
	setListener(expandableListAdapter);
}
```
```java
private void setAdapter(ExpandableListAdapter<ParentChild> expandableListAdapter) {
        mGroupContainer.setItemProvider(expandableListAdapter);
    }
```
If the item is not present in `mTempChildNameItem`, then it is a parent/group item, otherwise it is a child item.
``` java
private void handleListItem(ExpandableListAdapter.ViewHolder holder, ParentChild text, Integer image) {
	if (!mTempChildNameItem.contains(text.getChildItem())) {
	    handleParentItem(holder, text, image);
	} else {
	    handleChildItem(holder, text);
	}
}
```
```java
private void handleParentItem(ExpandableListAdapter.ViewHolder holder, ParentChild text, Integer image) {
	// Set background for parent/Group
	holder.makeInvisibleButton(ResourceTable.Id_checkbtn);
	holder.makeInvisibleImage(ResourceTable.Id_childstar);
	holder.setGroupItemBackground(ResourceTable.Id_groupContainer, ResourceTable.Color_white);
	holder.setText(ResourceTable.Id_tvGroupTitle, text.getChildItem(), Color.GRAY,
		ResUtil.getIntDimen(context, ResourceTable.Float_group_text_size));
	holder.setGroupImage(ResourceTable.Id_ivGroupIcon, image,
		ShapeElement.RECTANGLE, Image.ScaleMode.STRETCH, ResourceTable.Color_white);
	if (!mTempGroupNameItem.contains(text.getChildItem())) {
	    // Set arrow down icon
	    holder.setGroupImage(ResourceTable.Id_ArrowIcon, ResourceTable.Media_arrow_Down,
		    ShapeElement.OVAL, Image.ScaleMode.CENTER, ResourceTable.Color_white);
	} else {
	    // Set arrow up icon
	    holder.setGroupImage(ResourceTable.Id_ArrowIcon, ResourceTable.Media_arrow_Up,
		    ShapeElement.OVAL, Image.ScaleMode.CENTER, ResourceTable.Color_white);
	}
}
```
`handleChildItem` method will be different for different ability. For expand ability- 
```java
protected void handleChildItem(ExpandableListAdapter.ViewHolder holder, ParentChild text) {
        // Add child items to list
        holder.makeInvisibleButton(ResourceTable.Id_checkbtn);
        holder.makeInvisibleImage(ResourceTable.Id_ArrowIcon);
        holder.setText(ResourceTable.Id_tvGroupTitle, text.getChildItem(), Color.GRAY,
                ResUtil.getIntDimen(context, ResourceTable.Float_child_text_size));
}
```
Inside the `setOnItemClickListener`, we call the `handleClickedItem` method to check if the clickedItem is a Group item (i.e. Genre) or the Child item (i.e. Artist).
``` java
private void setListener(ExpandableListAdapter<ParentChild> expandableListAdapter) {
        expandableListAdapter.setOnItemClickListener((component, position) ->
                handleClickedItem(expandableListAdapter, position));
}
```
``` java
protected void handleClickedItem(ExpandableListAdapter<ParentChild> expandableListAdapter, int position) {
        ParentChild value = mGroupNameItem.get(position);
        String clickedItem = value.getChildItem();
        if (!mTempChildNameItem.contains(clickedItem)) {
            handleClickedParentItem(expandableListAdapter, position);
        } else {
            handleClickedChildItem(expandableListAdapter, value);
        }
}
```
```java
private void handleClickedParentItem(ExpandableListAdapter<ParentChild> expandableListAdapter, int position) {
        checkChild(position);
        expandableListAdapter.setData(mGroupNameItem);
}
```
`handleClickedChildItem` method will be different for different ability slice. For expand ability- 
```java
@Override
protected void handleClickedChildItem(ExpandableListAdapter<ParentChild> expandableListAdapter, ParentChild value) {
        super.showToast();
}
```
`mTempGroupNameItem` contains all the Group item that are in expand state and `mTempChildNameItem` will contains child of such GroupItems.
While collapsing the group, we will remove the group items from `mTempGroupNameItem` and their child items from `mTempChildNameItem`.
```java
private void checkChild(int position) {
        ParentChild value = mGroupNameItem.get(position);
        String clickedItem = value.getChildItem();
        if (mTempGroupNameItem.contains(clickedItem)) {
            int actualItemPosition = mFinalGroupNameItem.indexOf(clickedItem);
            removeChildItems(actualItemPosition, position);
            mTempGroupNameItem.remove(clickedItem);
        } else {
            int actualItemPosition = mFinalGroupNameItem.indexOf(clickedItem);
            addChildItems(actualItemPosition, clickedItem, position);
            mTempGroupNameItem.add(clickedItem);
        }
}
```
If it is the Group item and it is not there in `mTempGroupNameItem`, that means we have to expand it and then add it to `mTempGroupNameItem` and their corresponding child items to `mTempChildNameItem`.
``` java
private void addChildItems(int index, String clickedItem, int itemPositionFromGroup) {
        String[] childItems = childItems().get(index);
        for (String item : childItems) {
            itemPositionFromGroup = itemPositionFromGroup + 1;
            mGroupNameItem.add(itemPositionFromGroup, new ParentChild(clickedItem, item));
            mTempChildNameItem.add(item);
            mGroupImageItem.add(itemPositionFromGroup, ResourceTable.Media_star);
        }
}
``` 
If the `clickedItem` is already there in `mTempGroupNameItem`, then we have to collapse it and then remove it from `mTempGroupNameItem` and the 
corresponding child items from `mTempChildNameItem`.
``` java
private void removeChildItems(int position, int itemPositionFromGroup) {
        String[] items = childItems().get(position);
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
protected void getFavouriteItems() {
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
@Override
protected void handleClickedChildItem(ExpandableListAdapter<ParentChild> expandableListAdapter, ParentChild value) {
        String parentGroup = value.getParentItem();
        String clickedItem = value.getChildItem();
        if (mSelectedChild.containsKey(parentGroup)) {
            mSelectedChild.remove(parentGroup);
        }
        mSelectedChild.put(parentGroup, clickedItem);
        expandableListAdapter.setData(mGroupNameItem);
}
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
ExpandableRecyclerView is Copyright (c) 2016 thoughtbot, inc. It is free software, and may be redistributed under the terms specified in the [LICENSE](https://github.com/applibgroup/expandable-recycler-view/blob/master/LICENSE) file.
