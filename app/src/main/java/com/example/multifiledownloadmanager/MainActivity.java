package com.example.multifiledownloadmanager;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button downloadFiles = findViewById(R.id.bt_download_files);

        downloadFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MultiFileDownloadManager downloadManager = new MultiFileDownloadManager(
                        MainActivity.this);

                downloadManager.startDownload();
            }
        });

        final Button downloadStatus = findViewById(R.id.bt_download_status);

        downloadStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MultiFileDownloadStatus.getInstance().printFilesDownloaded();
            }
        });

    }
}
