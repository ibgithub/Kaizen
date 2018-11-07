/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dev.kaizen.fragment;

import android.app.Dialog;
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
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.dev.kaizen.R;
import com.dev.kaizen.adapter.Quotes;
import com.dev.kaizen.base.CustomDialogClass2;
import com.dev.kaizen.menu.QuotesListActivity;
import com.dev.kaizen.util.Constant;
import com.dev.kaizen.util.FontUtils;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlaybackControlView;
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

import java.util.ArrayList;
import java.util.List;


public class QuotesFragment extends Fragment implements  View.OnClickListener {
    private Context context;

    private RecyclerView recyclerView;
    private QuotesAdapter mAdapter;
    private List<Quotes> quotesList = new ArrayList<>();
    
    public static QuotesFragment newInstance() {
        QuotesFragment fragment = new QuotesFragment();

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
        View v = inflater.inflate(R.layout.layout_list, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.rvList);

        this.getImageUrl();

        mAdapter = new QuotesAdapter(quotesList, context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        TextView headertext = (TextView) getActivity().findViewById(R.id.headertext);
        headertext.setText("Kaizen Quotes");
        if(getArguments().getString("menu").equals("sample")) {
            headertext.setText("Kaizen Sample");
        } else if(getArguments().getString("menu").equals("tutorial")) {
            headertext.setText("Kaizen Tutorial");
        } else if(getArguments().getString("menu").equals("corner")) {
            headertext.setText("Kaizen Corner");
        } else if(getArguments().getString("menu").equals("tools")) {
            headertext.setText("Kaizen Tools");
        } else if(getArguments().getString("menu").equals("testimonial")) {
            headertext.setText("Testimonial");
        }
        headertext.setTypeface(FontUtils.loadFontFromAssets(context, Constant.FONT_SEMIBOLD));

        Button backBtn = (Button) getActivity().findViewById(R.id.backBtn);
        backBtn.setVisibility(Button.VISIBLE);
        backBtn.setOnClickListener(this);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(Toolbar.VISIBLE);

        return v;
    }

    private void getImageUrl () {
        String urlGet = "quotes";
        if(getArguments().getString("menu").equals("sample")) {
            urlGet = "sample-gudang-ides";
        } else if(getArguments().getString("menu").equals("tutorial")) {
            urlGet = "tutorials";
        } else if(getArguments().getString("menu").equals("corner")) {
            urlGet = "inspiring-corners";
        } else if(getArguments().getString("menu").equals("tools")) {
            urlGet = "tools";
        } else if(getArguments().getString("menu").equals("testimonial")) {
            urlGet = "isi-outlines";
        }

        final String url = Constant.BASE_URL + urlGet;

        RequestQueue queue = Volley.newRequestQueue(context);

// prepare the Request
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
                                JSONObject jsonobject = responseArr.getJSONObject(i);

                                Quotes quotes = new Quotes();
                                if(getArguments().getString("menu").equals("tutorial")) {
                                    quotes = new Quotes(jsonobject.getInt("id"),
                                            jsonobject.getString("title"),
                                            jsonobject.getString("tagline"),
                                            (jsonobject.has("urlPhoto"))?jsonobject.getString("urlPhoto"):"",
                                            (jsonobject.has("description"))?jsonobject.getString("description"):"null",
                                            (jsonobject.has("desc"))?jsonobject.getString("desc"):"");
                                } else if(getArguments().getString("menu").equals("corner")) {
                                    quotes = new Quotes(jsonobject.getInt("id"),
                                            jsonobject.getString("name"),
                                            jsonobject.getString("jobDesc"),
                                            (jsonobject.has("avatar"))?jsonobject.getString("avatar"):"",
                                            (jsonobject.has("comments"))?jsonobject.getString("comments"):"null",
                                            "");
                                } else if(getArguments().getString("menu").equals("tools") || getArguments().getString("menu").equals("testimonial")) {
                                    quotes = new Quotes(jsonobject.getInt("id"),
                                            jsonobject.getString("title"),
                                            jsonobject.getString("tagline"),
                                            "",
                                            (jsonobject.has("desc"))?jsonobject.getString("desc"):"null",
                                            (jsonobject.has("urlVideo"))?jsonobject.getString("urlVideo"):"");
                                } else {
                                    quotes = new Quotes(jsonobject.getInt("id"),
                                            jsonobject.getString("title"),
                                            jsonobject.getString("tagline"),
                                            (jsonobject.has("urlPhoto"))?jsonobject.getString("urlPhoto"):"",
                                            (jsonobject.has("description"))?jsonobject.getString("description"):"null",
                                            (jsonobject.has("urlVideo"))?jsonobject.getString("urlVideo"):"");
                                }


                                quotesList.add(quotes);
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
//                        Log.d("Error.Response", error.getMessage());
                    }
                }
        );

