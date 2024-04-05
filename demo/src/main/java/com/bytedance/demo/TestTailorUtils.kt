package com.bytedance.demo

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Debug
import android.os.Environment
import com.bytedance.tailor.Tailor
import java.io.File

object TestTailorUtils {

    var DIRECTORY = Environment.getExternalStorageDirectory().absolutePath + "/tailor"


    fun checkDirectory(context:Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                val dir = File(DIRECTORY)
                if (!dir.exists()) {
                    dir.mkdirs()
                }
            }
        }
    }

    fun tailor_for_file() {
        try {
            val source = "$DIRECTORY/0.hprof"
            val target = "$DIRECTORY/1.hprof"
            var t = System.currentTimeMillis()
            Debug.dumpHprofData(source)
            System.err.println(">>>>>>>> tailor_for_file: Debug.dumpHprofData:" + (System.currentTimeMillis() - t))
            t = System.currentTimeMillis()
            Tailor.cropHprofData(source, target, true)
            System.err.println(">>>>>>>> tailor_for_file: Tailor.cropHprofData:" + (System.currentTimeMillis() - t))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun tailor_for_hook() {
        val target = "$DIRECTORY/2.hprof"
        try {
            val t = System.currentTimeMillis()
            Tailor.dumpHprofData(target, false)
            System.err.println(">>>>>>>> tailor_for_hook: Tailor.dumpHprofData:" + (System.currentTimeMillis() - t))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}