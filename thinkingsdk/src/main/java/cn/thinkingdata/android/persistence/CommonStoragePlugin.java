/*
 * Copyright (C) 2022 ThinkingData
 */
package cn.thinkingdata.android.persistence;
import android.content.Context;

/**
 * <  >.
 *
 * @author liulongbing
 * @create 2022/9/7
 * @since
 */
public class CommonStoragePlugin extends AbstractStoragePlugin {

    private static final String PREFERENCE_NAME = "com.thinkingdata.analyse";

    private  StorageLoginID mLoginId;
    private  StorageIdentifyId mIdentifyId;
    private  StorageEnableFlag mEnableFlag;
    private  StorageOptOutFlag mOptOutFlag;
    private  StoragePausePostFlag mPausePostFlag;
    private  StorageSuperProperties mSuperProperties;

    public CommonStoragePlugin(Context context, String name) {
        super(context, PREFERENCE_NAME + "_" + name);
    }

    @Override
    protected void createStorage() {
        mLoginId = new StorageLoginID(storedSharedPrefs);
        mIdentifyId = new StorageIdentifyId(storedSharedPrefs);
        mSuperProperties = new StorageSuperProperties(storedSharedPrefs);
        mOptOutFlag = new StorageOptOutFlag(storedSharedPrefs);
        mEnableFlag = new StorageEnableFlag(storedSharedPrefs);
        mPausePostFlag = new StoragePausePostFlag(storedSharedPrefs);
    }

    @Override
    protected <T> SharedPreferencesStorage<T> getSharePreferenceStorage(LocalStorageType type) {
        switch (type){
            case LOGIN_ID:
                return (SharedPreferencesStorage<T>) mLoginId;
            case IDENTIFY:
                return (SharedPreferencesStorage<T>) mIdentifyId;
            case SUPER_PROPERTIES:
                return (SharedPreferencesStorage<T>) mSuperProperties;
            case OPT_OUT:
                return (SharedPreferencesStorage<T>) mOptOutFlag;
            case ENABLE:
                return (SharedPreferencesStorage<T>) mEnableFlag;
            case PAUSE_POST:
                return (SharedPreferencesStorage<T>) mPausePostFlag;
        }
        return null;
    }
}