// add it to the RequestQueue
        queue.add(getRequest);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.backBtn) {
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    class QuotesAdapter extends RecyclerView.Adapter<QuotesAdapter.MyViewHolder> {
        private List<Quotes> quotesList;
        private Context context1;

        private final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
        private DataSource.Factory mediaDataSourceFactory;
//        private Handler mainHandler;

//        private MyViewHolder holderView;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView img_quotes;
            public TextView title, tagline, description;
//            public VideoPlayView bigVideoView;

            private SimpleExoPlayerView exoPlayerView;
            private ImageView mFullScreenIcon;
            private FrameLayout mFullScreenButton;
            private boolean mExoPlayerFullscreen = false;
            private Dialog mFullScreenDialog;

            private SimpleExoPlayer player;

            private FrameLayout mediaFrame;

            public MyViewHolder(View view) {
                super(view);

                img_quotes = (ImageView) view.findViewById(R.id.img_quotes);
//                bigVideoView = (VideoPlayView) view.findViewById(R.id.bigVideoView);
                title = (TextView) view.findViewById(R.id.title);
                tagline = (TextView) view.findViewById(R.id.tagline);
                description = (TextView) view.findViewById(R.id.desc);

                title.setTypeface(FontUtils.loadFontFromAssets(context1));
                tagline.setTypeface(FontUtils.loadFontFromAssets(context1));
                description.setTypeface(FontUtils.loadFontFromAssets(context1));

                mediaDataSourceFactory = buildDataSourceFactory(true);

//                mainHandler = new Handler();
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

                PlaybackControlView controlView = exoPlayerView.findViewById(R.id.exo_controller);
                mFullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
                mFullScreenButton = controlView.findViewById(R.id.exo_fullscreen_button);

                mediaFrame = view.findViewById(R.id.main_media_frame);
            }
        }

        public QuotesAdapter(List<Quotes> quotesList, Context context1) {
            this.quotesList = quotesList;
            this.context1 = context1;
        }

        @Override
        public QuotesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.quotes_row, parent, false);
//            itemView.setOnClickListener(context1);
            return new QuotesAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final QuotesAdapter.MyViewHolder holder, int position) {
            Quotes quotes = quotesList.get(position);

            if(quotes.getUrlVideo().length() > 1) {
                DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(quotes.getUrlVideo()),
                        mediaDataSourceFactory, extractorsFactory, null, null);
                holder.player.prepare(mediaSource);

                holder.mediaFrame.setVisibility(SimpleExoPlayerView.VISIBLE);
//                holder.bigVideoView.setVideoUrl(quotes.getUrlVideo());
//                Glide.with(context)
//                        .load(R.drawable.loader)
                //.placeholder(R.drawable.ic_cloud_off_red)
//                        .into(holder.exoPlayerView.getLoadingView());
            }

            if(quotes.getUrlPhoto().length() > 1) {
                holder.mediaFrame.setVisibility(SimpleExoPlayerView.GONE);
                Glide.with(context1)
                        .load(quotes.getUrlPhoto())
                        //.placeholder(R.drawable.ic_cloud_off_red)
                        .into(holder.img_quotes);
            }


            holder.title.setText(quotes.getTitle());
            holder.tagline.setText(quotes.getTagline());
            holder.description.setText(quotes.getDescription());

            if(holder.title.getText().equals("null")) {
                holder.title.setVisibility(TextView.GONE);
            }
            if(holder.tagline.getText().equals("null")) {
                holder.tagline.setVisibility(TextView.GONE);
            }
            if(holder.description.getText().equals("null")) {
                holder.description.setVisibility(TextView.GONE);
            }

            holder.mFullScreenDialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
                public void onBackPressed() {
                    holder.player.setPlayWhenReady(false);
                    if (holder.mExoPlayerFullscreen) {
                        Log.d("exo", "masuk sini");
                        closeFullscreenDialog(holder);
                    }
                    super.onBackPressed();
                }
            };

            holder.mFullScreenButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!holder.mExoPlayerFullscreen)
                        openFullscreenDialog(holder);
                    else {
//                        closeFullscreenDialog(holder);
                        holder.mFullScreenDialog.onBackPressed();
                    }
                }
            });

