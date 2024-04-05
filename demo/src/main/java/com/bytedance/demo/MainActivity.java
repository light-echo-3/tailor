package com.bytedance.demo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.bytedance.tailor.Tailor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        new CrashHandler().init();

        TestTailorUtils.INSTANCE.checkDirectory(this);

        setContentView(R.layout.main);
        init();

        requestPermissionsI();
    }

    private void init() {
        findViewById(R.id.testDump).setOnClickListener(v -> {
            TestTailorUtils.INSTANCE.tailor_for_file();
            TestTailorUtils.INSTANCE.tailor_for_hook();
        });
        findViewById(R.id.testOOM).setOnClickListener(v -> {
            List<String> list = new ArrayList<>();
            int i = 0;
            while (true) {
                list.add("test---" + i++);
            }
        });
    }

    private void requestPermissionsI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, 101);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            TestTailorUtils.INSTANCE.checkDirectory(this);
        }
    }







}