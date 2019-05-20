package com.example.storeonline.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.storeonline.R;
import com.example.storeonline.model.Sanpham;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class LaptopAdapter extends BaseAdapter {
    Context context;
    ArrayList<Sanpham> araylaptop;

    public LaptopAdapter(Context context, ArrayList<Sanpham> araylaptop) {
        this.context = context;
        this.araylaptop = araylaptop;
    }

    @Override
    public int getCount() {
        return araylaptop.size();
    }

    @Override
    public Object getItem(int position) {
        return araylaptop.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public  class  Viewholder{
        public TextView txttenlaptop,txtgialaptop,txtmotalaptop;
        public ImageView imglaptap;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Viewholder viewholder=null;
        if( convertView== null)
        {
            viewholder = new Viewholder();
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView= inflater.inflate(R.layout.dong_laptop,null);
            viewholder.txttenlaptop=(TextView)convertView.findViewById(R.id.textviewlaptop);
            viewholder.txtgialaptop=(TextView)convertView.findViewById(R.id.textviewgialaptop);
            viewholder.txtmotalaptop=(TextView)convertView.findViewById(R.id.textviewmotalaptop);
            viewholder.imglaptap=(ImageView)convertView.findViewById(R.id.imageviewlaptop);
            convertView.setTag(viewholder);
        }
        else
        {
            viewholder= (LaptopAdapter.Viewholder) convertView.getTag();
        }
        Sanpham sanpham= (Sanpham) getItem(position);
        viewholder.txttenlaptop.setText(sanpham.getTensanpham());
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        viewholder.txtgialaptop.setText("Giá :"+decimalFormat.format(sanpham.getGiasanpham()+"Đ"));
        viewholder.txtmotalaptop.setMaxLines(2);
        viewholder.txtmotalaptop.setEllipsize(TextUtils.TruncateAt.END);
        viewholder.txtmotalaptop.setText(sanpham.getMotasanpham());
        Picasso.with(context).load(sanpham.getHinhanhsanpham())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.error)
                .into(viewholder.imglaptap);

        return convertView;
    }
}
