package com.davy.davy_wanandroid.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Camera;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.Settings;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.SettingService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * author: Davy
 * date: 18/10/13
 * desc：权限工具：适配5.0之下和之上
 */
public class PermissionUtils {

    /**
     * 打开应用详情页面intent
     */
    public static void gotoAppDetailSettingIntent(Context context) {
        // vivo 点击设置图标>加速白名单>我的app
        //      点击软件管理>软件管理权限>软件>我的app>信任该软件
        Intent appIntent = context.getPackageManager().getLaunchIntentForPackage("com.iqoo.secure");
        if (appIntent != null) {
            context.startActivity(appIntent);
            return;
        }
        // oppo 点击设置图标>应用权限管理>按应用程序管理>我的app>我信任该应用
        //      点击权限隐私>自启动管理>我的app
        appIntent = context.getPackageManager().getLaunchIntentForPackage("com.oppo.safe");
        if (appIntent != null) {
            context.startActivity(appIntent);
            return;
        }
        //其他
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        context.startActivity(intent);
    }

    /**
     * 系统设置界面
     *
     * @param context
     */
    public static void SystemConfig(Context context) {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        context.startActivity(intent);
    }

    /**
     * 判断是不是Android 6.0 以上的版本
     *
     * @return
     */
    public static boolean isAndroidM() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        }
        return false;
    }

    public interface PermissionCallBack {
        void onGranted();

        void onDenied();
    }

    public static void checkWritePermission(final Context context, final PermissionCallBack permissionCallBack) {
        //6.0及以上
        if (isAndroidM()) {
            //请求权限
            AndPermission.with(context)
                    .permission(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)
                    .onGranted(new Action() {
                        @Override
                        public void onAction(List<String> permissions) {
                            permissionCallBack.onGranted();
                        }
                    })
                    .onDenied(new Action() {
                        @Override
                        public void onAction(List<String> permissions) {
                            //一直被拒绝弹框给用户
                            if (AndPermission.hasAlwaysDeniedPermission(context, permissions)) {
                                LogHelper.i("AndPermission----onDenied--hasAlwaysDeniedPermission");
                                final SettingService settingService = AndPermission.permissionSetting(context);
                                //弹框给用户去设置页面
                                DialogUtils.showMyDialog(context,
                                        "权限提醒",
                                        "为保证您正常地使用此功能，需要到手机设置中开启您的存储权限，去开启。",
                                        "去开启",
                                        "取消",
                                        new DialogUtils.OnDialogClickListener() {
                                            @Override
                                            public void onConfirm() {
                                                try {
                                                    settingService.execute();
                                                } catch (Exception e) {
                                                    gotoAppDetailSettingIntent(context);
                                                }
                                            }

                                            @Override
                                            public void onCancel() {
                                                //取消
                                                permissionCallBack.onDenied();
                                            }
                                        });

                            } else {
                                LogHelper.i("AndPermission----onDenied");
                                permissionCallBack.onDenied();
                            }
                        }
                    })
                    .start();
        } else {
            if (isWritePermission()) {
                permissionCallBack.onGranted();
            } else {
                permissionCallBack.onDenied();
            }
        }
    }

    /**
     * 检查相机权限
     *
     * @param context
     * @param permissionCallBack
     */
    public static void checkCameraPermission(final Context context, final PermissionCallBack permissionCallBack) {
        //请求权限
        if (isAndroidM()) {
            AndPermission.with(context)
                    .permission(Permission.CAMERA)
                    .onGranted(new Action() {
                        @Override
                        public void onAction(List<String> permissions) {
                            permissionCallBack.onGranted();
                        }
                    })
                    .onDenied(new Action() {
                        @Override
                        public void onAction(List<String> permissions) {
                            //一直被拒绝弹框给用户
                            if (AndPermission.hasAlwaysDeniedPermission(context, permissions)) {
                                LogHelper.i("AndPermission----onDenied--hasAlwaysDeniedPermission");
                                final SettingService settingService = AndPermission.permissionSetting(context);
                                //弹框给用户去设置页面
                                DialogUtils.showMyDialog(context,
                                        "权限提醒",
                                        "为保证您正常地使用此功能，需要到手机设置中开启您的相机权限，去开启。",
                                        "去开启",
                                        "取消",
                                        new DialogUtils.OnDialogClickListener() {
                                            @Override
                                            public void onConfirm() {
                                                try {
                                                    settingService.execute();
                                                } catch (Exception e) {
                                                    gotoAppDetailSettingIntent(context);
                                                }
                                            }

                                            @Override
                                            public void onCancel() {
                                                permissionCallBack.onDenied();
                                            }
                                        });
                            } else {
                                LogHelper.i("AndPermission----onDenied");
                                permissionCallBack.onDenied();
                            }
                        }
                    })
                    .start();
        } else {
            boolean cameraPermission = isCameraPermission();
            if (!cameraPermission) {
                permissionCallBack.onDenied();
                return;
            }
            permissionCallBack.onGranted();
        }
    }


    public static void checkLocationPermission(final Context context, final PermissionCallBack permissionCallBack) {
        AndPermission.with(context)
                .permission(Permission.ACCESS_FINE_LOCATION)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        LogHelper.i("AndPermission----onGranted");
                        permissionCallBack.onGranted();
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        //一直被拒绝弹框给用户
                        if (AndPermission.hasAlwaysDeniedPermission(context, permissions)) {
                            LogHelper.i("AndPermission----onDenied--hasAlwaysDeniedPermission");
                            final SettingService settingService = AndPermission.permissionSetting(context);
                            //弹框给用户去设置页面
                            DialogUtils.showMyDialog(context,
                                    "权限提醒",
                                    "为保证您正常地使用此功能，需要到手机设置中开启您的定位权限，去开启。",
                                    "去开启",
                                    "取消",
                                    new DialogUtils.OnDialogClickListener() {
                                        @Override
                                        public void onConfirm() {
                                            try {
                                                settingService.execute();
                                            } catch (Exception e) {
                                                gotoAppDetailSettingIntent(context);
                                            }
                                        }

                                        @Override
                                        public void onCancel() {
                                            permissionCallBack.onDenied();
                                        }
                                    });
                        } else {
                            LogHelper.i("AndPermission----onDenied");
                            permissionCallBack.onDenied();
                        }
                    }
                })
                .start();
    }


    //--------------------6.0以下权限处理-------------------------

    /**
     * 相机是否可用
     * 返回true 表示可以使用  返回false表示不可以使用
     */
    private static boolean isCameraPermission() {
        boolean isCanUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters(); //针对魅族手机
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            isCanUse = false;
        }
        if (mCamera != null) {
            try {
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
                return isCanUse;
            }
        }
        return isCanUse;
    }

    /**
     * 录音是否可用
     * 返回true 表示可以使用  返回false表示不可以使用
     */
    private static boolean isVoicePermission() {
        AudioRecord record = null;
        try {
            record = new AudioRecord(MediaRecorder.AudioSource.MIC, 22050,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    AudioRecord.getMinBufferSize(22050,
                            AudioFormat.CHANNEL_CONFIGURATION_MONO,
                            AudioFormat.ENCODING_PCM_16BIT));
            record.startRecording();
            int recordingState = record.getRecordingState();
            if (recordingState == AudioRecord.RECORDSTATE_STOPPED) {
                return false;
            }
            //第一次  为true时，先释放资源，在进行一次判定
            //************
            record.release();
            record = new AudioRecord(MediaRecorder.AudioSource.MIC, 22050,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    AudioRecord.getMinBufferSize(22050,
                            AudioFormat.CHANNEL_CONFIGURATION_MONO,
                            AudioFormat.ENCODING_PCM_16BIT));
            record.startRecording();
            int recordingState1 = record.getRecordingState();
            if (recordingState1 == AudioRecord.RECORDSTATE_STOPPED) {
            }
            //**************
            //如果两次都是true， 就返回true  原因未知
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (record != null) {
                record.release();
            }
        }

    }

    /**
     * 联系人列表是否可用
     * 返回true 表示可以使用  返回false表示不可以使用
     */
    private static boolean isContactsListPermission(Context context) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * 是否有读写权限
     *
     * @return
     */
    private static boolean isWritePermission() {
        boolean isCanUser = true;
        File file = Environment.getExternalStorageDirectory();
        File newfile = new File(file, "1.txt");
        FileWriter fw = null;
        try {
            fw = new FileWriter(newfile);
            fw.flush();
            fw.write("123");
            isCanUser = true;
        } catch (Exception e) {
            isCanUser = false;
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                    isCanUser = true;
                } catch (IOException e) {
                    isCanUser = false;
                }
            }
        }
        return isCanUser;
    }

    private static boolean isSmsPermission(Context context) {
        String SMS_URI_ALL = "content://sms/";
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse(SMS_URI_ALL);
            String[] projection = new String[]{"_id", "address", "person",
                    "body", "date", "type"};
            cursor = context.getContentResolver().query(uri, projection, null,
                    null, "date desc"); // 获取手机内部短信
            //这里需要注意，当没有权限时拿到的count可能是0，也许记录被删除了，这里需要注意下！
            if (cursor != null && cursor.moveToFirst()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
