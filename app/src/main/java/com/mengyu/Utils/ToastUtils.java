package com.mengyu.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import com.meng.openglt.R;

public class ToastUtils {


    private static Toast toast;

    @SuppressLint("ShowToast")
    public static void show(Context context, String string){

        if (null == toast){
            toast = Toast.makeText(context,string,Toast.LENGTH_SHORT);
        } else {
            // TODO  Toast 对话框点击确定后，弹框内容也打印时；TOAST不展示~不清楚问题
            toast.setText(string);
         }
        toast.show();
    }
}
