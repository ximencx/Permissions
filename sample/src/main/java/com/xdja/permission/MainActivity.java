package com.xdja.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xdja.permissions.OnPermissionResult;
import com.xdja.permissions.PermissionUtils;
import com.xdja.permissions.PermissionsManager;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private Activity activity;
    private String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        init();
    }

    private void init() {
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickMethod();
            }
        });
    }

    private void clickMethod() {
        PermissionsManager permissionsManager = new PermissionsManager(activity);
        permissionsManager.setPermissions(permissions)//申请权限
                .setOnPermissionResult(new OnPermissionResult() {//设置回调
                    @Override
                    public void onSuccess() {//已有权限或者申请成功
                        Toast.makeText(activity, "success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(boolean onRationale) {//申请的权限没有全部成功
                        Toast.makeText(activity, "failed===onRationale:::" + onRationale, Toast.LENGTH_SHORT).show();
                    }
                }).request();
//        //跳转设置界面
//        PermissionUtils.goSettingActivity(activity,101);
//        //判断是否包含权限
//        PermissionUtils.hasPermission(activity, Arrays.asList(permissions));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==101){
            //跳转设置界面后的回调
        }
    }
}
