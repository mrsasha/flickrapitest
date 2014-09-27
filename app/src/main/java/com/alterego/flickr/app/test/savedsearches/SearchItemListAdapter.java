package com.alterego.flickr.app.test.savedsearches;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alterego.flickr.app.test.R;
import com.alterego.flickr.app.test.data.Photo;
import com.alterego.flickrtest.FlickrSearchDaoEntry;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class SearchItemListAdapter extends ArrayAdapter<FlickrSearchDaoEntry> {

    private final List<FlickrSearchDaoEntry> mSearchItems;
    private final Activity mContext;

    public SearchItemListAdapter(Activity context, int resource, List<FlickrSearchDaoEntry> items) {
        super(context, resource);
        mContext = context;
        mSearchItems = items;
    }

    static class ViewHolder {
        protected TextView tags;
    }

    @Override
    public int getCount() {
        return mSearchItems != null ? mSearchItems.size() : 0;
    }

    @Override
    public boolean isEmpty() {
        return (mSearchItems == null || mSearchItems.size() == 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            view = inflater.inflate(R.layout.fragment_search_listitem, null);

            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.tags = (TextView) view.findViewById(R.id.ItemTags);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.tags.setText(mSearchItems.get(position).getFlickr_search_string());

        return view;
    }

    @Override
    public FlickrSearchDaoEntry getItem(int position) {
        return mSearchItems.get(position);
    }
}
