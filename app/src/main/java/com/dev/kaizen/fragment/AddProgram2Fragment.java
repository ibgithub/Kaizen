/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dev.kaizen.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.asura.library.posters.RemoteImage;
import com.asura.library.posters.RemoteVideo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dev.kaizen.BuildConfig;
import com.dev.kaizen.R;
import com.dev.kaizen.adapter.Program;
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

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


public class AddProgram2Fragment extends Fragment implements View.OnClickListener{
    private Context context;

    private Program program;

    private SimpleExoPlayer player;

    private final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private DataSource.Factory mediaDataSourceFactory;
    private Handler mainHandler;

    private SimpleExoPlayerView exoPlayerView;

//    public Uri filePathBefore;
    private ImageView fotoBefore, fotoAfter, videoView;

    private Button uploadPhotoBeforeBtn, uploadPhotoAfterBtn, uploadVideoBtn;

    private int PICK_IMAGE_REQUEST = 1;
    private int PICK_VIDEO_REQUEST = 2;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;
    private static final String UPLOAD_URL = Constant.BASE_URL + "programs/fileUpload";

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePathBefore, filePathAfter, filePathVideo;

    private String choice;

    public static AddProgram2Fragment newInstance() {
        AddProgram2Fragment fragment = new AddProgram2Fragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_program2, container, false);

        TextView headertext = (TextView) getActivity().findViewById(R.id.headertext);
        headertext.setText("Add Program");
        headertext.setTypeface(FontUtils.loadFontFromAssets(context, Constant.FONT_SEMIBOLD));

        Button backBtn = (Button) getActivity().findViewById(R.id.backBtn);
        backBtn.setVisibility(Button.VISIBLE);
        backBtn.setOnClickListener(this);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(Toolbar.VISIBLE);

        fotoBefore = (ImageView) v.findViewById(R.id.fotoBefore);
        fotoBefore.setOnClickListener(this);

        fotoAfter = (ImageView) v.findViewById(R.id.fotoAfter);
        fotoAfter.setOnClickListener(this);

        videoView = (ImageView) v.findViewById(R.id.videoView);
        videoView.setOnClickListener(this);

        uploadPhotoBeforeBtn = (Button) v.findViewById(R.id.uploadPhotoBeforeBtn);
        uploadPhotoBeforeBtn.setOnClickListener(this);

        uploadPhotoAfterBtn = (Button) v.findViewById(R.id.uploadPhotoAfterBtn);
        uploadPhotoAfterBtn.setOnClickListener(this);

        uploadVideoBtn = (Button) v.findViewById(R.id.uploadVideoBtn);
        uploadVideoBtn.setOnClickListener(this);

        mediaDataSourceFactory = buildDataSourceFactory(true);

        mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        LoadControl loadControl = new DefaultLoadControl();

        player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
        exoPlayerView = (SimpleExoPlayerView) v.findViewById(R.id.exoPlayerView);
        exoPlayerView.setPlayer(player);
        exoPlayerView.setUseController(true);
        exoPlayerView.requestFocus();
        exoPlayerView.setOnClickListener(this);

        player.setPlayWhenReady(false);

