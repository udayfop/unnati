package com.project.tmc.Retrofit;

import android.app.Activity;
import android.content.Context;

import com.project.tmc.R;

import cc.cloudist.acplibrary.ACProgressFlower;

public class CustomDialog {
    private static ACProgressFlower dialog;
    public static void showDialog(Context context) {
        if (!isShoving()) {
            if (!((Activity) context).isFinishing()) {
                dialog = new ACProgressFlower.Builder(context).text(context.getResources().getString(R.string.please_wait)).textSize(26).textColor(context.getResources()
                        .getColor(R.color.white)).build();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        }
    }

    public static void dismissDialog(){
        try {
            if ((dialog != null) && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (final IllegalArgumentException e) {
            // Handle or log or ignore
        } catch (final Exception e) {
            // Handle or log or ignore
        } finally {
            dialog = null;
        }
    }

    public static boolean isShoving(){
        if (dialog != null){
            return dialog.isShowing();
        } else {
            return false;
        }
    }
}
