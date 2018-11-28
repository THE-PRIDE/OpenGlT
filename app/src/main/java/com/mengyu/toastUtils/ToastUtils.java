package com.mengyu.toastUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

public class ToastUtils {


    private static Toast toast;

    @SuppressLint("ShowToast")
    public static void show(Context context, String string){

        if (null == toast){
            toast = Toast.makeText(context,string,Toast.LENGTH_SHORT);
        } else {
            toast.setText(string);
        }
        toast.show();
    }
}
