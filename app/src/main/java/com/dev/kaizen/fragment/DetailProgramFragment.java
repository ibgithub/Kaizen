/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dev.kaizen.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.asura.library.posters.Poster;
import com.asura.library.posters.RemoteImage;
import com.asura.library.posters.RemoteVideo;
import com.asura.library.views.PosterSlider;
import com.dev.kaizen.R;
import com.dev.kaizen.adapter.Program;
import com.dev.kaizen.util.Constant;
import com.dev.kaizen.util.FontUtils;
import com.dev.kaizen.util.GlobalVar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DetailProgramFragment extends Fragment implements View.OnClickListener{
    private Context context;
    private Program program;
    private JSONObject participantObj;

    private PosterSlider posterSlider;
    private Button nextBtn;

    public static DetailProgramFragment newInstance() {
        DetailProgramFragment fragment = new DetailProgramFragment();
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
        View v = inflater.inflate(R.layout.detail_program, container, false);

        program = getArguments().getParcelable("item");
        try {
            participantObj = new JSONObject(program.getParticipantGroup());
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        this.getProvinces();

        TextView headertext = (TextView) getActivity().findViewById(R.id.headertext);
        headertext.setText("Detail Program");
        headertext.setTypeface(FontUtils.loadFontFromAssets(context, Constant.FONT_SEMIBOLD));

        Button backBtn = (Button) getActivity().findViewById(R.id.backBtn);
        backBtn.setVisibility(Button.VISIBLE);
        backBtn.setOnClickListener(this);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(Toolbar.VISIBLE);

        nextBtn = (Button) getActivity().findViewById(R.id.nextBtn);
        nextBtn.setBackgroundResource(R.drawable.ic_edit);
        nextBtn.setVisibility(Button.VISIBLE);
        nextBtn.setOnClickListener(this);

        posterSlider = (PosterSlider) v.findViewById(R.id.poster_slider);
        List<Poster> posters=new ArrayList<>();
        if(program.getUrlPhotoBefore() != null && !program.getUrlPhotoBefore().equals("null")) {
            posters.add(new RemoteImage(Constant.BASE_PICT + "fileUpload" + program.getUrlPhotoBefore()));
        }
        if(program.getUrlPhotoAfter() != null && !program.getUrlPhotoAfter().equals("null")) {
            posters.add(new RemoteImage(Constant.BASE_PICT + "fileUpload" + program.getUrlPhotoAfter()));
        }
        if(program.getUrlVideo() != null && !program.getUrlVideo().equals("null")) {
            posters.add(new RemoteVideo(Uri.parse(Constant.BASE_PICT + "fileUpload" + program.getUrlVideo())));
        }
        posterSlider.setPosters(posters);

        TextView tv = (TextView) v.findViewById(R.id.title);
        tv.setTypeface(FontUtils.loadFontFromAssets(context, Constant.FONT_BOLD));
        tv.setText(program.getProgramName());

        tv = (TextView) v.findViewById(R.id.programText);
        tv.setTypeface(FontUtils.loadFontFromAssets(context, Constant.FONT_BOLD));

        try {
            LinearLayout ll = (LinearLayout) v.findViewById(R.id.mentorLayout);
            if(!participantObj.getString("mentorName").equals("null") && participantObj.getString("mentorName") != null) {
                tv = (TextView) v.findViewById(R.id.mentorText);
                tv.setTypeface(FontUtils.loadFontFromAssets(context));
                tv.setText(participantObj.getString("mentorName"));
            } else {
                ll.setVisibility(LinearLayout.GONE);
            }

            ll = (LinearLayout) v.findViewById(R.id.memberLayout);
            if(program.getMemberList() != null) {
                if(!program.getMemberList().equals("null")) {
                    tv = (TextView) v.findViewById(R.id.memberText);
                    tv.setTypeface(FontUtils.loadFontFromAssets(context));

                    String[] namaMember = program.getMemberList().split("|");
                    StringBuilder member = new StringBuilder();
                    for(int i=0; i<namaMember.length; i++) {
                        member.append("- " + namaMember[i]);
                        if(i != namaMember.length-1) {
                            member.append("\n");
                        }
                    }
                } else {
                    ll.setVisibility(LinearLayout.GONE);
                }
            } else {
                ll.setVisibility(LinearLayout.GONE);
            }

            JSONObject profile = new JSONObject(GlobalVar.getInstance().getProfile());
            JSONObject school = new JSONObject(profile.getString("school"));

            tv = (TextView) v.findViewById(R.id.sekolahText);
            tv.setTypeface(FontUtils.loadFontFromAssets(context));
            tv.setText(school.getString("schoolName"));

            tv = (TextView) v.findViewById(R.id.lokasiText);
            tv.setTypeface(FontUtils.loadFontFromAssets(context));
            tv.setText(school.getString("address"));

            ll = (LinearLayout) v.findViewById(R.id.backgroundLayout);
            if(!program.getBackground().equals("null")) {
                tv = (TextView) v.findViewById(R.id.backgroundText);
                tv.setTypeface(FontUtils.loadFontFromAssets(context));
                tv.setText(program.getBackground());
            } else {
                ll.setVisibility(LinearLayout.GONE);
            }

            ll = (LinearLayout) v.findViewById(R.id.lamaLayout);
            if(!program.getTotalDays().equals("null")) {
                tv = (TextView) v.findViewById(R.id.lamaText);
                tv.setTypeface(FontUtils.loadFontFromAssets(context));
                tv.setText(program.getTotalDays() + " Hari");
            } else {
                ll.setVisibility(LinearLayout.GONE);
            }

            ll = (LinearLayout) v.findViewById(R.id.totalLayout);
            if(!program.getTotalBudget().equals("null")) {
                tv = (TextView) v.findViewById(R.id.totalText);
                tv.setTypeface(FontUtils.loadFontFromAssets(context));
                tv.setText(program.getTotalBudget());
            } else {
                ll.setVisibility(LinearLayout.GONE);
            }

            ll = (LinearLayout) v.findViewById(R.id.masalahLayout);
            if(!program.getProblemList().equals("null")) {
                tv = (TextView) v.findViewById(R.id.masalahText);
                tv.setTypeface(FontUtils.loadFontFromAssets(context));

                String[] listMasalah = program.getProblemList().split("|");
                StringBuilder masalah = new StringBuilder();
                for(int i=0; i<listMasalah.length; i++) {
                    masalah.append("- " + listMasalah[i]);
                    if(i != listMasalah.length-1) {
                        masalah.append("\n");
                    }
                }
            } else {
                ll.setVisibility(LinearLayout.GONE);
            }

            ll = (LinearLayout) v.findViewById(R.id.tugasLayout);
            if(!program.getTaskList().equals("null")) {
                tv = (TextView) v.findViewById(R.id.tugasText);
                tv.setTypeface(FontUtils.loadFontFromAssets(context));

                String[] listTugas = program.getTaskList().split("|");
                StringBuilder tugas = new StringBuilder();
                for(int i=0; i<listTugas.length; i++) {
                    tugas.append("- " + listTugas[i]);
                    if(i != listTugas.length-1) {
                        tugas.append("\n");
                    }
                }
            } else {
                ll.setVisibility(LinearLayout.GONE);
            }

            ll = (LinearLayout) v.findViewById(R.id.hasilLayout);
            if(!program.getResultList().equals("null")) {
                tv = (TextView) v.findViewById(R.id.hasilText);
                tv.setTypeface(FontUtils.loadFontFromAssets(context));

                String[] listHasil = program.getResultList().split("|");
                StringBuilder hasil = new StringBuilder();
                for(int i=0; i<listHasil.length; i++) {
                    hasil.append("- " + listHasil[i]);
                    if(i != listHasil.length-1) {
                        hasil.append("\n");
                    }
                }
            } else {
                ll.setVisibility(LinearLayout.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return v;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.backBtn) {
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else if(v.getId() == R.id.nextBtn) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("item", getArguments().getParcelable("item"));

            AddProgramFragment fragment2 = new AddProgramFragment();
            fragment2.setArguments(bundle);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content, fragment2);
            fragmentTransaction.addToBackStack("detail");
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        posterSlider.onVideoStopped();
        nextBtn.setVisibility(Button.INVISIBLE);
    }
}
