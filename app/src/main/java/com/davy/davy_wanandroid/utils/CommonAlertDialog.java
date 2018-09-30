package com.davy.davy_wanandroid.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.davy.davy_wanandroid.R;

/**
 * author: Davy
 * date: 2018/9/30
 */
public class CommonAlertDialog {

    private AlertDialog mAlertDialog;

    public static CommonAlertDialog newInstance(){
        return CommonAlertDialogHolder.COMMON_ALERT_DIALOG;
    }

    private static class CommonAlertDialogHolder{

        public static final CommonAlertDialog COMMON_ALERT_DIALOG = new CommonAlertDialog();

    }

    /**
     * 取消dialog
     * @param isAdd
     */
    public void isCancelDialog(boolean isAdd){
        if(isAdd && mAlertDialog != null && mAlertDialog.isShowing()){
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }
    }

    /**
     * 显示dialog
     * @param activity
     * @param content
     * @param okContent
     * @param noContent
     * @param okClickListener
     * @param noClickListener
     */
    public void showDialog(Activity activity, String content, String okContent, String noContent,
                           View.OnClickListener okClickListener,
                           View.OnClickListener noClickListener){
        if(activity == null){
            return;
        }
        if(mAlertDialog == null){
            mAlertDialog = new AlertDialog.Builder(activity, R.style.myCorDialog).create();
        }

        if(!mAlertDialog.isShowing()){
            mAlertDialog.show();
        }
        mAlertDialog.setCanceledOnTouchOutside(true);
        Window window = mAlertDialog.getWindow();
        if(window != null){
            window.setContentView(R.layout.common_alert_dialog);
            TextView dialogContent = window.findViewById(R.id.dialog_content);
            dialogContent.setText(content);
            View btnDivider = window.findViewById(R.id.dialog_btn_divider);
            btnDivider.setVisibility(View.VISIBLE);
            Button okBtn = window.findViewById(R.id.dialog_btn);
            okBtn.setText(okContent);
            okBtn.setOnClickListener(okClickListener);
            Button noBtn = window.findViewById(R.id.dialog_negative_btn);
            noBtn.setText(noContent);
            noBtn.setOnClickListener(noClickListener);
            noBtn.setVisibility(View.VISIBLE);
        }
    }

}
