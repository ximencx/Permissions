/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xm.permissions;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Build;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限申请管理类
 */
public class PermissionsManager {

    private static final String TAG = "PermissionsManager";

    private PermissionsFragment mRxPermissionsFragment;

    private String[] permissions;

    private OnPermissionResult onPermissionResult;

    public PermissionsManager(@NonNull Activity activity) {
        if (mRxPermissionsFragment == null) {
            mRxPermissionsFragment = getRxPermissionsFragment(activity);
        }
    }

    public PermissionsManager setPermissions(String[] permissions) {
        this.permissions = permissions;
        return this;
    }

    public PermissionsManager setOnPermissionResult(OnPermissionResult onPermissionResult) {
        this.onPermissionResult = onPermissionResult;
        return this;
    }

    private PermissionsFragment getRxPermissionsFragment(Activity activity) {
        PermissionsFragment rxPermissionsFragment = findRxPermissionsFragment(activity);
        boolean isNewInstance = rxPermissionsFragment == null;
        if (isNewInstance) {
            rxPermissionsFragment = new PermissionsFragment();
            FragmentManager fragmentManager = activity.getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(rxPermissionsFragment, TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return rxPermissionsFragment;
    }

    private PermissionsFragment findRxPermissionsFragment(Activity activity) {
        return (PermissionsFragment) activity.getFragmentManager().findFragmentByTag(TAG);
    }

    public PermissionsManager request() {
        //检查变量
        if (permissions == null || permissions.length == 0) {
            throw new IllegalArgumentException("permissions requires at least one input permission");
        }
        //设置回调
        if (mRxPermissionsFragment != null) {
            mRxPermissionsFragment.setOnPermissionResult(onPermissionResult);
        }
        //请求权限
        requestImplementation(permissions);
        return this;
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void requestImplementation(final String... permissions) {
        List<String> unrequestedPermissions = new ArrayList<>();
        //检查权限状态
        for (String permission : permissions) {
            if (mRxPermissionsFragment != null && PermissionUtils.isGranted(mRxPermissionsFragment.getActivity(), permission)) {
                continue;
            }
            if (mRxPermissionsFragment != null && PermissionUtils.isRevoked(mRxPermissionsFragment.getActivity(), permission)) {
                continue;
            }
            unrequestedPermissions.add(permission);
        }
        //请求权限
        if (!unrequestedPermissions.isEmpty()) {
            String[] unrequestedPermissionsArray = unrequestedPermissions.toArray(new String[unrequestedPermissions.size()]);
            requestPermissionsFromFragment(unrequestedPermissionsArray);
        } else {//没有可请求的权限则返回成功
            if (onPermissionResult != null) {
                onPermissionResult.onSuccess();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestPermissionsFromFragment(String[] permissions) {
        if (mRxPermissionsFragment != null) {
            mRxPermissionsFragment.requestPermissions(permissions);
        }
    }
}
