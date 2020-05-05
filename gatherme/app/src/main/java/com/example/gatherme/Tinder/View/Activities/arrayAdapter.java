package com.example.gatherme.Tinder.View.Activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.gatherme.R;

import java.util.List;

public class arrayAdapter extends ArrayAdapter<Card> {
    Context context;

    public arrayAdapter(Context context, int resouceId, List<Card> items) {
        super(context, resouceId, items);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        Card card_item = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView nombre = (TextView) convertView.findViewById(R.id.tinder_nombre);
        TextView ubicacion = (TextView) convertView.findViewById(R.id.tinder_ubicacion);
        TextView biografia = (TextView) convertView.findViewById(R.id.tinder_biografia);

        nombre.setText(card_item.getNombre());
        ubicacion.setText(card_item.getUbicacion());
        biografia.setText(card_item.getBiografia());

        return convertView;

    }


}
