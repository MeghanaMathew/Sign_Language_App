package com.example.mc_sign_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class DisplayActivity extends AppCompatActivity {

    VideoView video;
    ProgressDialog pd;
    Button practice;
    String gesture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        Intent intent= getIntent();
        String url=intent.getStringExtra("videoUrl");
        gesture=intent.getStringExtra("gesture");

        pd = new ProgressDialog(DisplayActivity.this);
        pd.setMessage("Buffering video please wait...");
        pd.show();

        video = (VideoView)findViewById(R.id.videoView);
        Uri uri = Uri.parse(url);
        video.setVideoURI(uri);

        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                pd.dismiss();
            }
        });
        video.start();
        video.setOnCompletionListener ( new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                video.start();
            }
        });

        practice=(Button) findViewById(R.id.button_practice);
        practice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayActivity.this, UploadActivity.class);
                intent.putExtra("gesture",gesture);
                startActivity(intent);
            }
        });

    }
}