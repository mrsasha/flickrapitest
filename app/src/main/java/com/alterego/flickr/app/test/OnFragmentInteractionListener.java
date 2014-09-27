package com.alterego.flickr.app.test;

import android.support.v4.app.Fragment;

public interface OnFragmentInteractionListener {
    public void setActionBarTitle (String title);
    public void onRequestOpenFragment(Fragment fragment, String title);
}
