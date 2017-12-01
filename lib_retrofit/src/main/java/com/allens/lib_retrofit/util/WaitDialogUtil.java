package com.allens.lib_retrofit.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by allens on 2017/11/30.
 */

public class WaitDialogUtil {

    private Dialog dialog;

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
            dialog.show();
        } else {
            ProgressDialog waitingDialog = new ProgressDialog(context);
            dialog = waitingDialog;
            waitingDialog.setMessage("正在加载数据...");
            waitingDialog.show();
        }
    }

    public void hide() {
        if (dialog != null && dialog.isShowing()) {
            dialog.hide();
        }
    }
}