//            holderView = holder;

            holder.player.addListener(new ExoPlayer.EventListener() {
                @Override
                public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                }

                @Override
                public void onLoadingChanged(boolean isLoading) {

                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    if (playWhenReady && !holder.mExoPlayerFullscreen) {
                        holder.mExoPlayerFullscreen = true;
                        openFullscreenDialog(holder);
                    }
                }

                @Override
                public void onRepeatModeChanged(int repeatMode) {

                }

                @Override
                public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {

                }

                @Override
                public void onPositionDiscontinuity(int reason) {

                }

                @Override
                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                }

                @Override
                public void onSeekProcessed() {

                }
            });
        }

        @Override
        public int getItemCount() {
            return quotesList.size();
        }

        public Quotes getItem(int position) {
            return quotesList.get(position);
        }

//        private MediaSource buildMediaSource(Uri uri, String overrideExtension) {
//            int type = TextUtils.isEmpty(overrideExtension) ? Util.inferContentType(uri)
//                    : Util.inferContentType("." + overrideExtension);
//            switch (type) {
//                case C.TYPE_SS:
//                    return new SsMediaSource(uri, buildDataSourceFactory(false),
//                            new DefaultSsChunkSource.Factory(mediaDataSourceFactory), mainHandler, null);
//                case C.TYPE_DASH:
//                    return new DashMediaSource(uri, buildDataSourceFactory(false),
//                            new DefaultDashChunkSource.Factory(mediaDataSourceFactory), mainHandler, null);
//                case C.TYPE_HLS:
//                    return new HlsMediaSource(uri, mediaDataSourceFactory, mainHandler, null);
//                case C.TYPE_OTHER:
//                    return new ExtractorMediaSource(uri, mediaDataSourceFactory, new DefaultExtractorsFactory(),
//                            mainHandler, null);
//                default: {
//                    throw new IllegalStateException("Unsupported type: " + type);
//                }
//            }
//        }

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

        private void openFullscreenDialog(final QuotesAdapter.MyViewHolder holder) {
            ((ViewGroup) holder.exoPlayerView.getParent()).removeView(holder.exoPlayerView);
            holder.mFullScreenDialog.addContentView(holder.exoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            holder.mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_fullscreen_skrink));
            holder.mExoPlayerFullscreen = true;
            holder.mFullScreenDialog.show();
        }

        private void closeFullscreenDialog(final QuotesAdapter.MyViewHolder holder) {
            ((ViewGroup) holder.exoPlayerView.getParent()).removeView(holder.exoPlayerView);
            ((FrameLayout) getView().findViewById(R.id.main_media_frame)).addView(holder.exoPlayerView);//.addView(holder.exoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 250));
            holder.mExoPlayerFullscreen = false;
            holder.mFullScreenDialog.dismiss();
            holder.mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_fullscreen_expand));
        }

//        private void closePlayer() {
//            Log.d("close player", "close player sukses");
////            if(player != null) {
////                player.stop();
////                player.seekTo(0);
//
////                player.release();
////            }
//
//            if (holderView.exoPlayerView != null && holderView.exoPlayerView.getPlayer() != null) {
////                mResumeWindow = mExoPlayerView.getPlayer().getCurrentWindowIndex();
////                mResumePosition = Math.max(0, mExoPlayerView.getPlayer().getContentPosition());
//
//                holderView.exoPlayerView.getPlayer().setPlayWhenReady(false);
//                holderView.player.setPlayWhenReady(false);
//
////                holderView.exoPlayerView.getPlayer().release();
////                holderView.exoPlayerView.getPlayer().seekTo(0);
////                holderView.exoPlayerView.getPlayer().stop();
//
//                holderView.exoPlayerView = null;
//                holderView.player = null;
//                mediaDataSourceFactory = null;
//                mainHandler = null;
//            }
//
////            if (mFullScreenDialog != null)
////                mFullScreenDialog.dismiss();
//        }
    }

//    @Override
//    public void onStop() {
//        super.onStop();
//
//        mAdapter.closePlayer();
//    }
}
