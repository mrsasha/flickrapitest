package com.alterego.flickr.app.test.photosearch;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alterego.flickr.app.test.MainApplication;
import com.alterego.flickr.app.test.OnFragmentInteractionListener;
import com.alterego.flickr.app.test.R;
import com.alterego.flickr.app.test.SettingsManager;
import com.alterego.flickr.app.test.data.FlickerResponse;
import com.alterego.flickrtest.FlickrSearchDaoEntry;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class SearchFragment extends Fragment {

    private static final String FRAGMENT_TITLE = "Search";
    private static final String LAST_SEARCH = "last_search";

    private String mLastSearch;
    private OnFragmentInteractionListener mListener;
    private Subscription mSearchSubscription;
    private SettingsManager mSettingsManager;
    private EditText mEditText;
    private Button mSearchButton;
    private ProgressBar mProgressBar;
    private TextView mNoResultsText;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mLastSearch = savedInstanceState.getString(LAST_SEARCH);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mSettingsManager = MainApplication.getMainApplication().getSettingsManager();
        mSettingsManager.getLogger().info("SearchFragment onCreateView");

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        mEditText = (EditText) view.findViewById(R.id.search_edit_text);
        mSearchButton = (Button) view.findViewById(R.id.search_button);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mNoResultsText = (TextView) view.findViewById(R.id.search_text_noresults);

        if (mLastSearch != null)
            mEditText.setText(mLastSearch);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                mNoResultsText.setVisibility(View.INVISIBLE);
                mSearchButton.setEnabled(false);
                if (mSearchSubscription != null && !mSearchSubscription.isUnsubscribed())
                    mSearchSubscription.unsubscribe();

                String searchtext = mEditText.getText().toString();
                mSettingsManager.getLogger().info("SearchFragment searching for = " + searchtext);
                mSearchSubscription = mSettingsManager.getLifesumApiManager()
                        .doPhotoSearchForTags(searchtext)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(taggedPhotoSearchObserver);
            }
        });


        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
            mListener.setActionBarTitle(FRAGMENT_TITLE);
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mListener != null) {
            mListener.setActionBarTitle(FRAGMENT_TITLE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        String searchtext = "";
        super.onSaveInstanceState(outState);
        if (mEditText!=null)
            searchtext = mEditText.getText().toString();

        if (searchtext != null && !searchtext.equals(""))
            outState.putString(LAST_SEARCH, searchtext);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSearchSubscription != null) {
            mSearchSubscription.unsubscribe();
        }
    }

    private Observer<FlickerResponse> taggedPhotoSearchObserver = new Observer<FlickerResponse>() {
        @Override
        public void onCompleted() {
            mSettingsManager.getLogger().info("SearchFragment taggedPhotoSearchObserver finished with photo search");
        }

        @Override
        public void onError(java.lang.Throwable throwable) {
            mSettingsManager.getLogger().error("SearchFragment taggedPhotoSearchObserver error receiving photo search results = " + throwable.toString());
        }

        @Override
        public void onNext(FlickerResponse flickerResponse) {
            mSettingsManager.getLogger().info("SearchFragment taggedPhotoSearchObserver photo search results = " + flickerResponse.toString());
            mProgressBar.setVisibility(View.GONE);
            mSearchButton.setEnabled(true);

            String json_string = mSettingsManager.getGson().toJson(flickerResponse);
            String searchtext = mEditText.getText().toString();

            mSettingsManager.storeSearch(new FlickrSearchDaoEntry(null, json_string, searchtext)).subscribe(new Action1<Boolean>() {
                @Override
                public void call(Boolean aBoolean) {
                    mSettingsManager.getLogger().info("SearchFragment taggedPhotoSearchObserver saved search = ");
                }
            });

            if (flickerResponse.getPhotos().getPhoto() != null && flickerResponse.getPhotos().getPhoto().size() > 0) {
                if (mListener != null) {
                    Fragment fragment_to_open = SearchResultFragment.newInstance(json_string);
                    mListener.onRequestOpenFragment(fragment_to_open, "Results: " + searchtext);
                }
            } else {
                mNoResultsText.setVisibility(View.VISIBLE);
            }
        }
    };


}
