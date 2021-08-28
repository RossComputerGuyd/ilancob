package com.uwo.tools.aibum.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by SRain on 2015/12/11.
 * <p/>
 * 弹出对话框工具类
 */
public class DialogUtils {

    /**
     * 创建一个列表对话框
     *
     * @param items
     */
    public static void createListDialog(Context context, String title, String[] items, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setItems(items, listener);
        dialog.setNegativeButton("取消", null);
        dialog.show();
    }
}
