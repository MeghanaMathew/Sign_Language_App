package com.example.mc_sign_app;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class UploadActivity extends AppCompatActivity {

    private static final int VIDEO_CAPTURE = 101;
    VideoView videoView;
    Button record;
    Button upload;
    String gesture;
    String filepath;
    String buildName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        int counter= ((MyApplication) this.getApplication()).getCounter();
        videoView = (VideoView) findViewById(R.id.videoView2);
        record = (Button) findViewById(R.id.button2);
        upload = (Button) findViewById(R.id.button4);
        upload.setEnabled(false);

        //intent values
        gesture = getIntent().getStringExtra("gesture");
        buildName=gesture.toUpperCase()+"_Practice_"+counter+"_MEGHANAMATHEW";
        ((MyApplication) this.getApplication()).setCounter(++counter);

        //System.out.println("File name >>"+buildName);
        //Toast.makeText(getApplicationContext(), gesture, Toast.LENGTH_LONG).show();

        if (!hasCamera()) {
            record.setEnabled(false);
            Toast.makeText(getApplicationContext(), "Cannot Detect Camera", Toast.LENGTH_LONG).show();
        }
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startRecording();
                //System.out.println("Recording Successful");
                upload.setEnabled(true);
            }
        });


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UploadTask up1 = new UploadTask();
                //Toast.makeText(getApplicationContext(), "Starting to Upload", Toast.LENGTH_LONG).show();
                up1.execute();
                Toast.makeText(getApplicationContext(), "Upload Complete", Toast.LENGTH_LONG).show();

                Intent mainIntent=new Intent(UploadActivity.this,MainActivity.class);
                startActivity(mainIntent);
            }
        });
    }

    private boolean hasCamera() {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            return true;
        } else {
            return false;
        }
    }

    public void startRecording() {

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);
        startActivityForResult(intent, VIDEO_CAPTURE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == VIDEO_CAPTURE && resultCode == RESULT_OK) {

            Uri videoUri = intent.getData();
            videoView.setVideoURI(videoUri);
            filepath = getRealPathFromURI(videoUri);
            //System.out.println("the uri: "+videoUri+"--the path:"+filepath);
            Toast.makeText(getApplicationContext(), "Stored at:"+filepath, Toast.LENGTH_LONG).show();
            videoView.start();
        }
    }

    public String getRealPathFromURI(Uri uri) {

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int id = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(id);
    }

    public class UploadTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {

            String UPLOAD_URL = "http://192.168.0.32/videoUploads/upload.php";
            int serverResponseCode=0;
            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;

            File sourceFile = new File(filepath);
            if (!sourceFile.isFile()) {
                return null;
            }

            try {
                //System.out.println("Connecting with server");
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(UPLOAD_URL);
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"newname\""+lineEnd);
                dos.writeBytes("Content-Type: text/plain; charset=UTF-8"+lineEnd+lineEnd);
                dos.writeBytes(buildName+lineEnd);
                dos.writeBytes(lineEnd);

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"myFile\";filename="+sourceFile.getAbsolutePath()+ lineEnd);
                dos.writeBytes(lineEnd);

                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = conn.getResponseCode();

                fileInputStream.close();
                dos.flush();
                dos.close();
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (serverResponseCode == 200) {
                StringBuilder sb = new StringBuilder();
                try {
                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn
                            .getInputStream()));
                    String line;
                    while ((line = rd.readLine()) != null) {
                        sb.append(line);
                    }
                    rd.close();
                } catch (IOException ioex) {
                }
                System.out.println("Values>>"+sb.toString());
                return sb.toString();
            }else {
                return "Could not upload";
            }
        }
    }

}

