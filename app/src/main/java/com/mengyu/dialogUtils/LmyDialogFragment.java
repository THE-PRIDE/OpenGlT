package com.mengyu.dialogUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meng.openglt.R;

/**
 * 使用DialogFragment至少需要实现onCreateView或者onCreateDialog方法。
 * onCreateView即使用定义的xml布局文件展示Dialog。onCreateDialog即利用AlertDialog或者Dialog创建出Dialog。
 */
public class LmyDialogFragment extends DialogFragment implements View.OnClickListener{

    private View view;
    private TextView mTvDialogTitle;
    private View mViewTitleLine;
    private TextView mTvDialogContent;
    private TextView mTvDialogLeft;
    private TextView mTvDialogRight;

    private DialogClickListener clickListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        try {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }catch (NullPointerException e){
            e.printStackTrace();
        }

//        view = inflater.inflate(R.layout.dialog_default_layout,container);
//        initView(view);
//        initListener();
        return view;
    }

    public LmyDialogFragment(){

    }

    /**
     * @param context
     */
    @SuppressLint("ValidFragment")
    public LmyDialogFragment(Context context){
        view = LayoutInflater.from(context).inflate(R.layout.dialog_default_layout,null);
        initView(view);
        initListener();
    }

    private void initView(View view){
        mTvDialogTitle = view.findViewById(R.id.tv_dialog_title);
        mViewTitleLine = view.findViewById(R.id.view_title_line);
        mTvDialogContent = view.findViewById(R.id.tv_dialog_content);
        mTvDialogLeft = view.findViewById(R.id.tv_dialog_left);
        mTvDialogRight = view.findViewById(R.id.tv_dialog_right);
    }

    private void initListener(){
        mTvDialogLeft.setOnClickListener(this);
        mTvDialogRight.setOnClickListener(this);
    }

    public void setDialogTitle(String dialogTitle){
        mTvDialogTitle.setText(dialogTitle);
    }

    public void setmTvDialogLeft(String dialogLeft){
        mTvDialogLeft.setText(dialogLeft);
    }

    public void setmTvDialogRight(String dialogRight){
        mTvDialogRight.setText(dialogRight);
    }

    /**
     * 控制dialog是否可取消
     * @param canCancel 是否可取消
     */
    public void setDialogCanCancel(boolean canCancel){
        this.setCancelable(canCancel);
    }

    /**
     * 控制取消dialog
     */
    public void dialogDismiss(){
        this.dismissAllowingStateLoss();
        this.dismiss();  // 区别

        //区别是：前者允许界面状态丢失(即onSaveInstanceState被调用)，后者则不允许(会抛出异常)

        //TODO
        //dimiss没测试，commit测试时，执行onSaveInstanceState后,并没有抛出异常，不解？

    }

    public void setClickListener(DialogClickListener clickListener){
        this.clickListener = clickListener;
    }

//    @Override
//    public void show(FragmentManager manager, String tag) {
//        FragmentTransaction ft = manager.beginTransaction();
//        ft.add(this, tag);
//        ft.commitAllowingStateLoss();
//    }

    //    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_default_layout, null);
//        builder.setView(view);
//        return builder.create();
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_dialog_left:
                if (null != clickListener){
                    clickListener.DialogLeft();
                    this.dismiss();
                }
                break;
            case R.id.tv_dialog_right:
                if (null != clickListener){
                    clickListener.DialogRight();
                    this.dismiss();
                }
                break;
        }
    }
}
