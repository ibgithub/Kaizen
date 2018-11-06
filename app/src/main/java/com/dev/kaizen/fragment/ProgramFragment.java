/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dev.kaizen.fragment;

import android.content.Context;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.bumptech.glide.Glide;
import com.dev.kaizen.R;
import com.dev.kaizen.adapter.Program;
import com.dev.kaizen.base.CustomDialogClass2;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProgramFragment extends Fragment implements View.OnClickListener {
    private Context context;
    private RecyclerView recyclerView;
    private ProgramAdapter mAdapter;
    private List<Program> programList = new ArrayList<>();
    private boolean isNoTeam;

    public static ProgramFragment newInstance() {
        ProgramFragment fragment = new ProgramFragment();
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
        View v = inflater.inflate(R.layout.fragment_program, container, false);

        Button backBtn = (Button) getActivity().findViewById(R.id.backBtn);
        backBtn.setVisibility(Button.INVISIBLE);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(Toolbar.GONE);

        TextView tv = (TextView) v.findViewById(R.id.programText);
        tv.setTypeface(FontUtils.loadFontFromAssets(context));

        recyclerView = (RecyclerView) v.findViewById(R.id.rvList);

        if(programList.size() == 0) {
            this.getGroups();
        }

        mAdapter = new ProgramAdapter(programList, context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        CircleImageView addBtn =(CircleImageView) v.findViewById(R.id.addBtn);
        addBtn.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        if (isNoTeam) {
            final CustomDialogClass2 cd = new CustomDialogClass2(getActivity());
            cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            cd.show();
            cd.setCanceledOnTouchOutside(false);
            cd.header.setText("Belum Punya Tim");
            cd.isi.setText("Anda belum memiliki Tim, silahkan membuat Tim terlebih dulu");
        } else {
            AddProgramFragment fragment2 = new AddProgramFragment();

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content, fragment2);
            fragmentTransaction.addToBackStack("program");
            fragmentTransaction.commit();
        }
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
                    GlobalVar.getInstance().setGrup(response);
                    isNoTeam = false;
                    getProgram();
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

//                            final CustomDialogClass2 cd = new CustomDialogClass2(getActivity());
//                            cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                            cd.show();
//                            cd.setCanceledOnTouchOutside(false);
//                            cd.header.setText(obj.getString("title"));
//                            cd.isi.setText("Anda belum memiliki Tim, silahkan membuat Tim Baru");

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
    
    public void getProgram() {
        try {
            JSONObject obj = new JSONObject(GlobalVar.getInstance().getGrup());
            String url = Constant.BASE_URL + "programsByGroupId/" + obj.getString("id");

            RequestQueue queue = Volley.newRequestQueue(context);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY3", response);

                    try {
                        JSONArray responseArr = new JSONArray(response.toString());
                        for (int i = 0; i < responseArr.length(); i++) {
                            JSONObject jsonobject = responseArr.getJSONObject(i);

                            Program program = new Program(jsonobject.getInt("id"),
                                    jsonobject.getString("programName"),
                                    jsonobject.getString("background"),
                                    jsonobject.getString("totalDays"),
                                    jsonobject.getString("totalBudget"),
                                    jsonobject.getString("problemList"),
                                    jsonobject.getString("taskList"),
                                    jsonobject.getString("resultList"),
                                    jsonobject.getString("urlPhotoBefore"),
                                    jsonobject.getString("urlPhotoAfter"),
                                    jsonobject.getString("urlVideo"),
                                    jsonobject.getString("notes"),
                                    jsonobject.getJSONObject("participantGroup").toString(),
                                    (jsonobject.has("memberList"))?jsonobject.getString("memberList"):null);
                            programList.add(program);
                        }

                        mAdapter.notifyDataSetChanged();
                        getTeamMembers();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY4", error.toString());

//                    NetworkResponse response = error.networkResponse;
//                    if (error instanceof ServerError && response != null) {
//                        try {
//                            String res = new String(response.data,
//                                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
//                            JSONObject obj = new JSONObject(res);
//                            Log.d("obj", "" + obj);
//
//                            final CustomDialogClass2 cd = new CustomDialogClass2(context);
//                            cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                            cd.show();
//                            cd.setCanceledOnTouchOutside(false);
//                            cd.header.setText("Message");
//                            cd.isi.setText(obj.getString("title"));
//                        } catch (UnsupportedEncodingException e1) {
//                            e1.printStackTrace();
//                        } catch (JSONException e2) {
//                            e2.printStackTrace();
//                        }
//                    }
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

    void getTeamMembers () {
        try {
            JSONObject obj = new JSONObject(GlobalVar.getInstance().getAccount());
            final Long userId = Long.parseLong(obj.getString("id"));
            String url = Constant.BASE_URL + "groupNamesByUserId/" + userId ;
            final List<String> teamMembers = new ArrayList<String>();
            RequestQueue queue = Volley.newRequestQueue(context);
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

                                    teamMembers.add(obj.getString("fullName"));
                                }
                                GlobalVar.getInstance().setTeamMembers(teamMembers);
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
                                    cd.header.setText(obj.getString("title"));
                                    cd.isi.setText("Anda belum memiliki Tim, silahkan membuat Tim Baru");
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

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.MyViewHolder> implements View.OnClickListener {
        private List<Program> programList;
        private Context context1;

        private SimpleExoPlayer player;

        private final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
        private DataSource.Factory mediaDataSourceFactory;
        private Handler mainHandler;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public LinearLayout fotoLayout;
            public ImageView fotoBefore, fotoAfter;
            public TextView title;

            private SimpleExoPlayerView exoPlayerView;

            private Button detailBtn;

            public MyViewHolder(View view) {
                super(view);

                fotoLayout = (LinearLayout) view.findViewById(R.id.fotoLayout);
                fotoBefore = (ImageView) view.findViewById(R.id.fotoBefore);
                fotoAfter = (ImageView) view.findViewById(R.id.fotoAfter);
                title = (TextView) view.findViewById(R.id.title);

                title.setTypeface(FontUtils.loadFontFromAssets(context1, Constant.FONT_BOLD));

                TextView tv = (TextView) view.findViewById(R.id.beforeText);
                tv.setTypeface(FontUtils.loadFontFromAssets(context1, Constant.FONT_BOLD));

                tv = (TextView) view.findViewById(R.id.afterText);
                tv.setTypeface(FontUtils.loadFontFromAssets(context1, Constant.FONT_BOLD));

                mediaDataSourceFactory = buildDataSourceFactory(true);

                mainHandler = new Handler();
                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                TrackSelection.Factory videoTrackSelectionFactory =
                        new AdaptiveTrackSelection.Factory(bandwidthMeter);
                TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

                LoadControl loadControl = new DefaultLoadControl();

                player = ExoPlayerFactory.newSimpleInstance(context1, trackSelector, loadControl);
                exoPlayerView = (SimpleExoPlayerView) view.findViewById(R.id.exoPlayerView);
                exoPlayerView.setPlayer(player);
                exoPlayerView.setUseController(true);
                exoPlayerView.requestFocus();

                player.setPlayWhenReady(false);

                detailBtn = (Button) view.findViewById(R.id.detailBtn);
            }
        }

        public ProgramAdapter(List<Program> programList, Context context1) {
            this.programList = programList;
            this.context1 = context1;
        }

        @Override
        public ProgramAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.program_row, parent, false);
//            itemView.setOnClickListener(ProgramFragment.this);
            return new ProgramAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProgramAdapter.MyViewHolder holder, int position) {
            Program quotes = programList.get(position);

            if(!quotes.getUrlVideo().equals("null")) {
                DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(Constant.BASE_PICT + "fileUpload" + quotes.getUrlVideo()),
                        mediaDataSourceFactory, extractorsFactory, null, null);
                player.prepare(mediaSource);
            } else {
                holder.exoPlayerView.setVisibility(SimpleExoPlayerView.GONE);
            }

            if(!quotes.getUrlPhotoBefore().equals("null")) {
                Glide.with(context1)
                        .load(Constant.BASE_PICT + "fileUpload" + quotes.getUrlPhotoBefore())
                        //.placeholder(R.drawable.ic_cloud_off_red)
                        .into(holder.fotoBefore);
            }

            if(!quotes.getUrlPhotoAfter().equals("null")) {
                Glide.with(context1)
                        .load(Constant.BASE_PICT + "fileUpload" + quotes.getUrlPhotoAfter())
                        //.placeholder(R.drawable.ic_cloud_off_red)
                        .into(holder.fotoAfter);
            }

            if(quotes.getUrlPhotoBefore().equals("null") && quotes.getUrlPhotoAfter().equals("null")) {
                holder.fotoLayout.setVisibility(LinearLayout.GONE);
            }

            holder.title.setText(quotes.getProgramName());
            if(holder.title.getText().equals("null")) {
                holder.title.setVisibility(TextView.GONE);
            }

            holder.detailBtn.setOnClickListener(this);
            holder.detailBtn.setTag(position);
        }

        @Override
        public int getItemCount() {
            return programList.size();
        }

        public Program getItem(int position) {
            return programList.get(position);
        }

        private MediaSource buildMediaSource(Uri uri, String overrideExtension) {
            int type = TextUtils.isEmpty(overrideExtension) ? Util.inferContentType(uri)
                    : Util.inferContentType("." + overrideExtension);
            switch (type) {
                case C.TYPE_SS:
                    return new SsMediaSource(uri, buildDataSourceFactory(false),
                            new DefaultSsChunkSource.Factory(mediaDataSourceFactory), mainHandler, null);
                case C.TYPE_DASH:
                    return new DashMediaSource(uri, buildDataSourceFactory(false),
                            new DefaultDashChunkSource.Factory(mediaDataSourceFactory), mainHandler, null);
                case C.TYPE_HLS:
                    return new HlsMediaSource(uri, mediaDataSourceFactory, mainHandler, null);
                case C.TYPE_OTHER:
                    return new ExtractorMediaSource(uri, mediaDataSourceFactory, new DefaultExtractorsFactory(),
                            mainHandler, null);
                default: {
                    throw new IllegalStateException("Unsupported type: " + type);
                }
            }
        }

        private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
            return buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
        }

        public DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
            return new DefaultDataSourceFactory(context1, bandwidthMeter,
                    buildHttpDataSourceFactory(bandwidthMeter));
        }

        public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
            return new DefaultHttpDataSourceFactory(Util.getUserAgent(context1, "ExoPlayerDemo"), bandwidthMeter);
        }

        @Override
        public void onClick(View view) {
            int position = Integer.valueOf(view.getTag().toString());

            Bundle bundle = new Bundle();
            bundle.putParcelable("item", getItem(position));

            DetailProgramFragment fragment2 = new DetailProgramFragment();
            fragment2.setArguments(bundle);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content, fragment2);
            fragmentTransaction.addToBackStack("program");
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if(mAdapter.player != null) {
            mAdapter.player.stop();
            mAdapter.player.seekTo(0);
        }
    }
}
