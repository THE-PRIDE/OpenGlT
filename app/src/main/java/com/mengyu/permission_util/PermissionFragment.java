package com.mengyu.permission_util;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

/**
 * Created by LMY on 18/5/9.
 * 用来消费PermissionResult
 */

public class PermissionFragment extends Fragment {


    private PermissionListener permissionListener;
    public void setPermissionListener(PermissionListener permissionListener){
        this.permissionListener = permissionListener;
    }

    public void requestPermission(final String[] permissions , final int requestCode){

        if (Build.VERSION.SDK_INT < 23){
            //防止低版本手机，可以自行在设置界面关闭权限，导致应用没有获取到权限
            if (ActivityCompat.checkSelfPermission(getActivity(),permissions[0])
                    == PackageManager.PERMISSION_GRANTED){
                permissionListener.success();
            } else {
                permissionListener.failed();
            }
        } else {
            if (ActivityCompat.checkSelfPermission(getActivity(),permissions[0])
                    == PackageManager.PERMISSION_GRANTED){
                permissionListener.success();
            } else {
                if (shouldShowRequestPermissionRationale(permissions[0])){
                    //第一次被拒，第二次申请时，走这里   很多手机屏蔽了，需要验证
                    //三星  没问题
                    new AlertDialog.Builder(getActivity())
                            .setMessage("使用该功能需同意赋予权限")
                            .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    permissionListener.failed();
                                }
                            })
                            .setPositiveButton("允许", new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(permissions,requestCode);
                                }
                            }).show();
                } else {
                    requestPermissions(permissions,requestCode);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
            permissionListener.success();
        } else {
            permissionListener.failed();
        }
    }
}
