package com.example.storeonline.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.storeonline.R;
import com.example.storeonline.model.Sanpham;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SanphamAdapter extends RecyclerView.Adapter<SanphamAdapter.ItemHolder> {
    Context context;
    ArrayList<Sanpham> arraysanpham;

    public SanphamAdapter(Context context, ArrayList<Sanpham> arraysanpham) {
        this.context = context;
        this.arraysanpham = arraysanpham;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dong_sanphammoinhat,null);
        ItemHolder itemHolder = new ItemHolder(v);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder itemHolder, int i) {
        Sanpham sanpham = arraysanpham.get(i);
        itemHolder.txtTensanpham.setText(sanpham.getTensanpham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        itemHolder.txtGiasanpham.setText("Giá:"+decimalFormat.format(sanpham.getGiasanpham())+ "Đ");
        Picasso.with(context).load(sanpham.getHinhanhsanpham())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.error)
                .into(itemHolder.imgHinhsanpham);

    }

    @Override
    public int getItemCount() {
        return arraysanpham.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        public ImageView imgHinhsanpham;
        public TextView txtTensanpham;
        public  TextView txtGiasanpham;

        public ItemHolder(@NonNull View itemView) {

            super(itemView);
            imgHinhsanpham=(ImageView) itemView.findViewById(R.id.imageviewsanpham);
            txtGiasanpham=(TextView)itemView.findViewById(R.id.textviewgiacuasanpham);
            txtTensanpham=(TextView) itemView.findViewById((R.id.textviewtensanpham));


        }
    }
}
