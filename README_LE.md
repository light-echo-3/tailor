# 使用Tailor检测oom(可用于线上检测)

## 发生oom异常时，进行捕获。

实现类：com.bytedance.demo.CrashHandler
    
```java
    public class CrashHandler implements Thread.UncaughtExceptionHandler {

    ...

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        // 捕获oom异常，通过 Tailor 获取快照
        if (e instanceof java.lang.OutOfMemoryError) {
            ...
            showAlertDialog(t,e);
        } else {
            mDefaultHandler.uncaughtException(t, e);
        }
    }

    /***
     * 提示用户上报异常
     * 因为dump堆比较耗时，会冻结app，提示用户，让用户有心理准备
     */
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
```

com.bytedance.demo.TestTailorUtils#tailor_for_hook

```java
public static void tailor_for_hook(String fileName) {
    String target = DIRECTORY + "/" + fileName;
    try {
        long t = System.currentTimeMillis();
        System.err.println(">>>>>>>> tailor_for_hook: Tailor.dumpHprofData-begin:fileName=" + fileName);
        Tailor.dumpHprofData(target, true);//这里要开启压缩，https://github.com/bytedance/tailor/issues/14
        System.err.println(">>>>>>>> tailor_for_hook: Tailor.dumpHprofData-end:fileName=" + fileName + ",duration=" + (System.currentTimeMillis() - t));
    } catch (Exception e) {
        e.printStackTrace();
    }
}

```











---
```java
```