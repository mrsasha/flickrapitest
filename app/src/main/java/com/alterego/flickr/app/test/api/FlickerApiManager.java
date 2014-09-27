package com.alterego.flickr.app.test.api;

import com.alterego.flickr.app.test.R;
import com.alterego.flickr.app.test.SettingsManager;
import com.alterego.flickr.app.test.data.FlickerResponse;

import java.util.HashMap;

import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.converter.GsonConverter;
import rx.Observable;


public class FlickerApiManager {

    private final IFlickerApi mFlickerService;
    private final SettingsManager mSettingsManager;

    public FlickerApiManager(SettingsManager mgr) {

        mSettingsManager = mgr;

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setConverter(new GsonConverter(mSettingsManager.getGson()))
                .setEndpoint(mSettingsManager.getParentApplication().getResources().getString(R.string.flicker_server))
                .setErrorHandler(new FlickerApiErrorHandler(mSettingsManager.getLogger()))
                .setLogLevel(RestAdapter.LogLevel.FULL).setLog(new AndroidLog("FlickerApiManager"))
                .build();

        mFlickerService = restAdapter.create(IFlickerApi.class);

    }

    public Observable<FlickerResponse> doPhotoSearchForTags (String tags) {
        String sanitized_tags;
        mSettingsManager.getLogger().debug("FlickerApiManager doPhotoSearchForTags looking for = " + tags);

        if (tags.contains(" ") && !tags.contains(",")) //using spaces as delimiters
            sanitized_tags = tags.replace(" ", ",");
        else
            sanitized_tags = tags;

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("api_key", mSettingsManager.getParentApplication().getResources().getString(R.string.flicker_apikey));
        params.put("format", "json");
        params.put("nojsoncallback", "1");
        params.put("tags", sanitized_tags);
        return mFlickerService.getPhotoSearchResults(params);

    }

}
