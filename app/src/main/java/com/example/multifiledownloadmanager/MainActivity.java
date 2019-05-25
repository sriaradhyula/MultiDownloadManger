package com.example.multifiledownloadmanager;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private View download_progress_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button downloadFiles = findViewById(R.id.bt_download_files);

        downloadFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MultiFileDownloadManager downloadManager = new MultiFileDownloadManager(getApplicationContext(),
                        download_progress_view);
                downloadManager.downloadFiles();
                createAlertDialog();
            }
        });

    }

    private void createAlertDialog() {
        final AlertDialog download_progress_dialog = new android.support.v7.app.AlertDialog.Builder(
                MainActivity.this).create();
        LayoutInflater download_progress_factory = LayoutInflater.from(MainActivity.this);
        download_progress_view = download_progress_factory.inflate(R.layout.fragment_download_progress,
                null);

        Button bt_download_progress_dismiss = download_progress_view.findViewById(
                R.id.bt_download_progress_dismiss);
        bt_download_progress_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                download_progress_dialog.dismiss();
            }
        });

        download_progress_dialog.setView(download_progress_view);
        download_progress_dialog.setCancelable(false);
        download_progress_dialog.show();


        if(download_progress_view==null) {
            Log.i(TAG, "download_progress_view is NULL");
        } else {
            Log.i(TAG, "download_progress_view is not NULL");
        }
    }

}
