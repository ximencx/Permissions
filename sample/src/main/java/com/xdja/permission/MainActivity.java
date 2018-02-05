package com.xdja.permission;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xdja.permissions.OnPermissionResult;
import com.xdja.permissions.PermissionsManager;

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
        permissionsManager.setPermissions(permissions)
                .setOnPermissionResult(new OnPermissionResult() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(activity, "success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(boolean onRationale) {
                        Toast.makeText(activity, "failed===onRationale:::" + onRationale, Toast.LENGTH_SHORT).show();
                    }
                }).request();
    }
}
