package com.mengyu.dialogUtils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class DialogHelper {

    private LmyDialogFragment dialogFragment;

    private FragmentManager fragmentManager;

    private DialogHelper(AppCompatActivity appCompatActivity) {
        dialogFragment = new LmyDialogFragment(appCompatActivity);
        this.fragmentManager = appCompatActivity.getSupportFragmentManager();
    }

    private DialogHelper(Fragment fragment){
        dialogFragment = new LmyDialogFragment(fragment.getContext());
        this.fragmentManager = fragment.getActivity().getSupportFragmentManager();
    }

    public static DialogHelper with(AppCompatActivity appCompatActivity) {
        return new DialogHelper(appCompatActivity);
    }

    /**
     *  在fragment中
     * @param fragment 环境
     * @return 返回实例
     */
    public static DialogHelper with(Fragment fragment){
        return new DialogHelper(fragment);
    }

    public DialogHelper needDialogTitle(boolean needTitle){
        dialogFragment.setNeedDialogTitle(needTitle);
        return this;
    }

    public DialogHelper setDialogTitle(String dialogTitle) {
        dialogFragment.setDialogTitle(dialogTitle);
        return this;
    }

    public DialogHelper setDialogContent(String dialogContent){
        dialogFragment.setDialogContent(dialogContent);
        return this;
    }

    public DialogHelper setDialogLeft(String dialogLeft){
        dialogFragment.setDialogLeft(dialogLeft);
        return this;
    }

    public DialogHelper setDialogRight(String dialogRight){
        dialogFragment.setDialogRight(dialogRight);
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

    public DialogHelper setDialogInputListener(DialogInputListener inputListener){
        dialogFragment.setInputListener(inputListener);
        return this;
    }

    public void showDialog(){
        // TODO 执行onSaveInstanceState后，调用此方法，会抛异常，但是没遇到
        dialogFragment.show(fragmentManager,"");
    }
}
