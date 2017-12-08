package com.allens.lib_retrofit.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by allens on 2017/11/30.
 */

public class XDialogUtil {

    private Dialog dialog;
    private ProgressDialog waitingDialog;

    private Boolean isShow;

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    private static XDialogUtil dialogUtil;

    public static XDialogUtil create() {
        if (dialogUtil == null) {
            synchronized (XDialogUtil.class) {
                if (dialogUtil == null) {
                    dialogUtil = new XDialogUtil();
                }
            }
        }
        return dialogUtil;
    }

    public void show(Context context) {
        if (dialog != null && isShow) {
            if (!dialog.isShowing())
                dialog.show();
        } else if (dialog == null && isShow) {
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


    public Boolean getShow() {
        return isShow;
    }

    public void setShow(Boolean show) {
        isShow = show;
    }
}
