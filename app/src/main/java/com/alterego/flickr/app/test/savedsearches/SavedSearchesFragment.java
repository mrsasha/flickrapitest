package com.alterego.flickr.app.test.savedsearches;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
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
import android.widget.Toast;

import com.alterego.flickr.app.test.MainApplication;
import com.alterego.flickr.app.test.OnFragmentInteractionListener;
import com.alterego.flickr.app.test.R;
import com.alterego.flickr.app.test.SettingsManager;
import com.alterego.flickr.app.test.photosearch.SearchResultFragment;
import com.alterego.flickrtest.FlickrSearchDaoEntry;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SavedSearchesFragment extends Fragment {

    private static final String FRAGMENT_TITLE = "Saved searches";

    private SettingsManager mSettingsManager;
    private OnFragmentInteractionListener mListener;
    private List<FlickrSearchDaoEntry> mSavedSearchItems = new ArrayList<FlickrSearchDaoEntry>();

    private AbsListView mListView;
    private SearchItemListAdapter mAdapter;
    private ActionMode mActionMode;


    public static SavedSearchesFragment newInstance() {
        SavedSearchesFragment fragment = new SavedSearchesFragment();
        return fragment;
    }

    public SavedSearchesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSettingsManager = MainApplication.getMainApplication().getSettingsManager();
        mSettingsManager.getLogger().info("SavedSearchesFragment onCreate");

        mSavedSearchItems = mSettingsManager.getFlickrSearchDaoEntryDao().loadAll();
        mSettingsManager.getLogger().info("SavedSearchesFragment onCreate mSavedSearchItems size = " + mSavedSearchItems.size());
        mAdapter = new SearchItemListAdapter(mSettingsManager.getParentActivity(), R.layout.fragment_search_listitem, mSavedSearchItems);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_savedsearches_list, container, false);

        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        mListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        mListView.setMultiChoiceModeListener(mListViewMultiChoiceModeListener);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FlickrSearchDaoEntry search = mAdapter.getItem(position);
                if (mListener != null) {
                    Fragment fragment_to_open = SearchResultFragment.newInstance(search.getFlickr_response_string());
                    mListener.onRequestOpenFragment(fragment_to_open, "Results: " + search.getFlickr_search_string());
                }
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
                case R.id.action_delete:
                    deleteSelectedItems();
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
            inflater.inflate(R.menu.delete, menu);
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

    private void deleteSelectedItems() {

        mSettingsManager.getLogger().info("SavedSearchesFragment deleteSelectedItems, adapter size = " + mAdapter.getCount());
        SparseBooleanArray checkedArray = mListView.getCheckedItemPositions();
        ArrayList<FlickrSearchDaoEntry> selectedItems = new ArrayList<FlickrSearchDaoEntry>();
        for (int i = 0; i < checkedArray.size(); i++) {
            // Item position in adapter
            int position = checkedArray.keyAt(i);
            // Add sport if it is checked i.e.) == TRUE!
            if (checkedArray.valueAt(i))
                selectedItems.add(mAdapter.getItem(position));
        }

        mSettingsManager.deleteSearches(selectedItems).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Observer<Boolean>() {
            @Override
            public void onCompleted() {
                //do nothing
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(mSettingsManager.getParentActivity(), "Problem deleting items!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Boolean aBoolean) {
                Toast.makeText(mSettingsManager.getParentActivity(), "Deleting items successful!", Toast.LENGTH_SHORT).show();
                refreshAdapter();
            }
        });
        mSettingsManager.getLogger().info("SavedSearchesFragment deleteSelectedItems size = " + selectedItems.size());

    }

    private void refreshAdapter() {
        mSavedSearchItems = mSettingsManager.getFlickrSearchDaoEntryDao().loadAll();
        mAdapter = new SearchItemListAdapter(mSettingsManager.getParentActivity(), R.layout.fragment_search_listitem, mSavedSearchItems);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
