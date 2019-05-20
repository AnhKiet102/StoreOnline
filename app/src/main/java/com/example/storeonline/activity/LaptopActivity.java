package com.example.storeonline.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.storeonline.R;
import com.example.storeonline.adapter.LaptopAdapter;
import com.example.storeonline.model.Sanpham;
import com.example.storeonline.util.CheckConnection;
import com.example.storeonline.util.server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LaptopActivity extends AppCompatActivity {
    Toolbar toolbarlaptop;
    ListView listViewlaptop;
    int idlaptop=0;
  LaptopAdapter laptopAdapter;
    ArrayList<Sanpham> manglaptop;
    int page=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laptop);

        if(CheckConnection.haveNetworkConnection(getApplicationContext()))
        {
            Anhxa();
            GetIDloaisp();
            Acctiontoolbar();
            GetData(page);

        }
        else {
            CheckConnection.ShowToast_short(getApplicationContext(),"Bạn hãy kiểm tra lại Internet");
            finish();
        }

    }

    private void Anhxa() {
         toolbarlaptop=(Toolbar)findViewById(R.id.toolbarlaptop);
         listViewlaptop=(ListView)findViewById(R.id.listviewlaptop);
         manglaptop= new ArrayList<>();
         laptopAdapter = new LaptopAdapter(getApplicationContext(),manglaptop);
         listViewlaptop.setAdapter(laptopAdapter);

    }
    private void GetIDloaisp() {
        idlaptop = getIntent().getIntExtra("idloaisanpham", -1);
    }

    private void Acctiontoolbar(){
        setSupportActionBar(toolbarlaptop);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarlaptop.setNavigationOnClickListener(  new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void GetData(int Page) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String duongdan= server.Duongdandienthoai+String.valueOf(Page);
        String url = "http://192.168.1.14/server/getsanpham.php?page=" + Page;
        StringRequest stringRequest= new StringRequest(Request.Method.POST,duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                int id=0;
                String Tenlaptop="";
                int Gialaptop=0;
                String HinhAnhlaptop="";
                String Motalaptop="";
                int Idsplaptop=0;
                if( response !=null)
                {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for(int i=0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject= jsonArray.getJSONObject(i);
                            id=jsonObject.getInt("id");
                            Tenlaptop=jsonObject.getString("tensp");
                            Gialaptop=jsonObject.getInt("giasp");
                            Motalaptop=jsonObject.getString("motasp");
                            Idsplaptop=jsonObject.getInt("idsanpham");
                            HinhAnhlaptop=jsonObject.getString("hinhanhsp");
                            manglaptop.add(new Sanpham(id,Tenlaptop,Gialaptop,HinhAnhlaptop,Motalaptop,Idsplaptop ));
                            laptopAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {


                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<String, String>();
                param.put("idsanpham",String.valueOf(idlaptop));
                return param;

            }
        };
        requestQueue.add(stringRequest);

    }

}
