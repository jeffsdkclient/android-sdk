/*
 * Copyright (C) 2022 ThinkingData
 */

package cn.thinkingdata.android;

import cn.thinkingdata.android.utils.TDConstants;
import org.json.JSONObject;


/**
 * Events that can be updated. Corresponds to the track_update operation.
 * In some scenarios, attributes in the event table need to be updated. You can create a TDUpdatableEvent and pass in an eventId identifying this data point.
 * Upon receiving such a request, the server uses the current attribute to override the previous attribute of the same name in the corresponding data of the eventId.
 */
public class TDUpdatableEvent extends ThinkingAnalyticsEvent {
    private final String mEventId;

    public TDUpdatableEvent(String eventName, JSONObject properties, String eventId) {
        super(eventName, properties);
        mEventId = eventId;
    }

    @Override
    String getExtraField() {
        return TDConstants.KEY_EVENT_ID;
    }

    @Override
    String getExtraValue() {
        return mEventId;
    }

    @Override
    TDConstants.DataType getDataType() {
        return TDConstants.DataType.TRACK_UPDATE;
    }
}
