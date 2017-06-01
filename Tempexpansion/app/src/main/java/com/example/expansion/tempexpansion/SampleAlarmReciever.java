package com.example.expansion.tempexpansion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

/**
 * Created by asher.ansari on 5/30/2017.
 */

import com.android.vending.expansion.downloader.*;
import com.google.android.vending.expansion.downloader.DownloaderClientMarshaller;

public class SampleAlarmReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            DownloaderClientMarshaller.startDownloadServiceIfRequired(context,intent,SampleAlarmReciever.class);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }
}
