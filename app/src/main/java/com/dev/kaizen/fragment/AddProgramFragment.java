/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dev.kaizen.fragment;

import android.content.Context;
import android.content.Intent;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dev.kaizen.LoginActivity;
import com.dev.kaizen.R;
import com.dev.kaizen.adapter.Program;
import com.dev.kaizen.adapter.Team;
import com.dev.kaizen.base.BaseMenuActivity;
import com.dev.kaizen.base.CustomDialogClass2;
import com.dev.kaizen.util.Constant;
import com.dev.kaizen.util.FontUtils;
import com.dev.kaizen.util.GlobalVar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AddProgramFragment extends Fragment implements View.OnClickListener{
    private Context context;
    
    private RecyclerView rvMasalah;
    private MasalahAdapter masalahAdapter;
    private List<String> masalahList = new ArrayList<>();

    private RecyclerView rvTugas;
    private TugasAdapter tugasAdapter;
    private List<String> tugasList = new ArrayList<>();

    private RecyclerView rvHasil;
    private HasilAdapter hasilAdapter;
    private List<String> hasilList = new ArrayList<>();

    private Program program;

    private EditText nama, latar, lama, biaya;

    public static AddProgramFragment newInstance() {
        AddProgramFragment fragment = new AddProgramFragment();
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
        View v = inflater.inflate(R.layout.add_program, container, false);

        TextView headertext = (TextView) getActivity().findViewById(R.id.headertext);
        headertext.setText("Add Program");
        headertext.setTypeface(FontUtils.loadFontFromAssets(context, Constant.FONT_SEMIBOLD));

        Button backBtn = (Button) getActivity().findViewById(R.id.backBtn);
        backBtn.setVisibility(Button.VISIBLE);
        backBtn.setOnClickListener(this);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(Toolbar.VISIBLE);

        Button btn = (Button) v.findViewById(R.id.nextBtn);
        btn.setOnClickListener(this);

        masalahList.add("");
        rvMasalah = (RecyclerView) v.findViewById(R.id.rvMasalah);
        masalahAdapter = new MasalahAdapter(masalahList, context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        rvMasalah.setLayoutManager(mLayoutManager);
        rvMasalah.setItemAnimator(new DefaultItemAnimator());
        rvMasalah.setAdapter(masalahAdapter);

        tugasList.add("");
        rvTugas = (RecyclerView) v.findViewById(R.id.rvTugas);
        tugasAdapter = new TugasAdapter(tugasList, context);
        mLayoutManager = new LinearLayoutManager(context);
        rvTugas.setLayoutManager(mLayoutManager);
        rvTugas.setItemAnimator(new DefaultItemAnimator());
        rvTugas.setAdapter(tugasAdapter);

        hasilList.add("");
        rvHasil = (RecyclerView) v.findViewById(R.id.rvHasil);
        hasilAdapter = new HasilAdapter(hasilList, context);
        mLayoutManager = new LinearLayoutManager(context);
        rvHasil.setLayoutManager(mLayoutManager);
        rvHasil.setItemAnimator(new DefaultItemAnimator());
        rvHasil.setAdapter(hasilAdapter);

        btn = (Button) v.findViewById(R.id.masalahBtn);
        btn.setOnClickListener(this);

        btn = (Button) v.findViewById(R.id.tugasBtn);
        btn.setOnClickListener(this);

        btn = (Button) v.findViewById(R.id.hasilBtn);
        btn.setOnClickListener(this);

        nama = (EditText) v.findViewById(R.id.nama);
        latar = (EditText) v.findViewById(R.id.latar);
        lama = (EditText) v.findViewById(R.id.lama);
        biaya = (EditText) v.findViewById(R.id.biaya);

        if(getArguments() != null) {
            setValue(v);
        }

        return v;
    }

    private void setValue(View v) {
        program = getArguments().getParcelable("item");

        nama.setText(program.getProgramName());

        latar.setText((program.getBackground().equals("null"))? "":program.getBackground());

        lama.setText((program.getTotalDays().equals("null"))? "":program.getTotalDays());

        biaya.setText((program.getTotalBudget().equals("null"))? "":program.getTotalBudget());

        if(!program.getProblemList().equals("null")) {
            String[] listMasalah = program.getProblemList().split("|");
            for(int i=0; i<listMasalah.length; i++) {
                masalahList.add("- " + listMasalah[i]);
            }
            masalahAdapter.notifyDataSetChanged();
        }

        if(!program.getTaskList().equals("null")) {
            String[] listTugas = program.getTaskList().split("|");
            for(int i=0; i<listTugas.length; i++) {
                tugasList.add("- " + listTugas[i]);
            }
            tugasAdapter.notifyDataSetChanged();
        }

        if(!program.getResultList().equals("null")) {
            String[] listHasil = program.getResultList().split("|");
            for(int i=0; i<listHasil.length; i++) {
                hasilList.add("- " + listHasil[i]);
            }
            hasilAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.backBtn) {
            getFragmentManager().popBackStackImmediate();
        } else if(v.getId() == R.id.nextBtn) {
            if (nama.getText().toString().trim().equals("")) {
                final CustomDialogClass2 cd = new CustomDialogClass2(getActivity());
                cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cd.show();
                cd.setCanceledOnTouchOutside(false);
                cd.header.setText("Pesan");
                cd.isi.setText("Nama Program masih kosong");
            } else {
                String url = Constant.BASE_URL + "programs";

                RequestQueue queue = Volley.newRequestQueue(getContext());

                // prepare the Request
                JSONObject json = new JSONObject();
                try {
                    json.put("programName", nama.getText().toString().trim());
                    json.put("background", latar.getText().toString().trim());
                    json.put("totalDays", lama.getText().toString().trim());
                    json.put("totalBudget", biaya.getText().toString().trim());

                    StringBuilder problemList = new StringBuilder();
                    for(int i=0; i<masalahList.size(); i++){
                        problemList.append(masalahList.get(i));
                        if(i != masalahList.size()-1) {
                            problemList.append("|");
                        }
                    }
                    json.put("problemList", problemList.toString());

                    StringBuilder taskList = new StringBuilder();
                    for(int i=0; i<tugasList.size(); i++){
                        taskList.append(tugasList.get(i));
                        if(i != tugasList.size()-1) {
                            taskList.append("|");
                        }
                    }
                    json.put("taskList", taskList.toString());

                    StringBuilder resultList = new StringBuilder();
                    for(int i=0; i<hasilList.size(); i++){
                        resultList.append(hasilList.get(i));
                        if(i != hasilList.size()-1) {
                            resultList.append("|");
                        }
                    }
                    json.put("resultList", resultList.toString());

                    if(program != null) {
                        json.put("id", program.getId());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest postRequest = new JsonObjectRequest((program != null)? Request.Method.PUT:Request.Method.POST, url, json,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("response", response.toString());

                                Bundle bundle = new Bundle();
                                bundle.putString("response", response.toString());

                                AddProgram2Fragment fragment2 = new AddProgram2Fragment();
                                fragment2.setArguments(bundle);

                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.content, fragment2);
                                fragmentTransaction.addToBackStack("add");
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

                                        final CustomDialogClass2 cd = new CustomDialogClass2(getActivity());
                                        cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        cd.show();
                                        cd.setCanceledOnTouchOutside(false);
                                        cd.header.setText("Message");
                                        String title = obj.getString("title");
                                        String detail = obj.getString("detail");

                                        cd.isi.setText(title + ": " + detail);
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
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("Authorization", "Bearer " + GlobalVar.getInstance().getIdToken());
//                        map.put("programName", nama.getText().toString().trim());
                        Log.d("mapheader", map.toString());
                        return map;
                    }
                };
                queue.add(postRequest);
            }
        } else if(v.getId() == R.id.masalahBtn) {
            masalahList.add("");
            masalahAdapter.notifyDataSetChanged();
        } else if(v.getId() == R.id.tugasBtn) {
            tugasList.add("");
            tugasAdapter.notifyDataSetChanged();
        } else if(v.getId() == R.id.hasilBtn) {
            hasilList.add("");
            hasilAdapter.notifyDataSetChanged();
        }
    }

    class MasalahAdapter extends RecyclerView.Adapter<MasalahAdapter.MyViewHolder> implements View.OnClickListener {
        private List<String> masalahList1;
        private Context context1;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public EditText daftarEdit;
            public ImageView deleteDaftar;
            public MyCustomEditTextListener myCustomEditTextListener;

            public MyViewHolder(View view, MyCustomEditTextListener myCustomEditTextListener) {
                super(view);

                daftarEdit = (EditText) view.findViewById(R.id.daftarEdit);
                deleteDaftar = (ImageView) view.findViewById(R.id.deleteDaftar);

                this.myCustomEditTextListener = myCustomEditTextListener;
                daftarEdit.addTextChangedListener(myCustomEditTextListener);
            }
        }

        public MasalahAdapter(List<String> masalahList, Context context1) {
            this.masalahList1 = masalahList;
            this.context1 = context1;
        }

        @Override
        public MasalahAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tambah_daftar, parent, false);
//            itemView.setOnClickListener(this);
            return new MasalahAdapter.MyViewHolder(itemView, new MyCustomEditTextListener());
        }

        @Override
        public void onBindViewHolder(final MasalahAdapter.MyViewHolder holder, final int position) {
            final String item = masalahList.get(position);

            holder.daftarEdit.setHint("Masukkan daftar masalah");
            holder.myCustomEditTextListener.updatePosition(holder.getAdapterPosition());
            holder.daftarEdit.setText(item);

            holder.deleteDaftar.setOnClickListener(this);
            holder.deleteDaftar.setTag(position);
        }

        @Override
        public int getItemCount() {
            return masalahList1.size();
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.deleteDaftar) {
                int position = Integer.valueOf(view.getTag().toString());
                masalahList.remove(position);
                masalahAdapter.notifyDataSetChanged();
            }
        }

        private class MyCustomEditTextListener implements TextWatcher {
            private int position;

            public void updatePosition(int position) {
                this.position = position;
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                // no op
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                masalahList.set(position, charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // no op
            }
        }
    }

    class TugasAdapter extends RecyclerView.Adapter<TugasAdapter.MyViewHolder> implements View.OnClickListener {
        private List<String> tugasList1;
        private Context context1;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public EditText daftarEdit;
            public ImageView deleteDaftar;
            public MyCustomEditTextListener myCustomEditTextListener;

            public MyViewHolder(View view, MyCustomEditTextListener myCustomEditTextListener) {
                super(view);

                daftarEdit = (EditText) view.findViewById(R.id.daftarEdit);
                deleteDaftar = (ImageView) view.findViewById(R.id.deleteDaftar);

                this.myCustomEditTextListener = myCustomEditTextListener;
                daftarEdit.addTextChangedListener(myCustomEditTextListener);
            }
        }

        public TugasAdapter(List<String> tugasList1, Context context1) {
            this.tugasList1 = tugasList1;
            this.context1 = context1;
        }

        @Override
        public TugasAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tambah_daftar, parent, false);
