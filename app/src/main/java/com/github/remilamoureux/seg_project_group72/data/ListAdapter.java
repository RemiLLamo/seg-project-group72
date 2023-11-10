package com.github.remilamoureux.seg_project_group72.data;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.remilamoureux.seg_project_group72.R;

import java.util.List;

public class ListAdapter<E extends Listable> extends ArrayAdapter<E> {
    private Activity context;
    private List<E> things;

    public ListAdapter(Activity context, List<E> things) {
        super(context, R.layout.layout_item_list, things);
        this.context = context;
        this.things = things;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_item_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewPrice = (TextView) listViewItem.findViewById(R.id.textViewPrice);

        Listable product = things.get(position);
        textViewName.setText(product.getItemName());
        textViewPrice.setText(String.valueOf(product.getItemDesc()));
        return listViewItem;
    }
}
