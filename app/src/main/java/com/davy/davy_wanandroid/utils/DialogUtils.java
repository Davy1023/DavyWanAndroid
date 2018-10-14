package com.davy.davy_wanandroid.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

/**
 * author: Davy
 * date: 18/10/13
 */
public class DialogUtils {

    public static MaterialDialog showMyDialog(final Context context, String title, String content, String positiveBtnText, String negativeBtnText, final OnDialogClickListener onDialogClickListener) {

        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        builder.title(title);
        builder.content(content);
        builder.positiveText(positiveBtnText);
        builder.negativeText(negativeBtnText);
        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (onDialogClickListener != null) {
                            onDialogClickListener.onConfirm();
                        }
                    }
                });
                builder.onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (onDialogClickListener != null) {
                            onDialogClickListener.onCancel();
                        }
                    }
                });
        MaterialDialog materialDialog = builder.show(
        );
        return materialDialog;
    }

    public interface OnDialogClickListener {

        void onConfirm();

        void onCancel();
    }

    public static MaterialDialog showMyListDialog(final Context context,String title, int contents, final OnDialogListCallback onDialogListCallback){
        MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                .title(title)
                .items(contents)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        onDialogListCallback.onSelection(dialog,itemView,position,text);
                    }
                }).show();
        return materialDialog;
    }

    public interface OnDialogListCallback {

        void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text);
    }

}