//            itemView.setOnClickListener(this);
            return new TugasAdapter.MyViewHolder(itemView, new MyCustomEditTextListener());
        }

        @Override
        public void onBindViewHolder(final TugasAdapter.MyViewHolder holder, final int position) {
            final String item = tugasList.get(position);

            holder.daftarEdit.setHint("Masukkan daftar tugas");
            holder.myCustomEditTextListener.updatePosition(holder.getAdapterPosition());
            holder.daftarEdit.setText(item);

            holder.deleteDaftar.setOnClickListener(this);
            holder.deleteDaftar.setTag(position);
        }

        @Override
        public int getItemCount() {
            return tugasList1.size();
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.deleteDaftar) {
                int position = Integer.valueOf(view.getTag().toString());
                tugasList.remove(position);
                tugasAdapter.notifyDataSetChanged();
            }
        }

        private class MyCustomEditTextListener implements TextWatcher {
            private int position;

            public void updatePosition(int position) {
                this.position = position;
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                // no op
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                tugasList.set(position, charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // no op
            }
        }
    }

    class HasilAdapter extends RecyclerView.Adapter<HasilAdapter.MyViewHolder> implements View.OnClickListener {
        private List<String> hasilList1;
        private Context context1;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public EditText daftarEdit;
            public ImageView deleteDaftar;
            public MyCustomEditTextListener myCustomEditTextListener;

            public MyViewHolder(View view, MyCustomEditTextListener myCustomEditTextListener) {
                super(view);

                daftarEdit = (EditText) view.findViewById(R.id.daftarEdit);
                deleteDaftar = (ImageView) view.findViewById(R.id.deleteDaftar);

                this.myCustomEditTextListener = myCustomEditTextListener;
                daftarEdit.addTextChangedListener(myCustomEditTextListener);
            }
        }

        public HasilAdapter(List<String> hasilList1, Context context1) {
            this.hasilList1 = hasilList1;
            this.context1 = context1;
        }

        @Override
        public HasilAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tambah_daftar, parent, false);
//            itemView.setOnClickListener(this);
            return new HasilAdapter.MyViewHolder(itemView, new MyCustomEditTextListener());
        }

        @Override
        public void onBindViewHolder(final HasilAdapter.MyViewHolder holder, final int position) {
            final String item = hasilList.get(position);

            holder.daftarEdit.setHint("Masukkan daftar hasil");
            holder.myCustomEditTextListener.updatePosition(holder.getAdapterPosition());
            holder.daftarEdit.setText(item);

            holder.deleteDaftar.setOnClickListener(this);
            holder.deleteDaftar.setTag(position);
        }

        @Override
        public int getItemCount() {
            return hasilList1.size();
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.deleteDaftar) {
                int position = Integer.valueOf(view.getTag().toString());
                hasilList.remove(position);
                hasilAdapter.notifyDataSetChanged();
            }
        }

        private class MyCustomEditTextListener implements TextWatcher {
            private int position;

            public void updatePosition(int position) {
                this.position = position;
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                // no op
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                hasilList.set(position, charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // no op
            }
        }
    }
}
