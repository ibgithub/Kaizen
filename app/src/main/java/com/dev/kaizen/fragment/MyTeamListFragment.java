/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dev.kaizen.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dev.kaizen.R;
import com.dev.kaizen.adapter.GroupTeam;
import com.dev.kaizen.adapter.Team;
import com.dev.kaizen.util.Constant;
import com.dev.kaizen.util.FontUtils;
import com.dev.kaizen.util.GlobalVar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyTeamListFragment extends Fragment implements View.OnClickListener{
    private Context context;
    private RequestQueue queue;

    private RecyclerView recyclerView;
    private GroupTeamAdapter mAdapter;
    private List<GroupTeam> groupList = new ArrayList<>();
    private boolean isNoTeam;

    private Button createTeamBtn;

    private Long userId;
    private Gson gson;

    public static MyTeamListFragment newInstance() {
        MyTeamListFragment fragment = new MyTeamListFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_team_list, container, false);

        TextView headertext = (TextView) getActivity().findViewById(R.id.headertext);
        headertext.setText("Daftar Tim di Sekolah");
        headertext.setTypeface(FontUtils.loadFontFromAssets(context, Constant.FONT_SEMIBOLD));

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        Button backBtn = (Button) getActivity().findViewById(R.id.backBtn);
        backBtn.setVisibility(Button.VISIBLE);
        backBtn.setOnClickListener(this);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(Toolbar.VISIBLE);

        TextView schoolText = (TextView) v.findViewById(R.id.schoolText);

        recyclerView = (RecyclerView) v.findViewById(R.id.rvList);

        mAdapter = new GroupTeamAdapter(groupList, context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        createTeamBtn = (Button) v.findViewById(R.id.createTeamBtn);
        createTeamBtn.setOnClickListener(this);

        queue = Volley.newRequestQueue(getActivity());

        if(GlobalVar.getInstance().getProfile() == null) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Pesan")
                    .setMessage("Anda belum mengisi data Profile diri Anda.")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Toast.makeText(getActivity(), "Yes", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        } else {
            try {
                JSONObject profile = new JSONObject(GlobalVar.getInstance().getProfile());
                JSONObject school = profile.getJSONObject("school");
                schoolText.setText(school.getString("schoolName"));

                JSONObject account = new JSONObject(GlobalVar.getInstance().getAccount());
                Log.d("userId", "" + account.getLong("id"));
                userId = account.getLong("id");

                getGroups(school.getLong("id"));

            } catch (JSONException ex) {

            }

        }

        return v;
    }

    private void getGroups (Long schoolId) {
        final String url = Constant.BASE_URL + "groupsBySchoolId/" + schoolId ;

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        Log.d("Response", response.toString());
                        isNoTeam = false;

                        try {
                            JSONArray responseArr = new JSONArray(response.toString());
                            for (int i = 0; i < responseArr.length(); i++) {
                                JSONObject obj = responseArr.getJSONObject(i);

                                GroupTeam groupTeam = gson.fromJson(obj.toString(), GroupTeam.class);

                                groupList.add(groupTeam);
                            }
                            mAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                JSONObject obj = new JSONObject(res);
                                Log.d("obj", "" + obj);

                                isNoTeam = true;
                            } catch (UnsupportedEncodingException e1) {
                                e1.printStackTrace();
                            } catch (JSONException e2) {
                                e2.printStackTrace();
                            }
                        }
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("Authorization", "Bearer " + GlobalVar.getInstance().getIdToken());
                return map;
            }
        };

// add it to the RequestQueue
        queue.add(getRequest);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.createTeamBtn) {
            Bundle bundle = new Bundle();

            MyTeamEditFragment fragment2 = new MyTeamEditFragment();
            fragment2.setArguments(bundle);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content, fragment2);
            fragmentTransaction.addToBackStack("profile");  //diganti apa ya?
            fragmentTransaction.commit();
        } else if(v.getId() == R.id.backBtn) {
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    class GroupTeamAdapter extends RecyclerView.Adapter<GroupTeamAdapter.MyViewHolder> {
        private List<GroupTeam> groupList;
        private Context context1;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView groupIdText, teamNameText, memberNamesText;

            public MyViewHolder(View view) {
                super(view);
                groupIdText = (TextView) view.findViewById(R.id.groupIdText);
                teamNameText = (TextView) view.findViewById(R.id.teamNameText);
                memberNamesText = (TextView) view.findViewById(R.id.memberNamesText);
            }
        }

        public GroupTeamAdapter(List<GroupTeam> teamList, Context context1) {
            this.groupList = teamList;
            this.context1 = context1;
        }

        @Override
        public GroupTeamAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_group, parent, false);

            final TextView groupId = (TextView) itemView.findViewById(R.id.groupIdText);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText( getActivity(), "I am Clicked! " + groupId.getText().toString(), Toast.LENGTH_SHORT).show();
                    String url = Constant.BASE_URL + "groups/" + groupId.getText().toString();
                    RequestQueue queue = Volley.newRequestQueue(context);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("VOLLEY1", response);
                            try {
                                JSONObject group = new JSONObject(response);

                                GroupTeam groupTeam = gson.fromJson(group.toString(), GroupTeam.class);

                                Bundle bundle = new Bundle();
                                bundle.putParcelable("item", groupTeam);

                                MyTeamJoinFragment fragment2 = new MyTeamJoinFragment();
                                fragment2.setArguments(bundle);

                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.content, fragment2);
                                fragmentTransaction.addToBackStack("profile");  //diganti apa ya?
                                fragmentTransaction.commit();
                            } catch (JSONException e2) {
                                e2.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("VOLLEY2", error.toString());
                            NetworkResponse response = error.networkResponse;
                            if (error instanceof ServerError && response != null) {
                                try {
                                    String res = new String(response.data,
                                            HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                    JSONObject obj = new JSONObject(res);
                                    Log.d("obj", "" + obj);
                                    isNoTeam = true;

                                } catch (UnsupportedEncodingException e1) {
                                    e1.printStackTrace();
                                } catch (JSONException e2) {
                                    e2.printStackTrace();
                                }
                            }
                        }
                    })
                    {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("Authorization", "Bearer " + GlobalVar.getInstance().getIdToken());
                            Log.d("mapheader", map.toString());
                            return map;
                        }
                    };
                    queue.add(stringRequest);
                }
            });
            return new GroupTeamAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(GroupTeamAdapter.MyViewHolder holder, int position) {
            GroupTeam group = groupList.get(position);
            holder.groupIdText.setText(group.getId().toString());
            holder.teamNameText.setText(group.getDesc());

        }

        @Override
        public int getItemCount() {
            return groupList.size();
        }

        public GroupTeam getItem(int position) {
            return groupList.get(position);
        }
    }
}
