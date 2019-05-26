package com.example.multifiledownloadmanager;

import android.app.DownloadManager;
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
    private ProgressBar progressBar;
    private String fileTitle;
    private int current_row_count;
    private String dialogTitle;
    private String dialogMsg;
    private boolean isDownloadComplete;
    private ImageView progressCompletedImageView;
    private ImageView fileInfoImageView;

    private int downloadProgress=0;

    public DownloadEachFile(Context inCtx, View in_download_progress_view,
                            String inFileTitle, int in_current_row_count, String inDialogTitle,
                            String inDialogMsg) {
        ctx = inCtx;
        download_progress_view = in_download_progress_view;
        fileTitle = inFileTitle;
        current_row_count = in_current_row_count;
        dialogTitle = inDialogTitle;
        dialogMsg = inDialogMsg;
        isDownloadComplete = false;
    }

    public long createEachFileDownload(String file_name, String file_url) {
        File file = new File(ctx.getExternalFilesDir(null), file_name);
        Uri fileUri = Uri.parse(file_url);

        final String description = "Downloading " + fileTitle;
        final int visibility = DownloadManager.Request.VISIBILITY_VISIBLE;

        // Create a Request with all the information necessary to start the download
        DownloadManager.Request request=new DownloadManager.Request(fileUri)
                .setTitle(fileTitle)                   // Title of the Download Notification
                .setDescription(description)           // Description of the Download Notification
                .setNotificationVisibility(visibility) // Visibility of the download Notification
                .setDestinationUri(Uri.fromFile(file)) // Uri of the destination file
                .setRequiresCharging(false)            // Set if charging is required to begin the download
                .setAllowedOverMetered(true)           // Set if download is allowed on Mobile network
                .setAllowedOverRoaming(true);          // Set if download is allowed on roaming network

        DownloadManager downloadManager= (DownloadManager) ctx.getSystemService(DOWNLOAD_SERVICE);
        long downloadID = downloadManager.enqueue(request);// enqueue puts the download request in the queue.

        setupProgressBar();
        return downloadID;
    }

    private void setupProgressBar() {
        try {
            Log.i(TAG, "Adding Progress Bar to Alert");
            Context local_ctx = download_progress_view.getContext();
            progressBar = new ProgressBar(local_ctx, null,
                    android.R.attr.progressBarStyleHorizontal);
            progressCompletedImageView = new ImageView(local_ctx);
            fileInfoImageView = new ImageView(local_ctx);
            GridButtonLayoutHelper.addProgressBar(download_progress_view, "test",
                    progressBar, progressCompletedImageView,fileInfoImageView,
                    current_row_count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public int getDownloadProgress() {
        return downloadProgress;
    }

    public void setDownloadProgress(int downloadProgress) {
        this.downloadProgress = downloadProgress;
    }

    public boolean isDownloadComplete() {
        return isDownloadComplete;
    }

    public void setDownloadComplete(boolean downloadComplete) {
        isDownloadComplete = downloadComplete;
    }

    public ImageView getProgressCompletedImageView() {
        return progressCompletedImageView;
    }
}
