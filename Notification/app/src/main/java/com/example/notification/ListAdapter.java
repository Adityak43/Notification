package com.example.notification;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {

    private Context context; //context
    private ArrayList<String> items; //data source of the list adapter

    //public constructor
    public ListAdapter(Context context, ArrayList<String> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return items.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.layout_list_view_row_items, parent, false);
        }

        // get current item to be displayed
       final  String currentItem = (String) getItem(position);

        // get the TextView for item name and item description
       final TextView textViewItemName = (TextView)
                convertView.findViewById(R.id.log);

        textViewItemName.setText(currentItem);

        //sets the text for item name and item description from the current item object



        // returns the view for the current row
        return convertView;
    }
}
