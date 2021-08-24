package com.uwo.tools.aibum.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by SRain on 15-12-8.
 * <p/>
 * image for bitmap
 * <p/>
 * 根据uri获取bitmap	getBitmapForUri
 * 根据文件流获取bitmap	getBitmapForFileInputStream
 * 根据文件路径获取bitmap	getBitmapForPath
 * 根据资源id获取bitmap	getPathForBitmap
 * 将bitmap保存至本地指定路径	saveFileForBitmap
 * bitmap剪切	cropperBitmap
 * bitmap旋转
 * 将图片旋转成正位
 * bitmap圆角/圆形	roundBitmap
 * bitmap压缩
 * bitmap大图片处理
 * bitmap计算所在空间大小	computeSampleSize
 * 设置图片压缩
 */
public class ImageBitmapUtils {
    private static String Tag = "ImageBitmapUtils";

    /**
     * 根据uri获取bitmap
     *
     * @param uri
     * @return bitmap
     */
    public static Bitmap getBitmapForUri(Activity activity, Uri uri) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据文件路径获取bitmap
     *
     * @param path
     * @return bitmap
     */
    public static Bitmap getBitmapForPath(String path) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        return bitmap;
    }

    /**
     * 根据文件流获取bitmap
     *
     * @param filepath
     * @return bitmap
     */
    public static Bitmap getBitmapForFileInputStream(String filepath) {
        Bitmap bitmap = null;
        try {
            FileInputStream fis = new FileInputStream(filepath);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (Exception e) {

        }
        return bitmap;
    }

    /**
     * 根据资源id获取bitmap
     *
     * @param id 资源id
     * @return filepath
     */
    public static Bitmap getPathForBitmap(Context context, int id) {
        InputStream is = context.getResources().openRawResource(id);
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        Paint mPaint = new Paint();
//        canvas.drawBitmap(bitmap, 40, 40, mPaint);
        return bitmap;
    }

    /**
     * 将Bitmap保存文件至本地SDCard
     *
     * @param dir
     * @param bitmap
     * @return
     */
    public static String saveBitmapToFile(String dir, Bitmap bitmap) {
        String filepath = "";
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            Log.e("TestFile", "SD card is not avaiable/writeable right now.");
            return filepath;
        }
        FileOutputStream outputStream = null;
        File file = new File(dir);
        if (!file.exists())
            file.mkdirs();// 创建文件夹
        filepath = dir + System.currentTimeMillis() + ".jpg";
        try {
            outputStream = new FileOutputStream(filepath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);// 把数据写入文件
            Log.e(Tag + "filepath", "ok" + filepath);
        } catch (FileNotFoundException e) {
            Log.e("FileNotFoundException", e.toString());
            filepath = "";
            Log.e(Tag + "filepath", filepath);
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                Log.e("IOException", e.toString());
                filepath = "";
                Log.e(Tag + "filepath", "finally:" + filepath);
            }
        }
        return filepath;
    }

    /**
     * Android提供的一种动态计算Bitmap所占空间大小的方法。
     *
     * @param options
     * @param minSideLength
     * @param maxNumOfPixels
     * @return
     */
    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    /**
     * 动态计算Bitmap所占空间大小
     *
     * @param options
     * @param minSideLength
     * @param maxNumOfPixels
     * @return
     */
    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }


}
