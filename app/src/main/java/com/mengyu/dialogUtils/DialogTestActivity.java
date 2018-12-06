package com.mengyu.dialogUtils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.meng.openglt.R;
import com.mengyu.Utils.LogUtils;
import com.mengyu.Utils.ToastUtils;

public class DialogTestActivity extends AppCompatActivity implements DialogInputListener{

    private static final String TAG = "DialogTestActivity";

    private TextView mBtnShowDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_test_layout);

        mBtnShowDialog = findViewById(R.id.button2);
        mBtnShowDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                LmyDialogFragment dialogFragment = new LmyDialogFragment();
//                dialogFragment.show(getSupportFragmentManager(),"123");
//                ToastUtils.show(DialogTestActivity.this,"确定");

                showDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void showDialog() {

        DialogHelper.with(this).setDialogCanCelable(false).setDialogLeft("确定").setDialogRight("取消")
                .needDialogTitle(true).setDialogContent("弹框内容")
                .setDialogInputListener(this)
                .setDialogTitle("提示").setDialogClickListener(new DialogClickListener() {
            @Override
            public void DialogLeft() {
                ToastUtils.show(DialogTestActivity.this,"确定");
                LogUtils.e(TAG,"确定");
            }

            @Override
            public void DialogRight() {
                ToastUtils.show(DialogTestActivity.this,"取消");
                LogUtils.e(TAG,"取消");
            }
        }).showDialog();

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //当按下HOME键的时
        //长按HOME键，选择运行程序的时
        //按下电源(关闭屏幕显示)时
        //从Activity中启动其他Activity时
        //屏幕方向切换时(例如从竖屏切换到横屏时)

        //会回调此方法，保存状态
        //保存状态后，一些commit将不可调用，可用允许状态丢失的commit代替
        super.onSaveInstanceState(outState);

        ToastUtils.show(this, "onSaveInstanceState");
    }

    @Override
    public void dialogInput(String string) {
        ToastUtils.show(this,string);
        LogUtils.e(TAG,string);
    }
}
