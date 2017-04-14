package com.stillwildman.lazyrunner.utilities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.stillwildman.lazyrunner.R;

/**
 * Created by vincent.chang on 2017/4/13.
 */

public class DialogHelper {

    private static ProgressDialog progressDialog;
    private static Dialog dialog;


    public static void showProgressDialog(Context context, String message) {
        progressDialog = ProgressDialog.show(context, message, "", true, false);
    }

    public static void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public static void showLoadingDialog(Context context) {
        dismissDialog();

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

        View view = LayoutInflater.from(context).inflate(R.layout.inflate_loading_window, null);
        dialogBuilder.setView(view);
        dialogBuilder.setCancelable(false);

        dialog = dialogBuilder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setDimAmount(0.0f);
        }
        dialog.show();
    }

    public static void dismissDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }
}
