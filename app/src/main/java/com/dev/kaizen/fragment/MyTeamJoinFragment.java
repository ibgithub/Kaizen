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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dev.kaizen.R;
import com.dev.kaizen.adapter.GroupTeam;
import com.dev.kaizen.adapter.Team;
import com.dev.kaizen.base.CustomDialogClass2;
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


public class MyTeamJoinFragment extends Fragment implements View.OnClickListener{
    private Context context;
    private RequestQueue queue;

    private RecyclerView recyclerView;
    private TeamAdapter mAdapter;
    private List<Team> teamList = new ArrayList<>();

    private Button updateTeamBtn;
    private Button leaveTeamBtn;
    private Long memberId;
    private TextView descText;
    private TextView teamMemberTxt;
    private GroupTeam groupTeam;
    private Gson gson;

    public static MyTeamJoinFragment newInstance() {
        MyTeamJoinFragment fragment = new MyTeamJoinFragment();
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
        View v = inflater.inflate(R.layout.fragment_team, container, false);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        TextView headertext = (TextView) getActivity().findViewById(R.id.headertext);
        headertext.setText("Team Detail");
        headertext.setTypeface(FontUtils.loadFontFromAssets(context, Constant.FONT_SEMIBOLD));

        Button backBtn = (Button) getActivity().findViewById(R.id.backBtn);
        backBtn.setVisibility(Button.VISIBLE);
        backBtn.setOnClickListener(this);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(Toolbar.VISIBLE);

        TextView schoolText = (TextView) v.findViewById(R.id.schoolText);
        descText = (TextView) v.findViewById(R.id.descText);
        teamMemberTxt = (TextView) v.findViewById(R.id.teamMemberTxt);

        recyclerView = (RecyclerView) v.findViewById(R.id.rvList);

        mAdapter = new TeamAdapter(teamList, context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        updateTeamBtn = (Button) v.findViewById(R.id.updateTeamBtn);
        updateTeamBtn.setText("Gabung");
        updateTeamBtn.setOnClickListener(this);

        leaveTeamBtn = (Button) v.findViewById(R.id.leaveTeamBtn);
        leaveTeamBtn.setText("Cancel");
        leaveTeamBtn.setOnClickListener(this);

        groupTeam = getArguments().getParcelable("item");
        Log.d("groupTeam:", "" + groupTeam);
        memberId =  groupTeam.getMember1();
        descText.setText(groupTeam.getDesc());

        queue = Volley.newRequestQueue(getActivity());

        try {
            JSONObject profile = new JSONObject(GlobalVar.getInstance().getProfile());
            JSONObject school = profile.getJSONObject("school");

            schoolText.setText(school.getString("schoolName"));


            getParticipants();

        } catch (JSONException ex) {

        }

        return v;
    }

    private void getParticipants () {
        final String url = Constant.BASE_URL + "groupNamesByUserId/" + memberId ;

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
            new Response.Listener<JSONArray>()
            {
                @Override
                public void onResponse(JSONArray response) {
                    // display response
                    Log.d("Response", response.toString());

                    try {
                        JSONArray responseArr = new JSONArray(response.toString());
                        for (int i = 0; i < responseArr.length(); i++) {
                            JSONObject obj = responseArr.getJSONObject(i);

                            Team team = new Team(obj.getInt("id"),
                                    obj.getString("fullName"),
                                    obj.getString("schoolClass"),
                                    obj.getString("address"),
                                    obj.getJSONObject("user"),
                                    obj.getJSONObject("school"));

                            teamList.add(team);
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
        if (v.getId() == R.id.leaveTeamBtn) {   //Cancel join
            Bundle bundle = new Bundle();

            MyTeamListFragment fragment2 = new MyTeamListFragment();
            fragment2.setArguments(bundle);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content, fragment2);
            fragmentTransaction.addToBackStack("profile");
            fragmentTransaction.commit();
        }else if (v.getId() == R.id.updateTeamBtn) {    //Join
            try {
                JSONObject account = new JSONObject(GlobalVar.getInstance().getAccount());
                Long userId = account.getLong("id");

                if (groupTeam.getMember2() == null) { groupTeam.setMember2(userId); }
                else if (groupTeam.getMember3() == null) { groupTeam.setMember3(userId); }
                else if (groupTeam.getMember4() == null) { groupTeam.setMember4(userId); }
                else if (groupTeam.getMember5() == null) { groupTeam.setMember5(userId); }
                else if (groupTeam.getMember6() == null) { groupTeam.setMember6(userId); }
                else if (groupTeam.getMember7() == null) { groupTeam.setMember7(userId); }
                else if (groupTeam.getMember8() == null) { groupTeam.setMember8(userId); }
                else if (groupTeam.getMember9() == null) { groupTeam.setMember9(userId); }

                String url = Constant.BASE_URL + "groups";
                RequestQueue queue = Volley.newRequestQueue(getContext());

                String str = gson.toJson(groupTeam);
                JSONObject json = new JSONObject(str);

                JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, url, json,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("response put team", response.toString());
                                Toast.makeText(getContext(), "Data berhasil disimpan", Toast.LENGTH_LONG).show();

                                groupTeam = gson.fromJson(response.toString(), GroupTeam.class);

                                Bundle bundle = new Bundle();
                                bundle.putParcelable("item", groupTeam);

                                MyTeamFragment fragment2 = new MyTeamFragment();
                                fragment2.setArguments(bundle);

                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.content, fragment2);
                                fragmentTransaction.addToBackStack("profile");
                                fragmentTransaction.commit();
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("VOLLEY", error.toString());
                                NetworkResponse response = error.networkResponse;
                                if (error instanceof AuthFailureError && response != null) {
                                    try {
                                        String res = new String(response.data,
                                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                        Log.e("res", "" + res);
                                        JSONObject obj = new JSONObject(res);
                                        Log.d("obj", "" + obj);

                                    } catch (UnsupportedEncodingException e1) {
                                        e1.printStackTrace();
                                    } catch (JSONException e2) {
                                        e2.printStackTrace();
                                    }
                                } else if (error instanceof ServerError && response != null) {

                                }
                            }
                        }
                )
                {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("Authorization", "Bearer " + GlobalVar.getInstance().getIdToken());
                        Log.d("mapheader", map.toString());
                        return map;
                    }
                };
                queue.add(putRequest);
            } catch (JSONException ex) {

            }
        } else if(v.getId() == R.id.backBtn) {
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.MyViewHolder> {
        private List<Team> teamList;
        private Context context1;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView iconImg;
            public TextView namaText, alamatText, emailText;

            public MyViewHolder(View view) {
                super(view);

                iconImg = (ImageView) view.findViewById(R.id.iconImg);
                namaText = (TextView) view.findViewById(R.id.namaText);
                alamatText = (TextView) view.findViewById(R.id.alamatText);
                emailText = (TextView) view.findViewById(R.id.emailText);
            }
        }

        public TeamAdapter(List<Team> teamList, Context context1) {
            this.teamList = teamList;
            this.context1 = context1;
        }

        @Override
        public TeamAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_team, parent, false);
//            itemView.setOnClickListener(this);
            return new TeamAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(TeamAdapter.MyViewHolder holder, int position) {
            Team team = teamList.get(position);
            holder.namaText.setText(team.getFullName());
            holder.alamatText.setText(team.getAddress());

            JSONObject obj = team.getUser();
            try {
                holder.emailText.setText(obj.getString("email"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return teamList.size();
        }

        public Team getItem(int position) {
            return teamList.get(position);
        }
    }
}
