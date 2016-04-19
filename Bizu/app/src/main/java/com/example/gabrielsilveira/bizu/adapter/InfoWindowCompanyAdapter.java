package com.example.gabrielsilveira.bizu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.gabrielsilveira.bizu.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by gabrielsilveira on 18/04/16.
 */
public class InfoWindowCompanyAdapter implements GoogleMap.InfoWindowAdapter {

    Context context;

    public InfoWindowCompanyAdapter(Context context){
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.info_window_company, null);

        TextView companyName = (TextView) v.findViewById(R.id.infoWindowCompanyName);


        return v;
    }
}