        if(getArguments().getParcelable("item") != null) {
            program = getArguments().getParcelable("item");

            if(program.getUrlPhotoBefore() != null && !program.getUrlPhotoBefore().equals("null")) {
                Glide.with(getActivity())
                        .load(Constant.BASE_PICT + "fileUpload" + program.getUrlPhotoBefore())
                        //.placeholder(R.drawable.ic_cloud_off_red)
                        .into(fotoBefore);
//            posters.add(new RemoteImage(Constant.BASE_PICT + "fileUpload" + program.getUrlPhotoBefore()));
            }
            if(program.getUrlPhotoAfter() != null && !program.getUrlPhotoAfter().equals("null")) {
                Glide.with(getActivity())
                        .load(Constant.BASE_PICT + "fileUpload" + program.getUrlPhotoAfter())
                        //.placeholder(R.drawable.ic_cloud_off_red)
                        .into(fotoAfter);
//            posters.add(new RemoteImage(Constant.BASE_PICT + "fileUpload" + program.getUrlPhotoAfter()));
            }
            if(program.getUrlVideo() != null && !program.getUrlVideo().equals("null")) {
                DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(Constant.BASE_PICT + "fileUpload" + program.getUrlVideo()),
                        mediaDataSourceFactory, extractorsFactory, null, null);
                player.prepare(mediaSource);
//            posters.add(new RemoteVideo(Uri.parse(Constant.BASE_PICT + "fileUpload" + program.getUrlVideo())));
            }
        }

        //Requesting storage permission
        requestStoragePermission();

        return v;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.backBtn) {
            getFragmentManager().popBackStackImmediate();
        } else if(v.getId() == R.id.uploadPhotoBeforeBtn) {
            if(filePathBefore == null) {
                Toast.makeText(getContext(), "Silahkan pilih foto sebelum program", Toast.LENGTH_LONG);
            } else {
                String path = getPath(filePathBefore);

                try {
                    String uploadId = UUID.randomUUID().toString();

                    JSONObject account = new JSONObject(GlobalVar.getInstance().getAccount());

                    new MultipartUploadRequest(getContext(), uploadId, UPLOAD_URL)
                            .addHeader("Authorization", "Bearer " + GlobalVar.getInstance().getIdToken())
                            .addFileToUpload(path, "file")
                            .addParameter("login", account.getString("login")) //username ketika login
                            .addParameter("programId", ""+program.getId())  //programId
                            .addParameter("uploadType", "PHOTO_BEFORE") //PHOTO_BEFORE|PHOTO_AFTER|VIDEO
                            .setNotificationConfig(new UploadNotificationConfig())
                            .setMaxRetries(2)
                            .startUpload(); //Starting the upload
                } catch (Exception exc) {
                    Toast.makeText(getContext(), exc.getMessage(), Toast.LENGTH_SHORT).show();
                }
                //getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                Toast.makeText(getContext(), "Foto sebelum program berhasil disimpan", Toast.LENGTH_LONG);
            }
        } else if(v.getId() == R.id.uploadPhotoAfterBtn) {
            if(filePathAfter == null) {
                Toast.makeText(getContext(), "Silahkan pilih foto setelah program", Toast.LENGTH_LONG);
            } else {
                String path = getPath(filePathAfter);

                try {
                    String uploadId = UUID.randomUUID().toString();

                    JSONObject account = new JSONObject(GlobalVar.getInstance().getAccount());

                    new MultipartUploadRequest(getContext(), uploadId, UPLOAD_URL)
                            .addHeader("Authorization", "Bearer " + GlobalVar.getInstance().getIdToken())
                            .addFileToUpload(path, "file")
                            .addParameter("login", account.getString("login")) //username ketika login
                            .addParameter("programId", ""+program.getId())  //programId
                            .addParameter("uploadType", "PHOTO_AFTER") //PHOTO_BEFORE|PHOTO_AFTER|VIDEO
                            .setNotificationConfig(new UploadNotificationConfig())
                            .setMaxRetries(2)
                            .startUpload(); //Starting the upload
                } catch (Exception exc) {
                    Toast.makeText(getContext(), exc.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getContext(), "Foto sesudah program berhasil disimpan", Toast.LENGTH_LONG);
            }
        } else if(v.getId() == R.id.uploadVideoBtn) {
            if(filePathVideo == null) {
                Toast.makeText(getContext(), "Silahkan pilih video program", Toast.LENGTH_LONG);
            } else {
                String path = getPath(filePathVideo);

                try {
                    String uploadId = UUID.randomUUID().toString();

                    JSONObject account = new JSONObject(GlobalVar.getInstance().getAccount());

                    new MultipartUploadRequest(getContext(), uploadId, UPLOAD_URL)
                            .addHeader("Authorization", "Bearer " + GlobalVar.getInstance().getIdToken())
                            .addFileToUpload(path, "file")
                            .addParameter("login", account.getString("login")) //username ketika login
                            .addParameter("programId", ""+program.getId())  //programId
                            .addParameter("uploadType", "VIDEO") //PHOTO_BEFORE|PHOTO_AFTER|VIDEO
                            .setNotificationConfig(new UploadNotificationConfig())
                            .setMaxRetries(2)
                            .startUpload(); //Starting the upload
                } catch (Exception exc) {
                    Toast.makeText(getContext(), exc.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getContext(), "Video berhasil disimpan", Toast.LENGTH_LONG);
            }
        } else if(v.getId() == R.id.nextBtn) {
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            Toast.makeText(getContext(), "Data berhasil disimpan", Toast.LENGTH_LONG);
        } else if(v.getId() == R.id.fotoBefore) {
            choice = "fotoBefore";
            showFileChooser();

        } else if(v.getId() == R.id.fotoAfter) {
            choice = "fotoAfter";
            showFileChooser();

        } else if(v.getId() == R.id.videoView) {
            choice = "video";
            showFileChooser();

        }
    }

//    private MediaSource buildMediaSource(Uri uri, String overrideExtension) {
//        int type = TextUtils.isEmpty(overrideExtension) ? Util.inferContentType(uri)
//                : Util.inferContentType("." + overrideExtension);
//        switch (type) {
//            case C.TYPE_SS:
//                return new SsMediaSource(uri, buildDataSourceFactory(false),
//                        new DefaultSsChunkSource.Factory(mediaDataSourceFactory), mainHandler, null);
//            case C.TYPE_DASH:
//                return new DashMediaSource(uri, buildDataSourceFactory(false),
//                        new DefaultDashChunkSource.Factory(mediaDataSourceFactory), mainHandler, null);
//            case C.TYPE_HLS:
//                return new HlsMediaSource(uri, mediaDataSourceFactory, mainHandler, null);
//            case C.TYPE_OTHER:
//                return new ExtractorMediaSource(uri, mediaDataSourceFactory, new DefaultExtractorsFactory(),
//                        mainHandler, null);
//            default: {
//                throw new IllegalStateException("Unsupported type: " + type);
//            }
//        }
//    }

    private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        return buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }

    public DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(getActivity(), bandwidthMeter,
                buildHttpDataSourceFactory(bandwidthMeter));
    }

    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(Util.getUserAgent(getActivity(), "ExoPlayerDemo"), bandwidthMeter);
    }

    @Override
    public void onStop() {
        super.onStop();

        if(player != null) {
            player.stop();
            player.seekTo(0);
        }
    }

    private void showFileChooser() {
        if(choice.equals("video")) {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO_REQUEST);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        }
    }

    //handling the image chooser activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            try {
                if(choice.equals("fotoBefore")) {
                    filePathBefore = data.getData();
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePathBefore);
                    fotoBefore.setImageBitmap(bitmap);
                } else {
                    filePathAfter = data.getData();
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePathAfter);
                    fotoAfter.setImageBitmap(bitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == PICK_VIDEO_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            filePathVideo = data.getData();
//            File videoFile = new File(filePathVideo.getPath());

//            String[] filePathColumn = {MediaStore.Images.Media.DATA};
//            Cursor cursor = context.getContentResolver().query(filePathVideo, filePathColumn, null, null, null);
//            cursor.moveToFirst();
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            String picturePath = cursor.getString(columnIndex);
//            cursor.close();

            bitmap = ThumbnailUtils.createVideoThumbnail(getPath(filePathVideo), MediaStore.Video.Thumbnails.MINI_KIND);
            videoView.setImageBitmap(bitmap);

//            bitmap = ThumbnailUtils.createVideoThumbnail(getPath(filePathVideo),
//                    MediaStore.Images.Thumbnails.MINI_KIND);

//            try {
//                bitmap = MediaStore.Video.Thumbnails.getThumbnail(getActivity().getContentResolver(), Long.parseLong(filePathVideo.getLastPathSegment()), 3, null);
//                videoView.setImageBitmap(bitmap);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

//            Uri selectedImageUri = data.getData();

            // OI FILE Manager
//            filePath = selectedImageUri.getPath();

            // MEDIA GALLERY
//            selectedImagePath = getPath(filePath);
//            if (getPath(filePath) != null) {

//                Intent intent = new Intent(HomeActivity.this,
//                        VideoplayAvtivity.class);
//                intent.putExtra("path", selectedImagePath);
//                startActivity(intent);

//                DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
//                MediaSource mediaSource = new ExtractorMediaSource(filePath,
//                        mediaDataSourceFactory, extractorsFactory, null, null);
//                player.prepare(mediaSource);
//            }

//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
//                fotoBefore.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(
                (choice.equals("video"))? MediaStore.Video.Media.EXTERNAL_CONTENT_URI:android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(getContext(), "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(getContext(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
}
