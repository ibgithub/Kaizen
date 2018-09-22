package com.dev.kaizen.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.dev.kaizen.R;
import com.dev.kaizen.util.Constant;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QuoteListActivity extends Activity  {

    private ListView listview;
    private TextView title;
    private Button btn_prev;
    private Button btn_next;

    private ArrayList<String> data;
    ArrayAdapter<String> sd;
    List<HashMap<String, SimpleDraweeView>> aList = new ArrayList<HashMap<String, SimpleDraweeView>>();

    private int pageCount ;

    private int increment = 0;

    public int NUM_ITEMS_PAGE  = 10;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_list);

        listview = (ListView)findViewById(R.id.list_quotes);
        btn_prev     = (Button)findViewById(R.id.prev);
        btn_next     = (Button)findViewById(R.id.next);
        title    = (TextView)findViewById(R.id.title);

        btn_prev.setEnabled(false);

        data = new ArrayList<String>();

        final String url = Constant.BASE_URL + "quotes";

        RequestQueue queue = Volley.newRequestQueue(QuoteListActivity.this);

// prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        Log.d("Response", response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonobject = response.getJSONObject(i);

                                String urlPhoto = jsonobject.getString("urlPhoto");
                                data.add(urlPhoto);
                            }
                            int val = response.length()%NUM_ITEMS_PAGE;
                            val = val==0?0:1;
                            pageCount = response.length()/NUM_ITEMS_PAGE+val;

                            loadList(0);
                        } catch (JSONException ex) {
                            Log.d("ex=", ex.getMessage());
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        );

// add it to the RequestQueue
        queue.add(getRequest);



        btn_next.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                increment = increment + 1;
                loadList(increment);
                CheckEnable();
            }
        });

        btn_prev.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                increment = increment - 1;
                loadList(increment);
                CheckEnable();
            }
        });

    }

    /**
     * Method for enabling and disabling Buttons
     */
    private void CheckEnable()
    {
        if(increment+1 == pageCount)
        {
            btn_prev.setEnabled(true);
            btn_next.setEnabled(false);
        }
        else if(increment == 0)
        {
            btn_prev.setEnabled(false);
            if(increment+1 <= pageCount)
            {
                btn_next.setEnabled(true);
            }
        }
        else
        {
            btn_prev.setEnabled(true);
            btn_next.setEnabled(true);
        }
    }

    /**
     * Method for loading data in listview
     * @param number
     */
    private void loadList(int number)
    {
        ArrayList<String> sort = new ArrayList<String>();
        title.setText("Page "+(number+1)+" of "+pageCount);

        int start = number * NUM_ITEMS_PAGE;
        for(int i=start;i<(start)+NUM_ITEMS_PAGE;i++)
        {
            if(i<data.size())
            {
                sort.add(data.get(i));
            }
            else
            {
                break;
            }
        }

        MyCustomAdapter customAdapter = new MyCustomAdapter(getApplicationContext(), sort);
        ListView androidListView = findViewById(R.id.list_quotes);
        androidListView.setAdapter(customAdapter);

    }

    private class MyCustomAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        ArrayList<String> uris;

        public MyCustomAdapter(Context applicationContext, ArrayList<String> uris) {
            this.uris = uris;
            mInflater = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return uris.size();
        }

        @Override
        public String getItem(int position) {
            return uris.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public SimpleDraweeView getView(int position, View convertView, ViewGroup parent) {

            convertView = mInflater.inflate(R.layout.listview_image_layout, null);
            SimpleDraweeView icon = convertView.findViewById(R.id.listview_image);
            icon.setImageURI(uris.get(position));
            return icon;
        }

    }

}
