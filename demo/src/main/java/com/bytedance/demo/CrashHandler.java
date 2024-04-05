package com.bytedance.demo;

import com.bytedance.tailor.Tailor;

import java.io.IOException;

public class CrashHandler implements Thread.UncaughtExceptionHandler {

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
            TestTailorUtils.debugDumpHprofData();
            TestTailorUtils.tailor_for_hook("mini.hprof");
            System.err.println(">>>>>>>> tailor--uncaughtException-kill-process");
            mDefaultHandler.uncaughtException(t, e);
        } else {
            mDefaultHandler.uncaughtException(t, e);
        }
    }
}