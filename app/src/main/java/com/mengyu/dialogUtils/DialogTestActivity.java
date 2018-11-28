package com.mengyu.dialogUtils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.meng.openglt.R;
import com.mengyu.toastUtils.ToastUtils;

public class DialogTestActivity extends AppCompatActivity{

    private Button mBtnShowDialog;
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

                showDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void showDialog(){

        DialogHelper.with(this).setDialogCanCelable(false).setDialogLeft("确定").setDialogRight("取消")
                .setDialogTitle("提示").setDialogClickListener(new DialogClickListener() {
            @Override
            public void DialogLeft() {
                Toast.makeText(DialogTestActivity.this,"确定",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void DialogRight() {
                Toast.makeText(DialogTestActivity.this,"取消",Toast.LENGTH_SHORT).show();
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

        ToastUtils.show(this,"onSaveInstanceState");
    }
}
