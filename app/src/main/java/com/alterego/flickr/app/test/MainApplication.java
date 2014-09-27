package com.alterego.flickr.app.test;

import android.app.Application;

import com.alterego.advancedandroidlogger.implementations.DetailedAndroidLogger;
import com.alterego.advancedandroidlogger.interfaces.IAndroidLogger;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix="m")
public class MainApplication extends Application {

    private static final String LOGGING_TAG = "FlickerTest";
    @Getter
    private SettingsManager mSettingsManager;
    @Getter private static MainApplication mMainApplication;


    @Override
    public void onCreate() {
        super.onCreate();
        mMainApplication = this;
        mSettingsManager = new SettingsManager(this, new DetailedAndroidLogger(LOGGING_TAG, IAndroidLogger.LoggingLevel.VERBOSE), getDefaultImageLoaderConfiguration());

    }

    public ImageLoaderConfiguration getDefaultImageLoaderConfiguration() {

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.ic_action_photo)
                .showImageOnLoading(R.drawable.ic_action_photo)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        return config;

    }

}
