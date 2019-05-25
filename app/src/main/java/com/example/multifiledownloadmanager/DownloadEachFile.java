package com.example.multifiledownloadmanager;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.File;

import static android.content.Context.DOWNLOAD_SERVICE;

public class DownloadEachFile {
    private static final String TAG = "DownloadEachFile";
    private Context ctx;
    private View download_progress_view;
    ProgressDialog progressDialog;
    int mDownloadProgress=0;

    public DownloadEachFile(Context inCtx, View in_download_progress_view) {
        ctx = inCtx;
        download_progress_view = in_download_progress_view;
    }

    public long createEachFileDownload(String file_name, String file_url) {
        File file = new File(ctx.getExternalFilesDir(null), file_name);
        Uri fileUri = Uri.parse(file_url);

       /*
       Create a MultiFileDownloadManager.Request with all the information necessary to start the download
        */
        android.app.DownloadManager.Request request=new android.app.DownloadManager.Request(fileUri)
                .setTitle("Lesson Content")// Title of the Download Notification
                .setDescription("Downloading")// Description of the Download Notification
                .setNotificationVisibility(android.app.DownloadManager.Request.VISIBILITY_VISIBLE) // Visibility of the download Notification
                .setDestinationUri(Uri.fromFile(file))// Uri of the destination file
                .setRequiresCharging(false)// Set if charging is required to begin the download
                .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                .setAllowedOverRoaming(true);// Set if download is allowed on roaming network

        android.app.DownloadManager downloadManager= (android.app.DownloadManager) ctx.getSystemService(DOWNLOAD_SERVICE);
        long downloadID = downloadManager.enqueue(request);// enqueue puts the download request in the queue.

        return downloadID;
    }

    private void setupProgressDialog(String fileTitle, int current_row_count,
                                     String dialogTitle, String dialogMsg) {
        progressDialog = new ProgressDialog(ctx);
        try {
            Log.i(TAG, "Adding Progress Bar to Alert");
            Context local_ctx = download_progress_view.getContext();
            ProgressBar progressBar = new ProgressBar(local_ctx, null,
                    android.R.attr.progressBarStyleHorizontal);
            ImageView progressCompletedImageView = new ImageView(local_ctx);
            if(fileTitle==null) {
                GridButtonLayout.addProgressBar(download_progress_view,
                        "Download " + current_row_count,
                        progressBar,
                        progressCompletedImageView, current_row_count);
            } else {
                GridButtonLayout.addProgressBar(download_progress_view, fileTitle,
                        progressBar, progressCompletedImageView, current_row_count);
            }
            progressDialog.setTitle(dialogTitle);
            progressDialog.setMessage(dialogMsg);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(false);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }
}
