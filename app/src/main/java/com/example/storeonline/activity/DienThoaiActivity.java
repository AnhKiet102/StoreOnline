package com.example.storeonline.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.storeonline.R;
import com.example.storeonline.adapter.DienThoaiAdapter;
import com.example.storeonline.model.Sanpham;
import com.example.storeonline.util.CheckConnection;
import com.example.storeonline.util.server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//import android.widget.Toolbar;


public class  DienThoaiActivity extends AppCompatActivity {
   Toolbar toolbardt;
    ListView listViewdt;
   DienThoaiAdapter dienThoaiAdapter;
    ArrayList<Sanpham>mangdt;
    int iddt=0;
    int page=1;
    View footerview;
    boolean isLoading=false;
    mHandler mHandler;
    boolean limitdata=false;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dien_thoai);
        anhxa();
        if(CheckConnection.haveNetworkConnection(getApplicationContext()))
        {

            GetIDloaisp();
            Acctiontoolbar();
            GetData(page);
            LoadMoreData();
        }
        else {
            CheckConnection.ShowToast_short(getApplicationContext(),"Bạn hãy kiểm tra kết nối Internet!");
            finish();
        }

    }

    private void LoadMoreData() {
        listViewdt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),Chitietsanpham.class);
                intent.putExtra("thongtinsanpham",mangdt.get(position));
                startActivity(intent);
            }
        });
        listViewdt.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if((firstVisibleItem+visibleItemCount==totalItemCount) && totalItemCount !=0&& isLoading==false&& limitdata==false)
                {
                    isLoading=true;
                    ThreadData threadData= new ThreadData();
                    threadData.start();

                }
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
                String Tendt="";
                int Giadt=0;
                String HinhAnhdt="";
                String Motadt="";
                int Idspdt=0;
                if( response !=null&& response.length()!=2)

                {
                    listViewdt.removeFooterView(footerview);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for(int i=0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject= jsonArray.getJSONObject(i);
                            id=jsonObject.getInt("id");
                            Tendt=jsonObject.getString("tensanpham");
                            Giadt=jsonObject.getInt("giasanpham");
                            HinhAnhdt=jsonObject.getString("hinhanhsanpham");
                            Motadt=jsonObject.getString("motasanpham");
                            Idspdt=jsonObject.getInt("idsanpham");

                            mangdt.add(new Sanpham(id,Tendt,Giadt,HinhAnhdt,Motadt,Idspdt));
                            dienThoaiAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {


                    }
                }
                else
                {
                    limitdata=true;
                    listViewdt.removeFooterView(footerview);
                    CheckConnection.ShowToast_short(getApplicationContext(),"Đã hết dữ liệu!");
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
                param.put("idsanpham",String.valueOf(iddt));
                return param;

            }
        };
        requestQueue.add(stringRequest);

    }

    private void Acctiontoolbar(){
        setSupportActionBar(toolbardt);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbardt.setNavigationOnClickListener(  new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

  

    private void GetIDloaisp() {
        iddt=getIntent().getIntExtra("idloaisanpham",-1);
        Log.d("giatriloaisanpham",iddt+"");
    }


    private void anhxa() {
        toolbardt = (Toolbar)findViewById(R.id.toolbardienthoai);
        listViewdt=(ListView)findViewById(R.id.listviewdienthoai);
        mangdt= new ArrayList<>();
        dienThoaiAdapter= new DienThoaiAdapter(getApplicationContext(),mangdt);
        listViewdt.setAdapter(dienThoaiAdapter);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerview=inflater.inflate(R.layout.progressbar,null);
        mHandler= new mHandler();
    }
    public  class  mHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case 0:
                    listViewdt.addFooterView(footerview);
                    break;
                case 1:
                    GetData(++page);
                    isLoading=false;
                    break;
            }
            super.handleMessage(msg);
        }
    }
    public class ThreadData extends  Thread{
        @Override
        public void run() {
            mHandler.sendEmptyMessage(0);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
            Message message= mHandler.obtainMessage(1);
            mHandler.sendMessage(message);
            super.run();
        }
    }
}
