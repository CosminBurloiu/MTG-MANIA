package com.example.android.mtg_mania.videos;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.mtg_mania.MTGMApplicationContext;
import com.example.android.mtg_mania.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class VideosActivity extends AppCompatActivity {

    protected static VideoView videoView;

    private final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 1;

    private RecyclerView items;
    private RecyclerView.Adapter adapter;

    // Video file uri used in referencing the video that will be played in the videoView.
    private Uri currentVideo = null;
    private String currentVideoString = null;
    private ArrayList<String> sharedPreferencesVideosList = new ArrayList<String>();
    private Integer lastFrame = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        videoView = findViewById(R.id.videoView);

        isFullscreen();

        deserializeVideoList();

        // Setup current video for playback
        // First get the selected video
        Uri currentVideo = Uri.parse(PreferenceManager.getDefaultSharedPreferences(this).getString("selectedVideo", ""));

        // Now get the current frame position of the video
        String currentPositionString = PreferenceManager.getDefaultSharedPreferences(this).getString("selectedVideoCurrentFrame", "");
        Integer currentPosition = 0;
        if (currentPositionString != "") {
            currentPosition = Integer.parseInt(currentPositionString);
        }
        setupMediaController(currentVideo, currentPosition);

        // Prepare the items for the RecyclerView layout
        getVideosList();
    }

    private void setupMediaController(Uri currentVideo, Integer currentPosition) {
        videoView.setVideoURI(currentVideo);

        MediaController mediaController = new FullScreenMediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        // Skipping to the last frame the user has seen. If video wasn't played at all, then seek to the first frame.
        videoView.seekTo(currentPosition);
    }

    private void isFullscreen() {
        String fullScreen = getIntent().getStringExtra("fullScreenInd");
        if ("y".equals(fullScreen)) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getSupportActionBar().hide();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        videoView.pause();
    }

    private ArrayList<VideoItem> initVideosList() {
        ArrayList<VideoItem> displayList = new ArrayList<>();
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        for (int i = 0; i < sharedPreferencesVideosList.size(); i++) {

            String value = sharedPreferencesVideosList.get(i);

        // if (file.exists()) {

            // use one of overloaded setDataSource() functions to set your data source
            retriever.setDataSource(this, Uri.parse(value));

            Bitmap currentThumbnail = BitmapFactory.decodeResource(getResources(), R.drawable.videoplaceholder);

            Long rawDuration = Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) / 1000;

            // Get the video details
            String currentTitle = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            String currentPlaytime = calculateTime(rawDuration);
            Uri currentUri = Uri.parse(value);

            displayList.add(new VideoItem(currentTitle, currentPlaytime, currentUri, currentThumbnail));
        }

        retriever.release();
        // We give the list to the VideoItemAdapter
        return displayList;
    }

    /**
     * Used in refreshing the videos list.
     */
    public void getVideosList() {
        // Initialize the list, fetching the video from the MediaStore
        ArrayList<VideoItem> items = initVideosList();

        // Use items to represent every note,RecyclerView and an adapter, create a dynamic layout that will contain every note in the database
        this.items = (RecyclerView) findViewById(R.id.items);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        this.items.setLayoutManager(mLayoutManager);
        adapter = new VideoItemAdapter(items);
        this.items.setAdapter(adapter);

        // We have received the latest version
    }

    private void addVideo() {
        // Create an intent with action ACTION_GET_CONTENT.
        Intent selectVideoIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Show video in the content browser.
        // Set selectVideoIntent.setType("*/*") to select all data
        // Intent for this action must set content type, otherwise you will encounter below exception : android.content.ActivityNotFoundException: No Activity found to handle Intent { act=android.intent.action.GET_CONTENT }
        selectVideoIntent.setType("video/*");
        selectVideoIntent.addCategory(Intent.CATEGORY_OPENABLE);

        // Start android get content activity ( this is an Android os built-in activity.) .
        startActivityForResult(selectVideoIntent, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
    }

    protected static void playVideo(View view) {

        ImageButton deleteButton = (ImageButton) view.findViewById(R.id.deleteButton);
        Uri currentUri = (Uri) deleteButton.getTag();
        videoView.setVideoURI(currentUri);

        // Refresh Current Video shared preferences
        PreferenceManager.getDefaultSharedPreferences(MTGMApplicationContext.getAppContext()).edit().putString("selectedVideo", currentUri.toString()).apply();

        videoView.start();
    }


    protected void deleteVideo(View view) {

        Uri currentUri = (Uri) view.getTag();

        sharedPreferencesVideosList.remove(currentUri.toString());

        //Refresh the videos list
        getVideosList();
    }

    /* This method will be invoked when startActivityForResult method complete in addVideo() method.
     * It is used to process activity result that is started by startActivityForResult method.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Identify activity by request code.
        if (requestCode == REQUEST_PERMISSION_READ_EXTERNAL_STORAGE && data != null) {
            currentVideo = data.getData();
            currentVideoString = "";

            if (currentVideo != null) {
                currentVideoString = currentVideo.toString();

                // Add to shared preferences
                sharedPreferencesVideosList.add(currentVideoString);
                PreferenceManager.getDefaultSharedPreferences(this).edit().putString("selectedVideo", currentVideoString).apply();
                PreferenceManager.getDefaultSharedPreferences(this).edit().putString("selectedVideoCurrentFrame", "0").apply();

                //Refresh the videos list
                getVideosList();

                //Play video after adding it
                videoView.setVideoURI(currentVideo);
                videoView.start();
            }
        }
    }

    private void deserializeVideoList() {

        String serializedVideosList = PreferenceManager.getDefaultSharedPreferences(MTGMApplicationContext.getAppContext()).getString("videosList", "");

        if ("" != serializedVideosList) {
            sharedPreferencesVideosList = new Gson().fromJson(serializedVideosList, ArrayList.class);
        }
    }

    private void serializeVideoList() {

        if (!sharedPreferencesVideosList.isEmpty()) {
            String json = new Gson().toJson(sharedPreferencesVideosList);

            PreferenceManager.getDefaultSharedPreferences(MTGMApplicationContext.getAppContext()).edit().putString("videosList", json).apply();
        }
    }

    /**
     * Functionality regarding toolbar menu.
     *
     * @param menu The object containing different video options.
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.videos_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addVideo: {

                requestExternalStorageReadPermission();

                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    // Permission checks
    private void requestExternalStorageReadPermission() {

        String[] desiredPermissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
        };

        if (ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED) {
            addVideo();
        } else {
            ActivityCompat.requestPermissions(this, desiredPermissions, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_READ_EXTERNAL_STORAGE) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        addVideo();
                    }
                }
            }
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        Log.d("nicusIdeea", "Switching screen orientation.");

        if (newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            findViewById(R.id.items).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.items).setVisibility(View.GONE);
        }

    }

    @Override
    protected void onPause() {
        lastFrame = videoView.getCurrentPosition();

        //Save the frame the video is on
        PreferenceManager.getDefaultSharedPreferences(MTGMApplicationContext.getAppContext()).edit().putString("selectedVideoCurrentFrame", lastFrame.toString()).apply();

        super.onPause();
    }

    @Override
    protected void onResume() {
        // Setup current video for playback
        // First get the selected video
        Uri currentVideo = Uri.parse(PreferenceManager.getDefaultSharedPreferences(this).getString("selectedVideo", ""));

        // Now get the current frame position of the video
        String currentPositionString = PreferenceManager.getDefaultSharedPreferences(this).getString("selectedVideoCurrentFrame", "");
        Integer currentPosition = 0;
        if (currentPositionString != "") {
            currentPosition = Integer.parseInt(currentPositionString);
        }
        setupMediaController(currentVideo, currentPosition);

        super.onResume();
    }

    @Override
    protected void onDestroy() {
        serializeVideoList();

        super.onDestroy();
    }

    // Helpers
    public static void showAllLocalVideos(Context context) {
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Video.VideoColumns.DATA, MediaStore.Video.VideoColumns._ID};
        Cursor c = context.getContentResolver().query(uri, projection, null, null, null);
        int vidsCount = 0;
        if (c != null) {
            vidsCount = c.getCount();
            while (c.moveToNext()) {
                Log.d("VIDEO_path", c.getString(0));
                Log.d("VIDEO_id", c.getString(1));
            }
            c.close();
        }
        Log.d("VIDEO", "Total count of videos: " + vidsCount);
    }

    public static String calculateTime(long seconds) {
        int day = (int) TimeUnit.SECONDS.toDays(seconds);
        long hour = TimeUnit.SECONDS.toHours(seconds) -
                TimeUnit.DAYS.toHours(day);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) -
                TimeUnit.DAYS.toMinutes(day) -
                TimeUnit.HOURS.toMinutes(hour);
        long second = TimeUnit.SECONDS.toSeconds(seconds) -
                TimeUnit.DAYS.toSeconds(day) -
                TimeUnit.HOURS.toSeconds(hour) -
                TimeUnit.MINUTES.toSeconds(minute);

        String duration = "";

        if (9 >= hour) {
            duration = "0" + hour + ":";
        } else {
            duration = hour + ":";
        }

        if (9 >= minute) {
            duration = duration + "0" + minute + ":";
        } else {
            duration = duration + minute + ":";
        }

        if (9 >= second) {
            duration = duration + "0" + second;
        } else {
            duration = duration + second;
        }

        return duration;
    }
}
