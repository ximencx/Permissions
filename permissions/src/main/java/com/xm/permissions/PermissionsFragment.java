package com.xm.permissions;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 申请权限的载体，申请权限的回调
 */
public class PermissionsFragment extends Fragment {

    private static final int PERMISSIONS_REQUEST_CODE = 42;

    private OnPermissionResult onPermissionResult;

    public PermissionsFragment() {
    }

    protected void setOnPermissionResult(OnPermissionResult onPermissionResult) {
        this.onPermissionResult = onPermissionResult;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    /**
     * 申请权限
     *
     * @param permissions
     */
    @TargetApi(Build.VERSION_CODES.M)
    protected void requestPermissions(@NonNull String[] permissions) {
        requestPermissions(permissions, PERMISSIONS_REQUEST_CODE);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //检查requestCode
        if (requestCode != PERMISSIONS_REQUEST_CODE) return;
        List<PermissionModel> list = new ArrayList<>(permissions.length);
        for (String permission : permissions) {
            if (PermissionUtils.isGranted(getActivity(), permission)) {
                list.add(new PermissionModel(permission, true));
                continue;
            }
            if (PermissionUtils.isRevoked(getActivity(), permission)) {
                list.add(new PermissionModel(permission, false));
                continue;
            }
            list.add(new PermissionModel(permission, false));
        }
        //回调结果
        for (PermissionModel p : list) {
            if (!p.granted) {
                if (onPermissionResult != null) {
                    onPermissionResult.onFailed(!PermissionUtils.shouldShowRequestPermissionRationale(getActivity(), permissions));
                    return;
                }
            }
        }
        if (onPermissionResult != null) {
            onPermissionResult.onSuccess();
        }
    }
}
