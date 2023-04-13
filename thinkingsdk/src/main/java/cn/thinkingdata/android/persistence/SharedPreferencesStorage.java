/*
 * Copyright (C) 2022 ThinkingData
 */

package cn.thinkingdata.android.persistence;

import android.content.SharedPreferences;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

abstract class SharedPreferencesStorage<T> {

    protected T data;
    final String storageKey;
    private final Future<SharedPreferences> loadStoredPreferences;

    SharedPreferencesStorage(final Future<SharedPreferences> loadStoredPreferences, final String storageKey) {
        this.loadStoredPreferences = loadStoredPreferences;
        this.storageKey = storageKey;
    }

    // return default value.
    T create() {
        return null;
    }

    // save the data to sharedPreference. If the type of T is not String, override this method.
    void save(SharedPreferences.Editor editor, T data) {
        editor.putString(storageKey, (String) data);
        editor.apply();
    }

    // load the data from sharedPreference. If the type of T is not String, override this method.
    void load(SharedPreferences sharedPreferences) {
        String data = sharedPreferences.getString(this.storageKey, null);
        if (data == null) {
            put(create());
        } else {
            this.data = (T) data;
        }
    }

    /**
     * Gets the value stored in SharedPreference.
     *
     * @return value of the storageKey.
     */
    public T get() {
        if (this.data == null) {
            synchronized (loadStoredPreferences) {
                SharedPreferences sharedPreferences = null;
                try {
                    sharedPreferences = loadStoredPreferences.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if (sharedPreferences != null) {
                    load(sharedPreferences);
                }
            }
        }
        return this.data;
    }

    /**
     * Set the value of the storage key and save it to sharedPreference.
     *
     * @param data value.
     */
    public void put(T data) {
        this.data = data;

        synchronized (loadStoredPreferences) {
            final SharedPreferences.Editor editor = getEditor();
            if (editor != null) {
                save(editor, this.data);
            }
        }
    }

    private SharedPreferences.Editor getEditor() {
        SharedPreferences sharedPreferences = null;
        try {
            sharedPreferences = loadStoredPreferences.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (sharedPreferences != null) {
            return sharedPreferences.edit();
        } else {
            return null;
        }
    }

}
