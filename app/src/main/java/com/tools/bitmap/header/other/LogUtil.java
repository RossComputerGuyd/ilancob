package com.tools.bitmap.header.other;


import java.io.File;
import java.io.RandomAccessFile;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
/**
 * 
 * @author yanggf
 *
 */
public class LogUtil {
	public static final String SAVE_FILE_PATH_DIRECTORY = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/" + "wanzhao";
	private static final String LOG_PATH = SAVE_FILE_PATH_DIRECTORY
			+ "/fslog.txt";
	public static File file = new File(LOG_PATH);

	public static final String TAG = "wanzhaoOa";
	public static boolean DEBUG = true;
	private static boolean PRINTLOG = false;
	private static boolean IS_DEBUG = false;

	public static void i(String msg) {
		if (DEBUG) {
			Log.i(TAG, msg);
		}
		if (PRINTLOG) {
			writeFile(msg);
		}
	}

	public static void i(String tag, String msg) {
		if (DEBUG) {
			Log.i(tag, msg);
		}
		if (PRINTLOG) {
			writeFile(msg);
		}
	}

	public static void i(String tag, String msg, Throwable tr) {
		if (DEBUG) {
			Log.i(tag, msg, tr);
		}
		if (PRINTLOG) {
			writeFile(msg);
		}
	}

	public static void v(String msg) {
		if (DEBUG || IS_DEBUG) {
			Log.v(TAG, msg);
		}
		if (PRINTLOG) {
			writeFile(msg);
		}
	}

	public static void v(String tag, String msg) {
		if (DEBUG || IS_DEBUG) {
			Log.v(tag, msg);
		}
		if (PRINTLOG) {
			writeFile(msg);
		}
	}

	public static void e(String msg) {
		if (DEBUG) {
			Log.e(TAG, msg);
		}
		if (PRINTLOG) {
			writeFile(msg);
		}
	}

	public static void e(String tag, String msg) {
		if (DEBUG) {
			Log.e(tag, msg);
		}
		if (PRINTLOG) {
			writeFile(msg);
		}
	}

	public static void e(String tag, String msg, Throwable tr) {
		if (DEBUG) {
			Log.e(tag, msg, tr);
		}
		if (PRINTLOG) {
			writeFile(msg);
		}
	}

	public static boolean isDEBUG() {
		return DEBUG;
	}

	public static void setDEBUG(boolean debug) {
		DEBUG = debug;
	}

	public static boolean isPrintlog() {
		return PRINTLOG;
	}

	public static boolean isPRINTLOG() {
		return PRINTLOG;
	}

	public static void setPRINTLOG(boolean pRINTLOG) {
		PRINTLOG = pRINTLOG;
	}
	
	
	public static void Logger(String msg) {
		if (IS_DEBUG) {
			Log.e(TAG, msg);
		}
		if (PRINTLOG) {
		}
	}
	/**
	 * 写入文件
	 * 
	 * @param str
	 */
	public synchronized static void writeFile(String content) {
		if (TextUtils.isEmpty(content) || !isSDcardExist()) {
			return;
		}
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			long len = raf.length();
			raf.seek(len);
			raf.writeBytes(content);
			raf.close();
		} catch (Exception e) {
			LogUtil.e(TAG, e.toString());
		}
	}
	/**
	 * 判断是否有存储卡，有返回TRUE，否则FALSE
	 * 
	 * @return
	 */
	public static boolean isSDcardExist() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}


}
