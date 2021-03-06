package com.alterego.flickrtest;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.alterego.flickrtest.FlickrSearchDaoEntry;

import com.alterego.flickrtest.FlickrSearchDaoEntryDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig flickrSearchDaoEntryDaoConfig;

    private final FlickrSearchDaoEntryDao flickrSearchDaoEntryDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        flickrSearchDaoEntryDaoConfig = daoConfigMap.get(FlickrSearchDaoEntryDao.class).clone();
        flickrSearchDaoEntryDaoConfig.initIdentityScope(type);

        flickrSearchDaoEntryDao = new FlickrSearchDaoEntryDao(flickrSearchDaoEntryDaoConfig, this);

        registerDao(FlickrSearchDaoEntry.class, flickrSearchDaoEntryDao);
    }
    
    public void clear() {
        flickrSearchDaoEntryDaoConfig.getIdentityScope().clear();
    }

    public FlickrSearchDaoEntryDao getFlickrSearchDaoEntryDao() {
        return flickrSearchDaoEntryDao;
    }

}
