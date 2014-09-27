package com.alterego.flickr.daogenerator;

import java.io.IOException;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class FlickrDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "com.alterego.flickrtest");
        schema.enableKeepSectionsByDefault();

        addFlickrSearchJsonResponse(schema);

        try {
            new DaoGenerator().generateAll(schema, "..\\..\\app\\src-gen");
        } catch (IOException io) {
            new DaoGenerator().generateAll(schema, "./app/src-gen");
        }
    }

    private static void addFlickrSearchJsonResponse(Schema schema) {
        Entity flickr_search_response = schema.addEntity("FlickrSearchDaoEntry");
        flickr_search_response.addIdProperty().primaryKey().autoincrement();
        flickr_search_response.addStringProperty("flickr_response_string");
        flickr_search_response.addStringProperty("flickr_search_string");
    }

}
