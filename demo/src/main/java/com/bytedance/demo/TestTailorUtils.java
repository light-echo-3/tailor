package com.bytedance.demo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Debug;
import android.os.Environment;

import com.bytedance.tailor.Tailor;

import java.io.File;

public class TestTailorUtils {

    private static final String DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tailor";


    public static void checkDirectory(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                File dir = new File(DIRECTORY);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
            }
        }
    }

    public static void tailor_for_file() {
        try {
            String source = DIRECTORY + "/0.hprof";
            String target = DIRECTORY + "/1.hprof";
            long t = System.currentTimeMillis();
            Debug.dumpHprofData(source);
            System.err.println(">>>>>>>> tailor_for_file: Debug.dumpHprofData:duration=" + (System.currentTimeMillis() - t));
            t = System.currentTimeMillis();
            Tailor.cropHprofData(source, target, true);
            System.err.println(">>>>>>>> tailor_for_file: Tailor.cropHprofData:duration=" + (System.currentTimeMillis() - t));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void tailor_for_hook(String fileName) {
        String target = DIRECTORY + "/" + fileName;
        try {
            long t = System.currentTimeMillis();
            System.err.println(">>>>>>>> tailor_for_hook: Tailor.dumpHprofData-begin:fileName=" + fileName);
            Tailor.dumpHprofData(target, false);
            System.err.println(">>>>>>>> tailor_for_hook: Tailor.dumpHprofData-end:fileName=" + fileName + ",duration=" + (System.currentTimeMillis() - t));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
