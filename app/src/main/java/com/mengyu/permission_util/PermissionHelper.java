package com.mengyu.permission_util;

import android.app.Activity;
import android.app.FragmentManager;
import android.support.v4.app.Fragment;

/**
 * Created by LMY on 18/5/7.
 *
 * 链式调用
 *
 * 目前每次只允许申请一个权限
 *
 */

public class PermissionHelper {

    private Object mObject;
    private int mRequestCode;
    private String[] mRequestPermission;
    private final static String TAG = "permissionFragment";

    private PermissionHelper(Object object){
        this.mObject = object;
    }

    //链式方法
    public static PermissionHelper with(Activity activity){
        return new PermissionHelper(activity);
    }

    public static PermissionHelper with(Fragment fragment){
        return new PermissionHelper(fragment);
    }

    public PermissionHelper requestCode(int requestCode){
        this.mRequestCode = requestCode;
        return this;
    }

    public PermissionHelper requestPermission(String... permissions){
        this.mRequestPermission = permissions;
        return this;
    }

    private PermissionListener permissionListener;

    public PermissionHelper setOnResultListener(PermissionListener permissionListener){
        this.permissionListener = permissionListener;
        return this;
    }

    private PermissionFragment permissionFragment;

    /**
     * 发送申请权限请求
     */
    public void request(){
        permissionFragment = getRxPermissionsFragment((Activity) mObject);
        permissionFragment.setPermissionListener(permissionListener);
        permissionFragment.requestPermission(mRequestPermission,mRequestCode);
    }

    private PermissionFragment getRxPermissionsFragment(Activity activity) {
        PermissionFragment permissionFragment = findRxPermissionsFragment(activity);
        boolean isNewInstance = permissionFragment == null;
        if (isNewInstance) {
            permissionFragment = new PermissionFragment();
            FragmentManager fragmentManager = activity.getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(permissionFragment, TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return permissionFragment;
    }

    private PermissionFragment findRxPermissionsFragment(Activity activity) {
        return (PermissionFragment) activity.getFragmentManager().findFragmentByTag(TAG);
    }

}
