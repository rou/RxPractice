package io.rou.rxpractice.models;

import android.content.Context;
import android.content.SharedPreferences;

import rx.Observable;
import rx.Subscriber;

public class BroadcastConfigure {
    private static final String PREFERENCES_NAME = "broadcastConfigure";
    private SharedPreferences mSharedPreferences;

    private final class SharedPreferencesName {
        public static final String IS_MUTE = "isMute";
    }

    private final class SharedPreferencesDefaultValue {
        public static final boolean IS_MUTE = false;
    }

    public BroadcastConfigure(Context context) {
        mSharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        mSharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                switch (key) {
                    case SharedPreferencesName.IS_MUTE:
                        isMuteObservable().nest();
                        break;
                }
            }
        });
    }

    public boolean getIsMute() {
        return mSharedPreferences.getBoolean(SharedPreferencesName.IS_MUTE, SharedPreferencesDefaultValue.IS_MUTE);
    }

    public void setIsMute(boolean value) {
        mSharedPreferences.edit().putBoolean(SharedPreferencesName.IS_MUTE, value).apply();
    }

    public Observable<Boolean> isMuteObservable() {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> observer) {
                try {
                    if (!observer.isUnsubscribed()) {
                        observer.onNext(getIsMute());
                    }
                } catch (Exception exception) {
                    observer.onError(exception);
                }
            }
        });
    }

    public void reset() {
        mSharedPreferences.edit().clear();
    }
}
