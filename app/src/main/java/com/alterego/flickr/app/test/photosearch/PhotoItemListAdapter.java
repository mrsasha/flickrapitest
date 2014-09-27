package com.alterego.flickr.app.test.photosearch;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alterego.flickr.app.test.R;
import com.alterego.flickr.app.test.data.Photo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class PhotoItemListAdapter extends ArrayAdapter<Photo> {

    private final List<Photo> mPhotoItems;
    private final Activity mContext;

    public PhotoItemListAdapter(Activity context, int resource, List<Photo> items) {
        super(context, resource);
        mContext = context;
        mPhotoItems = items;
    }

    static class ViewHolder {
        protected TextView title;
        protected ImageView photo;
    }

    @Override
    public int getCount() {
        return mPhotoItems != null ? mPhotoItems.size() : 0;
    }

    @Override
    public boolean isEmpty() {
        return (mPhotoItems == null || mPhotoItems.size() == 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            view = inflater.inflate(R.layout.fragment_searchresult_listitem, null);

            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.title = (TextView) view.findViewById(R.id.ItemTitle);
            viewHolder.photo = (ImageView) view.findViewById(R.id.ItemPhoto);

            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.title.setText(mPhotoItems.get(position).getTitle());
        ImageLoader.getInstance().displayImage(mPhotoItems.get(position).getUrlFromPhoto("b"), holder.photo);

        return view;
    }

    @Override
    public Photo getItem(int position) {
        return mPhotoItems.get(position);
    }
}
