package com.xm.permissions;

/**
 * created on 2018/2/5.
 * author:wangkezhi
 * email:45436660@qq.com
 * summary:权限回调结果
 */
public interface OnPermissionResult {
    /**
     * 申请权限成功
     */
    void onSuccess();

    /**
     * 申请权限失败
     *
     * @param onRationale true:选中不再询问;false:未选中不再询问;
     */
    void onFailed(boolean onRationale);
}
