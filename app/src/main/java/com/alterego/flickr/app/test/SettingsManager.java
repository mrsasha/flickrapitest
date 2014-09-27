package com.alterego.flickr.app.test;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.alterego.advancedandroidlogger.implementations.NullAndroidLogger;
import com.alterego.advancedandroidlogger.interfaces.IAndroidLogger;
import com.alterego.flickr.app.test.api.FlickerApiManager;
import com.alterego.flickrtest.DaoMaster;
import com.alterego.flickrtest.DaoSession;
import com.alterego.flickrtest.FlickrSearchDaoEntry;
import com.alterego.flickrtest.FlickrSearchDaoEntryDao;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import rx.Observable;
import rx.Subscriber;

@Accessors(prefix = "m")
public class SettingsManager {

    private final ImageLoaderConfiguration mImageLoaderConfiguration;
    @Getter
    private final FlickerApiManager mLifesumApiManager;

    @Getter
    Activity mParentActivity;
    @Getter
    @Setter
    Application mParentApplication;
    @Getter
    @Setter
    IAndroidLogger mLogger = NullAndroidLogger.instance;
    @Getter
    private Gson mGson = new GsonBuilder().create();
    @Getter
    private FlickrSearchDaoEntryDao mFlickrSearchDaoEntryDao;

    public SettingsManager(Application app, IAndroidLogger logger, ImageLoaderConfiguration imageLoaderConfig) {
        setLogger(logger);
        setParentApplication(app);
        mImageLoaderConfiguration = imageLoaderConfig;
        setupDatabase(app);
        mLifesumApiManager = new FlickerApiManager(this);
        ImageLoader.getInstance().init(mImageLoaderConfiguration);
    }

    public void setParentActivity(Activity act) {
        mParentActivity = act;
    }

    private void setupDatabase(Context ctx) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(ctx, "flickrtest-db", null);
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(database);
        DaoSession daoSession = daoMaster.newSession();
        mFlickrSearchDaoEntryDao = daoSession.getFlickrSearchDaoEntryDao();

    }

    /**
     * ****************** DAO MANAGEMENT ******************************
     */

    public Observable<List<FlickrSearchDaoEntry>> loadSearches() {
        return Observable.create(new Observable.OnSubscribe<List<FlickrSearchDaoEntry>>() {
            @Override
            public void call(Subscriber<? super List<FlickrSearchDaoEntry>> subscriber) {
                try {
                    List<FlickrSearchDaoEntry> items = mFlickrSearchDaoEntryDao.loadAll();
                    mLogger.info("SettingsManager loadSearches size = " + items.size());
                    subscriber.onNext(items);
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    public Observable<Boolean> storeSearch(final FlickrSearchDaoEntry search) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    long row = mFlickrSearchDaoEntryDao.insert(search);
                    mLogger.info("SettingsManager storeSearch successful, row = " + row);
                    subscriber.onNext(true);
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    public Observable<Boolean> deleteSearches(final List<FlickrSearchDaoEntry> items) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    for (FlickrSearchDaoEntry item : items) {
                        mFlickrSearchDaoEntryDao.delete(item);
                    }
                    mLogger.info("SettingsManager deleteSearches successful, removed items size = " + items.size());
                    subscriber.onNext(true);
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

}
