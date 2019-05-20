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

public class DienThoaiAdapter extends BaseAdapter
{
    Context context;
    ArrayList<Sanpham> araydienthoai;

    public DienThoaiAdapter(Context context, ArrayList<Sanpham> araydienthoai) {
        this.context = context;
        this.araydienthoai = araydienthoai;
    }

    @Override
    public int getCount() {
        return araydienthoai.size();
    }

    @Override
    public Object getItem(int position) {
        return araydienthoai.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public  class  Viewholder{
        public TextView txttendienthoai,txtgiadienthoai,txtmotadienthoai;
        public ImageView imgdienthoai;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Viewholder viewholder = null;
        if( convertView== null)
        {
            viewholder = new Viewholder();
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView= inflater.inflate(R.layout.dong_dienthoai,null);
            viewholder.txttendienthoai=(TextView)convertView.findViewById(R.id.textviewtendienthoai);
            viewholder.txtgiadienthoai=(TextView)convertView.findViewById(R.id.textviewgiadienthoai);
            viewholder.txtmotadienthoai=(TextView)convertView.findViewById(R.id.textviewmotadienthoai);
            viewholder.imgdienthoai=(ImageView)convertView.findViewById(R.id.imageviewdienthoai);
            convertView.setTag(viewholder);
        }
        else
        {
            viewholder= (Viewholder) convertView.getTag();
        }
        Sanpham sanpham= (Sanpham) getItem(position);
        viewholder.txttendienthoai.setText(sanpham.getTensanpham());
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        viewholder.txtgiadienthoai.setText("Giá :"+decimalFormat.format(sanpham.getGiasanpham()+"Đ"));
        viewholder.txtmotadienthoai.setMaxLines(2);
        viewholder.txtmotadienthoai.setEllipsize(TextUtils.TruncateAt.END);
        viewholder.txtmotadienthoai.setText(sanpham.getMotasanpham());
        Picasso.with(context).load(sanpham.getHinhanhsanpham())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.error)
                .into(viewholder.imgdienthoai);

        return convertView;
    }
}
