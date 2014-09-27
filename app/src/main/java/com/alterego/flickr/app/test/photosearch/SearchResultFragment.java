package com.alterego.flickr.app.test.photosearch;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.alterego.flickr.app.test.MainApplication;
import com.alterego.flickr.app.test.OnFragmentInteractionListener;
import com.alterego.flickr.app.test.R;
import com.alterego.flickr.app.test.SettingsManager;
import com.alterego.flickr.app.test.data.FlickerResponse;
import com.alterego.flickr.app.test.data.Photo;

import java.util.ArrayList;
import java.util.List;

public class SearchResultFragment extends Fragment {

    private static final String SEARCH_RESULT = "search_result";

    private SettingsManager mSettingsManager;
    private OnFragmentInteractionListener mListener;
    private List<Photo> mPhotoItems = new ArrayList<Photo>();

    private AbsListView mListView;
    private PhotoItemListAdapter mAdapter;
    private android.view.ActionMode mActionMode;


    public static SearchResultFragment newInstance(String search_result) {
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle args = new Bundle();
        args.putString(SEARCH_RESULT, search_result);
        fragment.setArguments(args);
        return fragment;
    }

    public SearchResultFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSettingsManager = MainApplication.getMainApplication().getSettingsManager();
        mSettingsManager.getLogger().info("SearchResultFragment onCreate");

        if (getArguments() != null) {
            String searchResultJSON = getArguments().getString(SEARCH_RESULT);
            FlickerResponse searchResultObject = mSettingsManager.getGson().fromJson(searchResultJSON, FlickerResponse.class);
            mPhotoItems = searchResultObject.getPhotos().getPhoto();
        }

        mAdapter = new PhotoItemListAdapter(mSettingsManager.getParentActivity(), R.layout.fragment_searchresult_listitem, mPhotoItems);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photoitem, container, false);

        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        mListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        mListView.setMultiChoiceModeListener(mListViewMultiChoiceModeListener);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private AbsListView.MultiChoiceModeListener mListViewMultiChoiceModeListener = new AbsListView.MultiChoiceModeListener() {

        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position,
        long id, boolean checked) {
            // Here you can do something when items are selected/de-selected,
            // such as update the title in the CAB
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            // Respond to clicks on the actions in the CAB
            switch (item.getItemId()) {
                case R.id.action_save:
                    //TODO saveSelectedItems();
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate the menu for the CAB
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.save, menu);
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            // Here you can make any necessary updates to the activity when
            // the CAB is removed. By default, selected items are deselected/unchecked.
            mActionMode = null;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            // Here you can perform updates to the CAB due to
            // an invalidate() request
            return false;
        }
    };

}
