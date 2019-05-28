package com.example.multifiledownloadmanager;

import android.util.Log;

import java.util.HashMap;

public class MultiFileDownloadStatus {
    private final String TAG = "MultiFileDownloadStatus";
    private static MultiFileDownloadStatus mMultiFileDownloadStatusSingletonInstance;

    HashMap<Long, String> filesDownloadedPath;
    HashMap<Long, String> filesDownloadedSize;
    HashMap<Long, String> filesDownloadedStatus;


    public static synchronized MultiFileDownloadStatus getInstance() {
        if(mMultiFileDownloadStatusSingletonInstance==null) {
            mMultiFileDownloadStatusSingletonInstance = new MultiFileDownloadStatus();
        }
        return mMultiFileDownloadStatusSingletonInstance;
    }

    private MultiFileDownloadStatus() {
        filesDownloadedPath = new HashMap<>();
        filesDownloadedStatus = new HashMap<>();
        filesDownloadedSize = new HashMap<>();
    }

    public void addNewDownloadedFilePath(Long downloadID, String filePath) {
        filesDownloadedPath.put(downloadID, filePath);
    }

    public void addNewDownloadedFileStatus(Long downloadID, String fileDownloadStatus) {
        filesDownloadedStatus.put(downloadID, fileDownloadStatus);
    }

    public void addNewDownloadedFileSize(Long downloadID, String fileDownloadSize) {
        filesDownloadedStatus.put(downloadID, fileDownloadSize);
    }

    public void resetDownloadedFiles() {
        filesDownloadedPath.clear();
        filesDownloadedStatus.clear();
        filesDownloadedSize.clear();
    }

    public HashMap<Long,String> getFilesDownloadedPaths() {
        return filesDownloadedPath;
    }

    public HashMap<Long, String> getFilesDownloadedStatus() {
        return filesDownloadedStatus;
    }

    public HashMap<Long, String> getFilesDownloadedSize() {
        return filesDownloadedSize;
    }

    public void printFilesDownloaded() {
        Log.i(TAG, "filesDownloadedPath: " + filesDownloadedPath.toString());

        Log.i(TAG, "filesDownloadedStatus: " + filesDownloadedStatus.toString());

        Log.i(TAG, "filesDownloadedSize: " + filesDownloadedSize.toString());
    }
}
