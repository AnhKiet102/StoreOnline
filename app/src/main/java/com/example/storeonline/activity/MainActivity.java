package com.example.storeonline.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ViewFlipper;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.storeonline.R;
import com.example.storeonline.adapter.LoaispAdapter;
import com.example.storeonline.adapter.SanphamAdapter;
import com.example.storeonline.model.Loaisp;
import com.example.storeonline.model.Sanpham;
import com.example.storeonline.util.CheckConnection;
import com.example.storeonline.util.server;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewmanhinhchinh;
    NavigationView navigationView;
    ListView listViewmanhinhchinh;
    DrawerLayout drawerLayout;
    ArrayList<Loaisp> mangloaisp;
    LoaispAdapter loaispAdapter;
    int id = 0;
    String tenloaisanpham = "";
    String hinhanhloaisanpham = "";
    ArrayList<Sanpham> mangsanpham;
    SanphamAdapter sanphamAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Anhxa();
        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
            ActionBar();
            ActionViewFlipper();
            GetDuLieuLoaisp();
            GetDuLieuSanphamMoinhat();
            CatchOnItemListview();
        } else {
            CheckConnection.ShowToast_short(getApplicationContext(), "Bạn kiểm tra lại kết nối internet!");
            finish();
        }

    }

    private void CatchOnItemListview() {
        listViewmanhinhchinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position)
                {
                    case 0:
                        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            startActivity( intent);


                        } else {
                            CheckConnection.ShowToast_short(getApplicationContext(), "bạn hãy kiểm tra lại kết nối !");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case 1: if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
                        Intent intent = new Intent(MainActivity.this, DienThoaiActivity.class);
                        intent.putExtra("idloaisanpham",mangloaisp.get(position).getId());

                        startActivity( intent);



                    } else {
                        CheckConnection.ShowToast_short(getApplicationContext(), "bạn hãy kiểm tra lại kết nối !");
                    }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case 2: if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
                        Intent intent = new Intent(MainActivity.this, LaptopActivity.class);
                        intent.putExtra("idloaisanpham",mangloaisp.get(position).getId());

                        startActivity( intent);



                    } else {
                        CheckConnection.ShowToast_short(getApplicationContext(), "bạn hãy kiểm tra lại kết nối !");
                    }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case 3: if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
                        Intent intent = new Intent(MainActivity.this, LienHeActivity.class);
                        startActivity( intent);



                    } else {
                        CheckConnection.ShowToast_short(getApplicationContext(), "bạn hãy kiểm tra lại kết nối !");
                    }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 4: if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
                        Intent intent = new Intent(MainActivity.this, ThongTinActivity.class);
                        startActivity( intent);



                    } else {
                        CheckConnection.ShowToast_short(getApplicationContext(), "bạn hãy kiểm tra lại kết nối !");
                    }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;



                }
            }
        });
    }

    private void GetDuLieuSanphamMoinhat() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(server.Duongdansanphammoinhat, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    int ID = 0;
                    String tensanpham = "";
                    Integer Giasanpham = 0;
                    String Hinhanhsanpham = "";
                    String Motasanpham = "";
                    int IDsanpham = 0;
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            ID = jsonObject.getInt("id");
                            tensanpham = jsonObject.getString("tensanpham");
                            Giasanpham = jsonObject.getInt("giasanpham");
                            Hinhanhsanpham = jsonObject.getString("hinhanhsanpham");
                            Motasanpham = jsonObject.getString("motasanpham");
                            IDsanpham = jsonObject.getInt("idsanpham");
                            mangsanpham.add(new Sanpham(ID, tensanpham, Giasanpham, Hinhanhsanpham, Motasanpham, IDsanpham));
                            sanphamAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void GetDuLieuLoaisp() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(server.DuongdanLoaisp, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                if (response != null) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            tenloaisanpham = jsonObject.getString("tenloaisanpham");
                            hinhanhloaisanpham = jsonObject.getString("hinhanhloaisanpham");
                            mangloaisp.add(new Loaisp(id, tenloaisanpham, hinhanhloaisanpham));
                            loaispAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    mangloaisp.add(3, new Loaisp(0, "Liên hệ", "http://img.thuviendohoa.vn/items/vector-icon-bieu-tuong-dien-thoai-4367.jpg"));
                    mangloaisp.add(4, new Loaisp(0, "Thông tin", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR1u7m_-ADHW03Y1B0yoBno3EYYWATAhSwtlInr-N5EXdu2rSP3xw"));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CheckConnection.ShowToast_short(getApplicationContext(), error.toString());
            }
        });
        requestQueue.add(jsonArrayRequest);

    }


    private void ActionViewFlipper() {
        ArrayList<String> mangquangcao = new ArrayList<>();
        mangquangcao.add("https://cdn.tgdd.vn/Files/2019/05/03/1164598/zenfone-6_799x450.jpg");
        mangquangcao.add("http://i.dell.com/sites/csimages/Videos_Images/en/9eb776ec-d2b3-450c-b340-e1b5f6f31eeb.jpg");
        mangquangcao.add("https://edge.slashgear.com/wp-content/uploads/2018/11/apple-iphone-xr-11.jpg");
        mangquangcao.add("https://edge.slashgear.com/wp-content/uploads/2019/05/slashgear_galaxy_s10-1280x720.jpg");
        for (int i = 0; i < mangquangcao.size(); i++) {
            ImageView imageView = new ImageView(getApplicationContext());

            Picasso.with(getApplicationContext()).load(mangquangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(8000);
        viewFlipper.setAutoStart(true);
        Animation animation_slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation animation_slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(animation_slide_in);
        viewFlipper.setOutAnimation((animation_slide_out));
    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void Anhxa() {
        toolbar = (Toolbar) findViewById(R.id.toolbarmanhinhchinh);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);
        recyclerViewmanhinhchinh = (RecyclerView) findViewById(R.id.recyclerview);
        navigationView = (NavigationView) findViewById(R.id.navigationview);
        listViewmanhinhchinh = (ListView) findViewById(R.id.listviewmanhinhchinh);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        mangloaisp = new ArrayList<>();
        mangloaisp.add(0, new Loaisp(0, "Trang Chính", "https://maxcdn.icons8.com/app/uploads/2016/09/sweet-home-icon.jpg"));
        loaispAdapter = new LoaispAdapter(mangloaisp, getApplicationContext());
        listViewmanhinhchinh.setAdapter(loaispAdapter);
        mangsanpham = new ArrayList<>();
        sanphamAdapter = new SanphamAdapter(getApplicationContext(), mangsanpham);
        recyclerViewmanhinhchinh.setHasFixedSize(true);
        recyclerViewmanhinhchinh.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerViewmanhinhchinh.setAdapter(sanphamAdapter);
    }
}
