package com.example.multifiledownloadmanager;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.content.Context.DOWNLOAD_SERVICE;
import static com.example.multifiledownloadmanager.Constants.files;

public class MultiFileDownloadManager {
    private static final String TAG = "MultiDownloadManager";
    private Context ctx;
    private View download_progress_view;
    private ArrayList<Long> downloadIDList;
    private  HashMap<Long, DownloadEachFile> downloadEachFileHashMap;
    private HashMap<Long, Boolean> eachFileDownloadStatus;
    private AlertDialog download_progress_dialog;

    public MultiFileDownloadManager(Context inCtx) {
        ctx = inCtx;
        downloadIDList = new ArrayList<>();
        downloadEachFileHashMap = new HashMap<>();
        eachFileDownloadStatus = new HashMap<>();
    }

    public void startDownload() {
        createAlertDialog();
        downloadFiles();
    }

    private void createAlertDialog() {
        download_progress_dialog = new android.support.v7.app.AlertDialog.Builder(
                ctx).create();
        LayoutInflater download_progress_factory = LayoutInflater.from(ctx);
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

    private void downloadFiles() {
        Iterator it = files.entrySet().iterator();
        String file_name;
        String file_url;
        int current_row_count = 0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            file_name = (String) pair.getKey();
            file_url = (String) pair.getValue();
            Log.v(TAG, file_name + " : " + file_url);

            //Download Each File
            DownloadEachFile downloadEachFile = new DownloadEachFile(ctx, download_progress_view,
                    file_name, current_row_count, "test", "test");
            long downloadID = downloadEachFile.createEachFileDownload(file_name, file_url);
            downloadIDList.add(downloadID);
            downloadEachFileHashMap.put(downloadID, downloadEachFile);
            eachFileDownloadStatus.put(downloadID, Boolean.FALSE);

            // avoids a ConcurrentModificationException
            it.remove();
            current_row_count ++;
        }

        IntentFilter filter = new IntentFilter(android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        ctx.registerReceiver(downloadReceiver, filter);
        // Make a delay of 3 seconds so that next toast (Music Status) will not merge with this one.
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!isEverythingDownloaded()) {
                    for (int i=0; i<downloadIDList.size();i++) {
                        try {
                            long current_download_id = downloadIDList.get(i);
                            boolean current_download_status = eachFileDownloadStatus.get(current_download_id);
                            if (!current_download_status) {
                                checkEachDownloadStatus(current_download_id);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    handler.postDelayed(this, 1000);
                } else {
                    //download_progress_dialog.dismiss();
                }
            }
        }, 1000);
    }

    private boolean isEverythingDownloaded() {
        Iterator it = eachFileDownloadStatus.entrySet().iterator();
        boolean isDownloadComplete = true;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Long localDownloadID = (Long) pair.getKey();
            Boolean localDownloadStatus = (Boolean) pair.getValue();
            Log.d(TAG, localDownloadID + " = " + localDownloadStatus);
            isDownloadComplete = isDownloadComplete && localDownloadStatus.booleanValue();
            Log.d(TAG, "isDownloadComplete: " + isDownloadComplete);
        }
        return isDownloadComplete;
    }

    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            //check if the broadcast message is for our enqueued download
            long localDownloadID = intent.getLongExtra(android.app.DownloadManager.EXTRA_DOWNLOAD_ID,
                    -1);

            Log.d(TAG,"localDownloadID: " + localDownloadID);
            //eachFileDownloadStatus.put(localDownloadID, Boolean.TRUE);
        }
    };


    private void checkEachDownloadStatus(long localDownloadID) {

        android.app.DownloadManager downloadManager= (android.app.DownloadManager) ctx.getSystemService(
                DOWNLOAD_SERVICE);
        android.app.DownloadManager.Query downloadQuery = new android.app.DownloadManager.Query();
        //set the query filter to our previously Enqueued download
        downloadQuery.setFilterById(localDownloadID);

        //Query the download manager about downloads that have been requested.
        Cursor cursor = downloadManager.query(downloadQuery);
        if(cursor.moveToFirst()){
            downloadStatus(cursor, localDownloadID);
        }
    }

    private void downloadStatus(Cursor cursor, final long localDownloadID){

        //column for download  status
        int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
        int status = cursor.getInt(columnIndex);
        //column for reason code if the download failed or paused
        int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
        int reason = cursor.getInt(columnReason);
        //get the download filename
        String filename = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));

        int bytes_downloaded = cursor.getInt(cursor
                .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
        int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

        final int downloadProgress = (int) ((bytes_downloaded * 100l) / bytes_total);

        Log.i("DownloadStatus", "LocalID: " + localDownloadID +  " bytes_downloaded: " +
                bytes_downloaded + " bytes_total: " + bytes_total + " dl_progress: " +
                downloadProgress);

        Activity hostActivity = (Activity)ctx;
        hostActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DownloadEachFile downloadEachFile = downloadEachFileHashMap.get(localDownloadID);
                downloadEachFile.getProgressBar().setProgress(downloadProgress);
                if(downloadProgress==100) {
                    downloadEachFile.getProgressCompletedImageView().setVisibility(View.VISIBLE);
                    downloadEachFile.setDownloadComplete(true);
                    eachFileDownloadStatus.put(localDownloadID, Boolean.TRUE);
                }
            }
        });


        String statusText = "";
        String reasonText = "";

        switch(status){
            case DownloadManager.STATUS_FAILED:
                statusText = "STATUS_FAILED";
                switch(reason){
                    case DownloadManager.ERROR_CANNOT_RESUME:
                        reasonText = "ERROR_CANNOT_RESUME";
                        break;
                    case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                        reasonText = "ERROR_DEVICE_NOT_FOUND";
                        break;
                    case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                        reasonText = "ERROR_FILE_ALREADY_EXISTS";
                        break;
                    case DownloadManager.ERROR_FILE_ERROR:
                        reasonText = "ERROR_FILE_ERROR";
                        break;
                    case DownloadManager.ERROR_HTTP_DATA_ERROR:
                        reasonText = "ERROR_HTTP_DATA_ERROR";
                        break;
                    case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                        reasonText = "ERROR_INSUFFICIENT_SPACE";
                        break;
                    case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                        reasonText = "ERROR_TOO_MANY_REDIRECTS";
                        break;
                    case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                        reasonText = "ERROR_UNHANDLED_HTTP_CODE";
                        break;
                    case DownloadManager.ERROR_UNKNOWN:
                        reasonText = "ERROR_UNKNOWN";
                        break;
                }
                break;
            case DownloadManager.STATUS_PAUSED:
                statusText = "STATUS_PAUSED";
                switch(reason){
                    case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                        reasonText = "PAUSED_QUEUED_FOR_WIFI";
                        break;
                    case DownloadManager.PAUSED_UNKNOWN:
                        reasonText = "PAUSED_UNKNOWN";
                        break;
                    case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                        reasonText = "PAUSED_WAITING_FOR_NETWORK";
                        break;
                    case DownloadManager.PAUSED_WAITING_TO_RETRY:
                        reasonText = "PAUSED_WAITING_TO_RETRY";
                        break;
                }
                break;
            case DownloadManager.STATUS_PENDING:
                statusText = "STATUS_PENDING";
                break;
            case DownloadManager.STATUS_RUNNING:
                statusText = "STATUS_RUNNING";

                break;
            case DownloadManager.STATUS_SUCCESSFUL:
                statusText = "STATUS_SUCCESSFUL";
                reasonText = "Filename:\n" + filename;
                break;
        }
        cursor.close();

    }
}
