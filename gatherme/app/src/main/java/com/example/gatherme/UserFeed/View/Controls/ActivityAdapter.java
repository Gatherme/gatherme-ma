package com.example.gatherme.UserFeed.View.Controls;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gatherme.R;
import com.example.gatherme.UserFeed.Repository.Model.ActivityFModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ActivityAdapter extends BaseAdapter {
    private static LayoutInflater inflater;
    Context context;
    ArrayList<ActivityFModel> data;

    public ActivityAdapter(Context context, ArrayList<ActivityFModel> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ActivityFModel aux = data.get(position);
        final View view = inflater.inflate(R.layout.item_list,null);
        TextView title = view.findViewById(R.id.list_activity_title);
        TextView admin = view.findViewById(R.id.list_activity_admin);
        TextView date = view.findViewById(R.id.list_activity_date);
        TextView hour = view.findViewById(R.id.list_activity_hour);
        ImageView banner = view.findViewById(R.id.list_activity_img);

        title.setText(aux.getName());
        admin.setText(aux.getAdmin());
        date.setText(aux.getDate());
        hour.setText(aux.getHour());
        Picasso.get()
                .load(aux.getBanner())
                .placeholder(R.drawable.notfound)
                .resize(200,179)
                .into(banner);
        banner.setTag(position);
        return view;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


}
