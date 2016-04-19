package com.example.gabrielsilveira.bizu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gabrielsilveira.bizu.R;
import com.example.gabrielsilveira.bizu.Utilities;

/**
 * Created by gabrielsilveira on 12/04/16.
 */
public class DrawerLayoutBaseAdapter extends BaseAdapter {
    private final Context context;

    public DrawerLayoutBaseAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 7;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView listActionName;
        ImageView listActionIcon;
        switch (position){
            case 0:
                rowView = inflater.inflate(R.layout.row_static_drawer_layout, parent, false);
                break;
            case 1:
                rowView = inflater.inflate(R.layout.row_drawer_layout, parent, false);
                listActionName = (TextView) rowView.findViewById(R.id.listActionName);
                listActionIcon = (ImageView) rowView.findViewById(R.id.listActionIcon);
                listActionName.setText(R.string.profile);
                listActionIcon.setImageResource(R.drawable.perfil);
                //put the icon a little more for the left, because his widht is smaller than the others
                listActionIcon.setPadding(listActionIcon.getPaddingLeft(), listActionIcon.getPaddingTop(), listActionIcon.getPaddingRight()+7, listActionIcon.getPaddingBottom());
                break;
            case 2:
                rowView = inflater.inflate(R.layout.row_drawer_layout, parent, false);
                listActionName = (TextView) rowView.findViewById(R.id.listActionName);
                listActionIcon = (ImageView) rowView.findViewById(R.id.listActionIcon);
                listActionName.setText(R.string.tellFriend);
                listActionIcon.setImageResource(R.drawable.contar);
                break;
            case 3:
                rowView = inflater.inflate(R.layout.row_drawer_layout, parent, false);
                listActionName = (TextView) rowView.findViewById(R.id.listActionName);
                listActionIcon = (ImageView) rowView.findViewById(R.id.listActionIcon);
                listActionName.setText(R.string.reportProblem);
                listActionIcon.setImageResource(R.drawable.relatar);
                break;
            case 4:
                rowView = inflater.inflate(R.layout.row_drawer_layout, parent, false);
                listActionName = (TextView) rowView.findViewById(R.id.listActionName);
                listActionIcon = (ImageView) rowView.findViewById(R.id.listActionIcon);
                listActionName.setText(R.string.about);
                listActionIcon.setImageResource(R.drawable.sobre);
                break;
            case 5:
                rowView = inflater.inflate(R.layout.row_drawer_layout, parent, false);
                listActionName = (TextView) rowView.findViewById(R.id.listActionName);
                listActionIcon = (ImageView) rowView.findViewById(R.id.listActionIcon);
                listActionName.setText(R.string.terms);
                listActionIcon.setImageResource(R.drawable.termos);
                break;
            case 6:
                rowView = inflater.inflate(R.layout.row_drawer_layout, parent, false);
                listActionName = (TextView) rowView.findViewById(R.id.listActionName);
                listActionIcon = (ImageView) rowView.findViewById(R.id.listActionIcon);
                listActionName.setText(R.string.tutorial);
                listActionIcon.setImageResource(R.drawable.tutorial);
                //put the icon a little more for the left, because his widht is smaller than the others
                listActionIcon.setPadding(listActionIcon.getPaddingLeft(), listActionIcon.getPaddingTop(), listActionIcon.getPaddingRight()+7, listActionIcon.getPaddingBottom());
                break;
            }
        Utilities.setFontNexa(context, rowView);

        return rowView;
    }
}
