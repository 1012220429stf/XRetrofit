package com.allens.lib_retrofit.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by allens on 2017/11/30.
 */

public class WaitDialogUtil {

    private Dialog dialog;
    private ProgressDialog waitingDialog;

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }


    private WaitDialogUtil() {

    }

    private static WaitDialogUtil newIntance;

    public static WaitDialogUtil create() {
        if (newIntance == null) {
            synchronized (WaitDialogUtil.class) {
                if (newIntance == null) {
                    newIntance = new WaitDialogUtil();
                }
            }
        }
        return newIntance;
    }

    public void show(Context context) {
        if (dialog != null) {
            if (!dialog.isShowing())
                dialog.show();
        } else {
            waitingDialog = new ProgressDialog(context);
            dialog = waitingDialog;
            waitingDialog.setMessage("正在加载数据...");
            if (!waitingDialog.isShowing()) {
                waitingDialog.show();
            }
        }
    }

    public void hide() {
        if (dialog != null && dialog.isShowing()) {
            dialog.hide();
            dialog.dismiss();
            dialog = null;
        }

        if (waitingDialog != null) {
            waitingDialog.hide();
            waitingDialog.dismiss();
            waitingDialog = null;
        }
    }
}
