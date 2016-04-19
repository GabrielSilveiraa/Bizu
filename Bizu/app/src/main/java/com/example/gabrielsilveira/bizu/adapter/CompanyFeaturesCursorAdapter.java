package com.example.gabrielsilveira.bizu.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.gabrielsilveira.bizu.FeatureImageView;
import com.example.gabrielsilveira.bizu.R;

import java.util.ArrayList;

/**
 * Created by gabrielsilveira on 11/04/16.
 */
public class CompanyFeaturesCursorAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> features;

    public CompanyFeaturesCursorAdapter(Context context/*, ArrayList<String> features*/) {
        this.context = context;
       // this.features = features;
    }

    public int getCount() {
      //  return features.size();
        return 3;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new FeatureImageView(context, "");
            imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageView.setPadding(8, 8, 8, 8);

        } else {
            imageView = (ImageView) convertView;
        }

        //imageView.setImageResource(features.get(position).getIconId());
        imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),
                R.drawable.wifi));
        //imageView.setImageResource(R.drawable.wifi);
        imageView.setAdjustViewBounds(true);
        return imageView;
    }
}
