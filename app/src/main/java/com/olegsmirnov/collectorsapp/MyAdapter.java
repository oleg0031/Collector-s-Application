package com.olegsmirnov.collectorsapp;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;

import io.realm.RealmResults;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private RealmResults<Collection> list;

    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView description;
        private ImageView photo;
        private TextView price;

        public ViewHolder(View itemView) {
            super(itemView);
            description = (TextView) itemView.findViewById(R.id.rv_item_description);
            photo = (ImageView) itemView.findViewById(R.id.rv_item_image);
            price = (TextView) itemView.findViewById(R.id.rv_item_price);
        }
    }

    public MyAdapter(RealmResults<Collection> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        holder.description.setText(list.get(position).getDescription());
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(0);
        String val = df.format(list.get(position).getPrice());
        holder.price.setText(val + "$");
        holder.photo.setImageBitmap(BitmapFactory.decodeFile(list.get(position).getPath()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
