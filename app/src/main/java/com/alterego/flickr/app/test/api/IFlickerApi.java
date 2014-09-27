package com.alterego.flickr.app.test.api;


import com.alterego.flickr.app.test.data.FlickerResponse;

import java.util.Map;

import retrofit.http.GET;
import retrofit.http.QueryMap;
import rx.Observable;

/**
 * This is the API for Flicker.
 *
 */
public interface IFlickerApi {

    /**
     * This call executes the photo search search and retrieves the found photos
     *
     * @param params Map<String, String> with the search parameters
     *
     * @return {@link FlickerResponse} result as an {@link rx.Observable}
     */
    //https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=22d97ac079b7903e6ea07201f3a6c1e2&tags=school&format=json&nojsoncallback=1
    @GET("/?method=flickr.photos.search")
    Observable<FlickerResponse> getPhotoSearchResults(@QueryMap Map<String, String> params);

}
