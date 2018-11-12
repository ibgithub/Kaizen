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
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
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
import com.bumptech.glide.Glide;
import com.dev.kaizen.ForgotPasswordActivity;
import com.dev.kaizen.LoginActivity;
import com.dev.kaizen.MainActivity;
import com.dev.kaizen.R;
import com.dev.kaizen.adapter.GroupTeam;
import com.dev.kaizen.adapter.Team;
import com.dev.kaizen.adapter.Quotes;
import com.dev.kaizen.base.BaseMenuActivity;
import com.dev.kaizen.base.CustomDialogClass2;
import com.dev.kaizen.menu.QuotesListActivity;
import com.dev.kaizen.util.Constant;
import com.dev.kaizen.util.FontUtils;
import com.dev.kaizen.util.GlobalVar;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;
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

import de.hdodenhof.circleimageview.CircleImageView;


public class MyTeamFragment extends Fragment implements View.OnClickListener{
    private Context context;
    private RequestQueue queue;
    
    private RecyclerView recyclerView;
    private TeamAdapter mAdapter;
    private List<Team> teamList = new ArrayList<>();
    private boolean isNoTeam;
    
    private Button updateTeamBtn;
    private Button leaveTeamBtn;
    private Long userId;
    private TextView descText;
    private TextView teamMemberTxt;
    private GroupTeam groupTeam;
    private Gson gson;

    public static MyTeamFragment newInstance() {
        MyTeamFragment fragment = new MyTeamFragment();
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

        groupTeam = getArguments().getParcelable("item");
        Log.d("groupTeam:", "" + groupTeam);

        TextView headertext = (TextView) getActivity().findViewById(R.id.headertext);
        headertext.setText("My Team");
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
        updateTeamBtn.setOnClickListener(this);

        leaveTeamBtn = (Button) v.findViewById(R.id.leaveTeamBtn);
        leaveTeamBtn.setOnClickListener(this);

        queue = Volley.newRequestQueue(getActivity());

        if(GlobalVar.getInstance().getProfile() == null) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Pesan")
                    .setMessage("Anda belum mengisi data Profile diri Anda.")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Toast.makeText(getActivity(), "Silahkan Update ", Toast.LENGTH_SHORT).show();

                            Bundle bundle = new Bundle();
                            if (!isNoTeam) { //update team
                                bundle.putParcelable("item", groupTeam);
                            }

                            ProfileFragment fragment2 = new ProfileFragment();
                            fragment2.setArguments(bundle);

                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content, fragment2);
                            fragmentTransaction.addToBackStack("more");  //diganti apa ya?
                            fragmentTransaction.commit();
                        }
                    })
                    //.setNegativeButton(android.R.string.no, null)
                    .show();
        } else {
            try {
                JSONObject profile = new JSONObject(GlobalVar.getInstance().getProfile());
                JSONObject school = profile.getJSONObject("school");

                schoolText.setText(school.getString("schoolName"));

                JSONObject account = new JSONObject(GlobalVar.getInstance().getAccount());
                userId = account.getLong("id");
                getParticipants();

            } catch (JSONException ex) {

            }

        }

        return v;
    }

    private void getGroups() {
        try {
            JSONObject obj = new JSONObject(GlobalVar.getInstance().getAccount());
            String url = Constant.BASE_URL + "groupsByUserId/" + obj.getString("id");
            RequestQueue queue = Volley.newRequestQueue(context);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY1", response);
                    try {
                        JSONObject group = new JSONObject(response);
                        descText.setText((group.getString("desc").equals("null"))? "":group.getString("desc"));

                        groupTeam = gson.fromJson(group.toString(), GroupTeam.class);

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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getParticipants () {
        final String url = Constant.BASE_URL + "groupNamesByUserId/" + userId ;

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

                                Team team = new Team(obj.getInt("id"),
                                        obj.getString("fullName"),
                                        obj.getString("schoolClass"),
                                        obj.getString("address"),
                                        obj.getJSONObject("user"),
                                        obj.getJSONObject("school"));

                                teamList.add(team);
                            }
                            mAdapter.notifyDataSetChanged();
                            if (groupTeam == null) {
                                getGroups();
                            } else {
                                descText.setText(groupTeam.getDesc());
                            }
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

                                final CustomDialogClass2 cd = new CustomDialogClass2(getActivity());
                                cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                cd.show();
                                cd.setCanceledOnTouchOutside(false);
                                cd.header.setText("Belum memiliki Tim");
                                cd.isi.setText("Kamu belum memiliki Tim, silahkan memilih atau membuat Tim Baru untuk Sekolahmu");
                                Bundle bundle = new Bundle();
                                if (!isNoTeam) { //update team
                                    bundle.putParcelable("item", groupTeam);
                                }

                                isNoTeam = true;

                                MyTeamListFragment fragment2 = new MyTeamListFragment();
                                fragment2.setArguments(bundle);

                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.content, fragment2);
                                fragmentTransaction.addToBackStack("profile");  //diganti apa ya?
                                fragmentTransaction.commit();

//                                updateTeamBtn.setText("Create Team");
//                                leaveTeamBtn.setVisibility(View.INVISIBLE);
//                                teamMemberTxt.setVisibility(View.INVISIBLE);
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
        if (v.getId() == R.id.leaveTeamBtn) {

        }else if (v.getId() == R.id.updateTeamBtn) {
            Bundle bundle = new Bundle();
            if (!isNoTeam) { //update team
                bundle.putParcelable("item", groupTeam);
            }

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
