/*
 * Copyright (C) 2022 ThinkingData
 */

package cn.thinkingdata.android;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * ExpandableListView This interface can be implemented through Adapter to
 * add custom properties to the control click event that is triggered when an item is clicked.
 */
public interface ThinkingExpandableListViewItemTrackProperties {
    /**
     * Property when clicking on the item at childPosition.
     *
     * @param groupPosition The child's parent group's position.
     * @param childPosition The child position within the group.
     * @return
     */
    JSONObject getThinkingChildItemTrackProperties(int groupPosition, int childPosition) throws JSONException;

    /**
     * Property when clicking groupPosition item.
     *
     * @param groupPosition the group position
     * @return
     */
    JSONObject getThinkingGroupItemTrackProperties(int groupPosition) throws JSONException;
}