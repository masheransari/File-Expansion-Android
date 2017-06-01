package com.example.expansion.tempexpansion;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.vending.expansion.zipfile.APKExpansionSupport;
import com.android.vending.expansion.zipfile.ZipResourceFile;
import com.crashlytics.android.Crashlytics;
import com.google.android.vending.expansion.downloader.DownloadProgressInfo;
import com.google.android.vending.expansion.downloader.DownloaderClientMarshaller;
import com.google.android.vending.expansion.downloader.Helpers;
import com.google.android.vending.expansion.downloader.IDownloaderClient;
import com.google.android.vending.expansion.downloader.IDownloaderService;
import com.google.android.vending.expansion.downloader.IStub;
import com.google.android.vending.expansion.downloader.impl.DownloaderService;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements IDownloaderClient{

    TextView title1, title2, Loading;
    private IStub mDownloaderClientStub;
    private ProgressDialog progressDialog;
    private IDownloaderService mRemoteService;
    private static final String LOG_TAG = "MainActivity.class";

    private final static String EXP_PATH = "/Android/obb/";
    int DURATION = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        title1 = (TextView) findViewById(R.id.titOne);
        title2 = (TextView) findViewById(R.id.titTwo);
        Loading = (TextView) findViewById(R.id.load);

        if (!expansionFilesDeivered()) {
            Intent launchIntent = this.getIntent();

            Intent notifierIntent = new Intent(MainActivity.this, MainActivity.this.getClass());
            notifierIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            notifierIntent.setAction(launchIntent.getAction());
            if (launchIntent.getCategories() != null) {
                for (String catrgory : launchIntent.getCategories()) {
                    notifierIntent.addCategory(catrgory);
                    Log.e(LOG_TAG, catrgory);
                }
            }
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notifierIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Log.v(LOG_TAG, "Start the download service");
            int startResult = 0;
            try {
                startResult = DownloaderClientMarshaller.startDownloadServiceIfRequired(this,
                        pendingIntent, DownloaderService.class);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            if (startResult != DownloaderClientMarshaller.NO_DOWNLOAD_REQUIRED) {
                Log.v(LOG_TAG, "initialize activity to show progress");
                mDownloaderClientStub = DownloaderClientMarshaller.CreateStub(this, DownloaderService.class);

                progressDialog = new ProgressDialog(this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setMessage("Downloading has been started");

                progressDialog.setCancelable(false);
                progressDialog.show();
                return;
            } else {
                Log.v(LOG_TAG, "No download required");
            }
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(MainActivity.this, AnotherActivity.class));
                    finish();
                }
            }, DURATION);
        }
    }

    private static final XAPKFile[] xAPKS = {
            new XAPKFile(true, 2, 52948873L),
    };

    @Override
    protected void onStart() {
        if (null != mDownloaderClientStub) {
            mDownloaderClientStub.connect(this);
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (null != mDownloaderClientStub) {
            mDownloaderClientStub.disconnect(this);
        }
        super.onStop();
    }


    //    private static final CopyTask.XAPKFile[] xApks = {new CopyTask.XAPKFile(true,
//            1,
//            19169333L),
//    };

//    private class CopyTask extends AsyncTask<Void, Void, Boolean> {
//
//        private ProgressDialog mProgressBar;
//        private Context context;
//
//        public CopyTask(Context context) {
//            this.context = context;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            if (context != null) {
//                mProgressBar = ProgressDialog.show(context, "", "onPre Execution", false);
//            }
//        }
//
//
//        @Override
//        protected Boolean doInBackground(Void... voids) {
////            return null;
//            for (XAPKFile xf : xApks) {
//                String fileName = Helpers.getExpansionAPKFileName(MainActivity.this,
//                        xf.mIsMain, xf.mFinalVersion);
//                if (!Helpers.doesFileExist(MainActivity.this, fileName, xf.mFileSize, false)) {
//                    return true;
//                }
//                fileName = Helpers.generateSaveFileName(MainActivity.this, fileName);
//
//                ZipResourceFile zrf;
//
//                try {
//                    zrf = APKExpansionSupport.getAPKExpansionZipFile(
//                            MainActivity.this, 1, 0
//                    );
//
//                    File path = new File(
//                            Environment.getExternalStorageDirectory()
//                                    + File.separator
//                                    + getString(R.string.app_name));
//
//                    if (!path.exists()) {
//                        File photoDirectory = new File(String.valueOf(path.getAbsoluteFile()));
//
//                        photoDirectory.mkdir();
//                    } else {
//                        if (path.isDirectory()) {
//                            String[] children = path.list();
//                            Log.v("MainActivity", "file length:" + children.length);
//                            for (int i = 0; i < children.length; i++) {
//                                new File(path, children[i]).delete();
//                            }
//                        }
//                    }
//
//                    for (ZipResourceFile.ZipEntryRO entry : zrf.getAllEntries()) {
//                        Log.v("MainAtivity", "name:" + entry.mFileName);
//                        DataInputStream is = new DataInputStream(
//                                zrf.getInputStream(entry.mFileName));
//                        // AssetFileDescriptor af =
//                        // zrf.getAssetFileDescriptor(entry.mFileName);
//                        long length = entry.mCompressedLength;
//                        byte[] buf = new byte[1024 * 256];
//                        Log.v("assetfilediscripter", "" + length);
//                        // FileOutputStream fos = new FileOutputStream("");
//
//                        // OutputStream stream = new BufferedOutputStream(new
//                        // FileOutputStream("/storage/sdcard0/Download/sample.mp3"));
//
//                        String[] bits = entry.mFileName.split("/");
//
//                        String lastOne = bits[bits.length - 1];
//
//                        Log.v("MainACtivity", "path:" + path + ":" + lastOne);
//
//                        OutputStream stream = new BufferedOutputStream(
//                                new FileOutputStream(path + "/"
//                                        + lastOne));
//                        int bufferSize = 1024;
//                        byte[] buffer = new byte[bufferSize];
//                        int len = 0;
//                        while ((len = is.read(buffer)) != -1) {
//                            stream.write(buffer, 0, len);
//                        }
//                        if (stream != null)
//                            stream.close();
//
//                        // break;
//
//
//                    }
////                    } catch (IOException e) {
////                    e.printStackTrace();
////                }
////            } catch (FileNotFoundException e) {
////                    e.printStackTrace();
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
//            return true;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean result) {
//            super.onPostExecute(result);
//            if (mProgressDialog != null  && mProgressDialog.isShowing()) {
//                mProgressDialog.dismiss();
//            }
//
//            if (result) {
//                finish();
//                Intent intent = new Intent(MainActivity.this,
//                        MainList.class);
//                startActivity(intent);
//            } else {
//                errorDialog(context, context.getString(R.string.copy_failed));
//            }
//        }
//
//    }
//    }

    @Override
    public void onServiceConnected(Messenger messageOne) {
        mRemoteService = (IDownloaderService) DownloaderClientMarshaller.CreateProxy(messageOne);
        mRemoteService.onClientUpdated(mDownloaderClientStub.getMessenger());
    }

    @Override
    public void onDownloadStateChanged(int newState) {
        Log.v(LOG_TAG, "DownloadStateChanged : " + getString(Helpers.getDownloaderStringResourceIDFromState(newState)));

        switch (newState) {
            case STATE_DOWNLOADING:
                Log.e(LOG_TAG, "Downloading....");
                break;
            case STATE_COMPLETED:
                progressDialog.setMessage("");
                progressDialog.dismiss();
                break;
            case STATE_FAILED_UNLICENSED:
                Log.e(LOG_TAG, "State_Failed_Unlisensed");
                break;
            case STATE_FAILED_FETCHING_URL:
                Log.e(LOG_TAG, "State_failed_fetching_Url");
                break;
            case STATE_FAILED_SDCARD_FULL:
                Log.e(LOG_TAG, "State_failed_sDCard_Full");
                break;
            case STATE_FAILED_CANCELED:
                Log.e(LOG_TAG, "State_Failed_cancelled");
                break;
            case STATE_FAILED:
                Log.e(LOG_TAG, "State_failed");
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("error");
                alert.setMessage("Download Failed..");
                alert.setNeutralButton("Close", null);
                alert.show();
                break;
        }
    }


    @Override
    public void onDownloadProgress(DownloadProgressInfo progress) {
        long precentatge = progress.mOverallProgress * 100 / progress.mOverallTotal;
        Log.e(LOG_TAG, "Downloading in progress: " + Long.toString(precentatge) + "%");
        progressDialog.setProgress((int) precentatge);
    }

    private static class XAPKFile {
        public final boolean mIsMain;
        public final int mFinalVersion;
        public final long mFileSize;

        public XAPKFile(boolean mIsMain, int mFinalVersion, long mFileSize) {
            this.mIsMain = mIsMain;
            this.mFinalVersion = mFinalVersion;
            this.mFileSize = mFileSize;
        }
    }

    boolean expansionFilesDeivered() {
        for (XAPKFile xf : xAPKS) {
            String fileName = Helpers.getExpansionAPKFileName(this, xf.mIsMain, xf.mFinalVersion);
            if (!Helpers.doesFileExist(this, fileName, xf.mFileSize, false)) {
                return false;
            }

        }
        return true;
    }
}
