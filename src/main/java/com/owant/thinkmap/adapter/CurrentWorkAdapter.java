package com.owant.thinkmap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.owant.thinkmap.R;
import com.owant.thinkmap.model.CurrentFileModel;

import java.util.ArrayList;

/**
 * Created by owant on 27/02/2017.
 */
public class CurrentWorkAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<CurrentFileModel> mLists;

    public CurrentWorkAdapter(Context mContext, ArrayList<CurrentFileModel> mLists) {
        this.mContext = mContext;
        this.mLists = mLists;
    }

    public void setLists(ArrayList<CurrentFileModel> lists) {
        mLists = lists;
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CurrentFileViewHold viewHold;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_current_file, null);
            viewHold = new CurrentFileViewHold();
            viewHold.filePath = (TextView) convertView.findViewById(R.id.owant_file_path);
            viewHold.rootValue = (TextView) convertView.findViewById(R.id.owant_file_root_value);
            convertView.setTag(viewHold);
        } else {
            viewHold = (CurrentFileViewHold) convertView.getTag();
        }

        CurrentFileModel currentFileModel = mLists.get(position);
        viewHold.rootValue.setText(currentFileModel.mapRoot);
        viewHold.filePath.setText(currentFileModel.filePath);

        return convertView;
    }

    static class CurrentFileViewHold {
        public TextView rootValue;
        public TextView filePath;
    }

}
