package com.example.expansion.tempexpansion;

import android.app.Service;

import com.google.android.vending.expansion.downloader.impl.DownloaderService;

/**
 * Created by asher.ansari on 5/30/2017.
 */

public class SampleDownloadServices extends DownloaderService {

//    private static final String BASE64_PUBLIC_KEY = "Yeha hamari public key aye ge jo hum playstore se mile ge..";
    private static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAl0rC8XLNwlpDn0Ezc7LBF08eds9VLvbT6Bbl3B7h6uSqa703HXEv44LLBLAmBz2QM2iwzrf3WH7Tjp80II9rQxaVsUIukBHtnE5r9CBHn/pQl4OkZplmI3/Fvl2WPWyvXqVi2BungmW29FBqIWVQED0UepT1eXVr8X8sPAhcDwfI2WERIWHzm32oiz/a2dpMBs1DA/ERO1kWgtDKJuwjik9tHTeo2xfRBC+ytZQ5jqKFlmLqlDXpcDCUXVXhV2idtujV9cPD1PdEgElPTbqro7LQdrsMm4hn0olPK2Iy6hhdzO/Tll/viH03GtUkMOjpFA+OAVsA+b+oM1MfHiHTbwIDAQAB";

    public static final byte[] SALT = new byte[]{1, 42, -12, -1, 54, 98,
            -100, -12, 43, 2, -8, -4, 9, 5, -106, -107, -33, 45, -1, 84
    };

    @Override
    public String getPublicKey() {
        return BASE64_PUBLIC_KEY;
    }

    @Override
    public byte[] getSALT() {
        return SALT;
    }

    @Override
    public String getAlarmReceiverClassName() {
        return SampleAlarmReciever.class.getSimpleName();
    }
}
