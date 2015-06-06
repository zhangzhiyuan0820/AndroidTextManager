package com.androidtextmanager.tk.text.utils;

import android.app.Activity;
import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URI;
import java.util.Enumeration;
import java.util.UUID;

/**
 * Created by TK on 2015/6/5.
 */
public class AndroidUtil {
    private static final String TAG = "AndroidUtil";


    public static String getAppVersion(Context context) {
        if (null == context) {
            return null;
        } else {
            PackageManager packageManager = context.getPackageManager();

            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), Context.MODE_PRIVATE);
                if (packageInfo != null) {
                    return packageInfo.versionName;
                }

            } catch (PackageManager.NameNotFoundException e) {
                Logutil.e(TAG, e.getMessage(), e);
            }
        }
        return null;
    }

    public static boolean compareVersion(String newVersion, String curVersoin) {
        if (!TextUtils.isEmpty(newVersion) && !TextUtils.isEmpty(curVersoin)) {
            String[] newVersionArray = newVersion.split(".");
            String[] curVersoinArray = curVersoin.split(".");

            for (int i = 0; i < newVersionArray.length; i++) {
                // 若当前版本的长度没有新的长，则表示新版本比当前版本大

                if (i < curVersoinArray.length) {
                    try {
                        int newVersionTemp = Integer.parseInt(newVersionArray[i]);
                        int curVersionTemp = Integer.parseInt(newVersionArray[i]);
                        if (newVersionTemp != curVersionTemp) {
                            return newVersionTemp > curVersionTemp;
                        }
                    } catch (Exception e) {
                        Logutil.e(TAG, e.getMessage(), e);
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getDeviceId(Context context) {
        String deviceId = "";
        if (context != null) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (!TextUtils.isEmpty(tm.getDeviceId())) {
                deviceId = tm.getDeviceId();
            }
        }
        return deviceId;
    }

    public static String getUUID(Context context) {
        if (null == context) {
            return null;
        }
        String uuid = null;
        String SimSeriaNumber = "";
        String androidId = "";
        String deviceId = getDeviceId(context);

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        if (tm != null) {
            SimSeriaNumber = tm.getSimSerialNumber();
        }

        if (!TextUtils.isEmpty(deviceId) || TextUtils.isEmpty(androidId) || TextUtils.isEmpty(SimSeriaNumber)) {
            uuid = deviceId + androidId + SimSeriaNumber + Utility.generateString(10);
        } else {
            uuid = md5(uuid);
        }
        if (TextUtils.isEmpty(uuid)) {
            uuid = Utility.generateString(10);
        }
        return uuid;

    }

    public static String md5(String value) {
        return value;
    }


    public static boolean isNetworkConnected(Context context) {
        if (null != context) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info != null) {
                return info.isAvailable();
            }

        }
        return false;
    }


    public static String getNetworkTypeName(Context context) {
        String netType = "";
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info != null && info.getExtraInfo() != null) {
                netType = info.getTypeName() + "_" + info.getExtraInfo().replace("\"", "");
            }
        }
        return netType;

    }


    public static String getMAC(Context context) {
        if (context == null) {
            return null;
        }
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String macAddress = info.getMacAddress();

        if (TextUtils.isEmpty(macAddress)) {
            macAddress = "";
        }
        return macAddress;
    }


    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    public static String getDeviceName() {
        return Build.PRODUCT;
    }

    public static String getDevicesModel() {
        return Build.MODEL;
    }

    public static String getOsVersioni() {
        return Build.VERSION.RELEASE;
    }

    public static String getSSID(Context context) {
        if (context == null) {
            return null;
        }
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        if (info != null && info.getSSID() != null) {
            return info.getSSID().replace("\"", "");
        }

        return null;
    }

    public static String getSimserialNumber(Context context) {

        if (context == null) {
            return null;
        }
        String simNumber = "";
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null) {
            return tm.getSimSerialNumber();
        }
        return null;
    }

    public static DisplayMetrics getDieplayMetrics(Context context) {
        if (context != null) {
            return context.getResources().getDisplayMetrics();
        }

        return null;
    }


    public static int getLac(Context context) {

        int lac = 0;
        if (context != null) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager != null) {

                GsmCellLocation location = (GsmCellLocation) telephonyManager.getCellLocation();
                if (location != null) {
                    lac = location.getLac();
                }

            }


        }
        return lac;

    }

    public static int gegtCid(Context context) {
        int cid = 0;
        if (context != null) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager != null) {

                GsmCellLocation location = (GsmCellLocation) telephonyManager.getCellLocation();
                if (location != null) {
                    cid = location.getCid();
                }

            }


        }
        return cid;

    }

    public static void sendMsg(Activity activity, String msg, String number) {

        if (activity == null || TextUtils.isEmpty(msg)) {
            return;
        }

        Uri uri = Uri.parse("smsto:" + number);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", msg);
        try {
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Logutil.e(TAG, e.getMessage(), e.getCause());
        }

    }

    public static boolean checkSDCardAvailabel() {
        return Environment.getExternalStorageDirectory().equals(Environment.MEDIA_MOUNTED);
    }

    public static String getSDPath() {
        if (checkSDCardAvailabel()) {
            return Environment.getExternalStorageDirectory().getPath();
        } else {
            return Environment.getDownloadCacheDirectory().getPath();
        }

    }

    public static String getApplicationMetaDate(Context context, String key) {
        ApplicationInfo info;
        try {
            info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return info.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            Logutil.e(TAG, e.getMessage(), e);
        }
        return null;

    }


    public static String getIp() {

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {

                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }

                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "127.0.0.1";
    }

    public static String getOperateName(Context context) {

        if (context == null) {
            return null;
        }
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String op = tm.getSimOperatorName();
        if (op != null) {
            if ("46000".equals(op) || "46002".equals(op)) {
                return "中国移动";
            } else if ("46001".equals(op)) {
                return "中国联通";
            } else if ("46003".equals(op)) {
                return "中国移动";
            } else {
                return tm.getSimOperatorName();
            }
        }
        return null;


    }


}




















