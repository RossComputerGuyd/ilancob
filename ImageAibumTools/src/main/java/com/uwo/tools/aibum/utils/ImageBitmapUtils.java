package com.uwo.tools.aibum.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by SRain on 15-12-8.
 * <p/>
 * image for bitmap
 */
public class ImageBitmapUtils {

    private static String Tag = "ImageBitmapUtils";

    /**
     * get bitmap for uri
     *
     * @param uri
     * @return bitmap
     */
    public static Bitmap getBitmapForUri(Uri uri) {
        Bitmap bitmap = null;
        return bitmap;
    }

    /**
     * get Bitmap for filepath
     * @param path
     * @return bitmap
     */
    public static Bitmap getBitmapForPath(String path){
        Bitmap bitmap = null;
        return bitmap;
    }

    /**
     * get filepath for bitmap
     *
     * @param bitmap
     * @return filepath
     */
    public static String getPathForBitmap(Bitmap bitmap) {
        String path = "";
        return path;
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
