package com.mengyu.dialogUtils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class DialogHelper {

    private LmyDialogFragment dialogFragment;

//    public static DialogHelper getInstence() {
//
//        dialogFragment.show();
//        return new DialogHelper();
//    }

    private Object object;
    private FragmentManager fragmentManager;

    private AppCompatActivity appCompatActivity;

    public DialogHelper(Object object) {
        dialogFragment = new LmyDialogFragment((Context) object);
        this.appCompatActivity = (AppCompatActivity) object;
        this.fragmentManager = appCompatActivity.getSupportFragmentManager();
        this.object = object;
    }

    public static DialogHelper with(AppCompatActivity appCompatActivity) {
        return new DialogHelper(appCompatActivity);
    }

    /**
     *  在fragment中
     * @param fragment
     * @return
     */
    public static DialogHelper with(Fragment fragment){
        return new DialogHelper(fragment);
    }

    public DialogHelper setDialogTitle(String dialogTitle) {
        dialogFragment.setDialogTitle(dialogTitle);
        return this;
    }

    public DialogHelper setDialogLeft(String dialogLeft){
        dialogFragment.setmTvDialogLeft(dialogLeft);
        return this;
    }

    public DialogHelper setDialogRight(String dialogRight){
        dialogFragment.setmTvDialogRight(dialogRight);
        return this;
    }

    public DialogHelper setDialogCanCelable(boolean canCancel){
        dialogFragment.setDialogCanCancel(canCancel);
        return this;
    }

    public DialogHelper setDialogClickListener(DialogClickListener clickListener){
        dialogFragment.setClickListener(clickListener);
        return this;
    }

    public void showDialog(){
        dialogFragment.show(fragmentManager,"");
    }
}
