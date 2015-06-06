package com.androidtextmanager.tk.text.utils;

import android.content.Context;
import android.nfc.Tag;
import android.os.Environment;
import android.util.Log;
import android.util.TimeUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 * Created by TK on 2015/6/5.
 */
public class Logutil {

    private static final String TAG = "Logutil";
    private static final int MESSAGE_LENGTH_MAX = 256;
    private static final int mMaxArrayCount = 100;
    private static final int mMaxLogLength = 5 * 1024 * 1024;
    private static Object mLock = new Object();

    private static ArrayList<String> mLogArray = new ArrayList<String>();
    private static final String LOG_FILE_NAME = "log.txt";

    private static boolean mIsprintLog = true;
    private static Context mContext;

    public static void isprintLog(boolean printLog) {
        mIsprintLog = printLog;
    }

    public static void setContext(Context context) {
        mContext = context;
    }

    public static final int VERSBOE = 2;

    public static final int DEBUG = 3;

    public static final int INFO = 4;

    public static final int WARN = 5;

    public static final int ERROR = 6;

    public static void d(String tag, String msg) {
        if (null == msg) {
            return;
        }

        String finalTag = TAG;
        print(DEBUG, finalTag, msg);
        if (!mIsprintLog) {
            log(finalTag + "\t" + (msg.length() > MESSAGE_LENGTH_MAX ? msg.substring(MESSAGE_LENGTH_MAX) : msg));
        }
    }

    public static void i(String tag, String msg) {
        if (null == msg) {
            return;
        }

        String finalTag = TAG;
        print(INFO, finalTag, msg);
        if (!mIsprintLog) {
            log(finalTag + "\t" + (msg.length() > MESSAGE_LENGTH_MAX ? msg.substring(MESSAGE_LENGTH_MAX) : msg));
        }
    }

    public static void w(String tag, String msg) {
        if (null == msg) {
            return;
        }

        String finalTag = TAG;
        print(WARN, finalTag, msg);
        if (!mIsprintLog) {
            log(finalTag + "\t" + (msg.length() > MESSAGE_LENGTH_MAX ? msg.substring(MESSAGE_LENGTH_MAX) : msg));
        }
    }

    public static void e(String tag, String msg) {
        if (null == msg) {
            return;
        }

        String finalTag = TAG;
        print(ERROR, finalTag, msg);
        if (!mIsprintLog) {
            log(finalTag + "\t" + (msg.length() > MESSAGE_LENGTH_MAX ? msg.substring(MESSAGE_LENGTH_MAX) : msg));
        }
    }
    public static void e(String tag, String msg , Throwable t){
        if( null == msg){
            msg = "";
        }
        String finalTag = TAG + tag;
        print( ERROR,finalTag ,msg,t);
        if(mIsprintLog){
            e(finalTag ,t);
        }
    }
    public static void e(String tag, Throwable t){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        t.printStackTrace(pw);
        pw.flush();
        sw.flush();
        logAndCommit(tag + "\t" + sw.toString());

    }

    private static void logAndCommit(final String StrLog){
        new Thread( new Runnable() {
            @Override
            public void run() {
                 synchronized ( mLock){
                     mLogArray.add(TimeUtil.getNowTimeString() +"\t"+StrLog );
                     commit();
                     mLogArray.clear();
                 }
            }
        }).start();
    }


    public static void v(String tag, String msg) {
        if (null == msg) {
            return;
        }

        String finalTag = TAG;
        print(VERSBOE, finalTag, msg);
        if (!mIsprintLog) {
            log(finalTag + "\t" + (msg.length() > MESSAGE_LENGTH_MAX ? msg.substring(MESSAGE_LENGTH_MAX) : msg));
        }
    }

    private static void print(int prioroty, String tag, String msg) {

        android.util.Log.println(prioroty, tag, msg);
    }

    private static void print(int priority, String tag, String msg, Throwable tr) {
        print(priority, tag, msg);
        print(priority, tag, getStackTraceString(tr));
    }

    private static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        return sw.toString();

    }

    private static void log(final String strLog) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                synchronized (mLock) {
                    mLogArray.add(TimeUtil.getNowTimeString() + "\t" + strLog + "\r\n");
                    if (mLogArray.size() >= mMaxArrayCount) {
                        commit();
                        mLogArray.clear();
                    }
                }
            }
        }).start();


    }

    private static void commit() {
        if (mIsprintLog) {
            synchronized (mLock) {
                commitLog();
            }
        }

    }

    private static void commitLog() {
        if (mContext == null) {
            return;
        }
        FileOutputStream logFile = null;
        try {
            boolean isSDCradExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);

            if (isSDCradExist) {
                String path = mContext.getExternalCacheDir().getAbsolutePath();

                if (null == path) {
                    path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
                }
                File dir = new File(path);
                if (!dir.exists()) {
                    if (!dir.mkdir()) {
                        Logutil.e(TAG, "make dir failed : " + dir);
                        return;
                    }
                }
                File file = new File(path + "/" + LOG_FILE_NAME);
                if (file.length() >= mMaxLogLength) {
                    if (!file.delete()) {
                        Logutil.e(TAG, "delete file failed");
                        return;
                    }
                }
                if (!file.exists()) {
                    file.createNewFile();
                    logFile = new FileOutputStream(file, true);
                    for (String Str : mLogArray) {
                        logFile.write(Str.getBytes());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (logFile != null) {
                try {
                    logFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
