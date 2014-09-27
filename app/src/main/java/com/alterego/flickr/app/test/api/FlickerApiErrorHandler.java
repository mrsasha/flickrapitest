package com.alterego.flickr.app.test.api;

import com.alterego.advancedandroidlogger.interfaces.IAndroidLogger;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;

class FlickerApiErrorHandler implements ErrorHandler {

    private final IAndroidLogger mLogger;

    FlickerApiErrorHandler(IAndroidLogger logger) {
        mLogger = logger;
    }

    @Override
    public Throwable handleError(RetrofitError cause) {
        try {
            return new FlickerApiException(cause);
        } catch (Exception e) {
            mLogger.error("FlickerApiErrorHandler cannot read body, error = " + cause);
        }
        return cause;
    }



    private class FlickerApiException extends Throwable {

        public FlickerApiException(RetrofitError cause) {
            try {
                mLogger.error("FlickerApiException error = " + cause.getMessage() + ", response = " + cause.getResponse().getBody().in().toString());
            } catch (Exception e) {
                mLogger.error("FlickerApiException error = " + cause.getMessage() + ", can't read the response!");
            }
        }
    }
}