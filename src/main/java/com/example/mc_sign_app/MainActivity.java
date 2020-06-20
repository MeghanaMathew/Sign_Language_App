package com.example.mc_sign_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    Spinner spin;
    static Map<String,String> map;
    static {
        map = new HashMap<>();
        map.put("buy", "https://www.signingsavvy.com/media/mp4-ld/6/6442.mp4");
        map.put("house", "https://www.signingsavvy.com/media/mp4-ld/23/23234.mp4");
        map.put("fun", "https://www.signingsavvy.com/media/mp4-ld/22/22976.mp4");
        map.put("hope", "https://www.signingsavvy.com/media/mp4-ld/22/22197.mp4");
        map.put("arrive", "https://www.signingsavvy.com/media/mp4-ld/26/26971.mp4");
        map.put("really", "https://www.signingsavvy.com/media/mp4-ld/24/24977.mp4");
        map.put("read", "https://www.signingsavvy.com/media/mp4-ld/7/7042.mp4");
        map.put("lip", "https://www.signingsavvy.com/media/mp4-ld/26/26085.mp4");
        map.put("mouth", "https://www.signingsavvy.com/media/mp4-ld/22/22188.mp4");
        map.put("some", "https://www.signingsavvy.com/media/mp4-ld/23/23931.mp4");
        map.put("communicate", "https://www.signingsavvy.com/media/mp4-ld/22/22897.mp4");
        map.put("write", "https://www.signingsavvy.com/media/mp4-ld/27/27923.mp4");
        map.put("create", "https://www.signingsavvy.com/media/mp4-ld/22/22337.mp4");
        map.put("pretend", "https://www.signingsavvy.com/media/mp4-ld/25/25901.mp4");
        map.put("sister", "https://www.signingsavvy.com/media/mp4-ld/21/21587.mp4");
        map.put("man", "https://www.signingsavvy.com/media/mp4-ld/21/21568.mp4");
        map.put("one", "https://www.signingsavvy.com/media/mp4-ld/26/26492.mp4");
        map.put("drive", "https://www.signingsavvy.com/media/mp4-ld/23/23918.mp4");
        map.put("perfect", "https://www.signingsavvy.com/media/mp4-ld/24/24791.mp4");
        map.put("mother", "https://www.signingsavvy.com/media/mp4-ld/21/21571.mp4");
    }
    String[] gestureList = {"buy", "house", "fun", "hope", "arrive", "really", "read", "lip",
            "mouth", "some", "communicate", "write", "create", "pretend", "sister",
            "man", "one", "drive", "perfect", "mother"
    };


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissionCamera();
        checkPermissionStorage();
        spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(parent.getContext(),"selected",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter<String> spin_adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, gestureList);
        spin.setAdapter(spin_adapter);

        Button next = (Button) findViewById(R.id.button);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
                String gesture = String.valueOf(spin.getSelectedItem());

                intent.putExtra("gesture",gesture);
                intent.putExtra("videoUrl", map.getOrDefault(gesture,"buy"));
                startActivity(intent);
            }
        });
    }
    public void checkPermissionCamera()
    {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.CAMERA },
                            100);
        }
        else {
            Toast
                    .makeText(MainActivity.this,
                            "Permission already granted",
                            Toast.LENGTH_SHORT)
                    .show();
        }
    }
    public void checkPermissionStorage()
    {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { WRITE_EXTERNAL_STORAGE },
                    101);
        }
        else {
            Toast
                    .makeText(MainActivity.this,
                            "Permission already granted",
                            Toast.LENGTH_SHORT)
                    .show();
        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(MainActivity.this,
                        "Camera Permission Granted",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(MainActivity.this,
                        "Camera Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else if(requestCode==101){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(MainActivity.this,
                        "Storage Permission Granted",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(MainActivity.this,
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else{}
    }
}