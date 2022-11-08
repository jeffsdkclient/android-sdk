/*
 * Copyright (C) 2022 ThinkingData
 */

package cn.thinkingdata.android.utils;

import android.os.SystemClock;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * 事件校准.
 * */
public class TDTimeCalibrated implements ITime {
    private final long mSystemElapsedRealtime;
    private final TimeZone mTimeZone;
    private final ICalibratedTime mCalibratedTime;

    private Date mDate;

    /**
     * < TDTimeCalibrated >.
     *
     * @param calibratedTime 接口
     * @param timeZone 时区
     */
    public TDTimeCalibrated(ICalibratedTime calibratedTime, TimeZone timeZone) {
        mCalibratedTime = calibratedTime;
        mTimeZone = timeZone;
        mSystemElapsedRealtime = SystemClock.elapsedRealtime();
    }

    private synchronized Date getDate() {
        if (null == mDate) {
            mDate = mCalibratedTime.get(mSystemElapsedRealtime);
        }
        return mDate;
    }

    @Override
    public String getTime() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(TDConstants.TIME_PATTERN, Locale.CHINA);
            dateFormat.setTimeZone(mTimeZone);
            String ret = dateFormat.format(getDate());
            if (!Pattern.compile(TDConstants.TIME_CHECK_PATTERN).matcher(ret).find()) {
                ret = TDUtils.formatTime(getDate(), mTimeZone);
            }
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Double getZoneOffset() {
        return TDUtils.getTimezoneOffset(getDate().getTime(), mTimeZone);
    }
}
