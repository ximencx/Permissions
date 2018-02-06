package com.xm.permissions;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.AppOpsManagerCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import java.util.List;

/**
 * created on 2018/2/5.
 * author:wangkezhi
 * email:45436660@qq.com
 * summary:权限申请工具类
 */

public class PermissionUtils {
    /**
     * 判断是否包含权限
     *
     * @param context     上下文
     * @param permissions 权限列表
     * @return true:包含权限;false:不包含
     */
    public static boolean hasPermission(@NonNull Context context, @NonNull List<String> permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true;
        for (String permission : permissions) {
            int result = ContextCompat.checkSelfPermission(context, permission);
            if (result == PackageManager.PERMISSION_DENIED) return false;

            String op = AppOpsManagerCompat.permissionToOp(permission);
            if (TextUtils.isEmpty(op)) continue;
            result = AppOpsManagerCompat.noteProxyOp(context, op, context.getPackageName());
            if (result != AppOpsManagerCompat.MODE_ALLOWED) return false;
        }
        return true;
    }

    /**
     * 跳转设置界面
     *
     * @param activity    Activity对象
     * @param requestCode 请求code
     */
    public static void goSettingActivity(@NonNull Activity activity, int requestCode) {
        Context context = activity;
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        activity.startActivityForResult(intent, requestCode);
    }


    /**
     * 是否点击不再询问
     *
     * @param activity    Activity对象
     * @param permissions 权限列表
     * @return true:没选中;false:选中;
     */
    protected static boolean shouldShowRequestPermissionRationale(final Activity activity, final String... permissions) {
        if (!isMarshmallow()) {
            return false;
        }
        return shouldShowRequestPermissionRationaleImplementation(activity, permissions);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static boolean shouldShowRequestPermissionRationaleImplementation(final Activity activity, final String... permissions) {
        for (String p : permissions) {
            if (!isGranted(activity, p) && !activity.shouldShowRequestPermissionRationale(p)) {
                return false;
            }
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected static boolean isGranted(Activity activity, String permission) {
        return !isMarshmallow() || activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected static boolean isRevoked(Activity activity, String permission) {
        return isMarshmallow() && activity.getPackageManager().isPermissionRevokedByPolicy(permission, activity.getPackageName());
    }

    /**
     * 判断当前版本
     *
     * @return true:需要申请权限
     */
    protected static boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
