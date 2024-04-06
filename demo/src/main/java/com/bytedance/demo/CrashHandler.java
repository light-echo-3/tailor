package com.bytedance.demo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "CrashHandler";

    private Thread.UncaughtExceptionHandler mDefaultHandler;

    public void init() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为系统默认的
        Thread.setDefaultUncaughtExceptionHandler(this);
    }


    @Override
    public void uncaughtException(Thread t, Throwable e) {
        //回调函数，处理异常出现后的情况
        // 在异常回调里通过 Tailor 获取快照
        if (e instanceof java.lang.OutOfMemoryError) {
//            TestTailorUtils.debugDumpHprofData();
//            TestTailorUtils.tailor_for_hook("mini.hprof");
//            System.err.println(">>>>>>>> tailor--uncaughtException-kill-process");

            showAlertDialog(t,e);
        } else {
            mDefaultHandler.uncaughtException(t, e);
        }
    }

    private void showAlertDialog(Thread t, Throwable e) {
        Log.w(TAG, ">>>>>>>> showAlertDialog: ");
        new Thread() {
            @Override
            public void run() {
                Looper.prepare(); // 创建一个新的 Looper
                Handler handler = new Handler(Looper.myLooper());
                AlertDialog.Builder builder = new AlertDialog.Builder(MyApplication.topActivity);
                builder.setTitle("是否上报？")
                        .setMessage("app发生未知错误，是否上报官方？")
                        .setPositiveButton("上报", (dialog, which) -> {
                            TestTailorUtils.tailor_for_hook("mini.hprof");
                            Toast.makeText(MyApplication.application, "上报成功", Toast.LENGTH_SHORT).show();
                            handler.postDelayed(() -> {
                                mDefaultHandler.uncaughtException(t, e); // 退出应用
                            },3000);
                        })
                        .setNegativeButton("取消", (dialog, which) -> {
                            mDefaultHandler.uncaughtException(t, e); // 退出应用
                        })
                        .setCancelable(false); // 使对话框不可取消
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                Looper.loop(); // 进入消息循环
            }
        }.start();
    }
}