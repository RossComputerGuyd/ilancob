package com.tools.bitmap.bitmap;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;


public class BitmapScale {
    private static final String TAG = "BitmapScale";

    /**
     * 缩放Bitmap满屏
     *
     * @param bitmap
     * @param screenWidth
     * @param screenHight
     * @return
     */
    public static Bitmap getBitmap(Bitmap bitmap, int screenWidth,
                                   int screenHight) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix matrix = new Matrix();
        float scale = (float) screenWidth / w;
        float scale2 = (float) screenHight / h;

        // scale = scale < scale2 ? scale : scale2;

        matrix.postScale(scale, scale);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        if (bitmap != null && !bitmap.equals(bmp) && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }

        return bmp;// Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
    }


    /**
     * 按最大边按一定大小缩放图片
     */
    public static Bitmap scaleImage(byte[] buffer, float size) {
        // 获取原图宽度
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        options.inPurgeable = true;
        options.inInputShareable = true;

        Bitmap bm = BitmapFactory.decodeByteArray(buffer, 0, buffer.length, options);

        // 计算缩放比例
        float reSize = options.outWidth / size;
        if (options.outWidth < options.outHeight) {
            reSize = options.outHeight / size;
        }
        // 如果是小图则放大
        if (reSize <= 1) {
            int newWidth = 0;
            int newHeight = 0;
            if (options.outWidth > options.outHeight) {
                newWidth = (int) size;
                newHeight = options.outHeight * (int) size / options.outWidth;
            } else {
                newHeight = (int) size;
                newWidth = options.outWidth * (int) size / options.outHeight;
            }
            bm = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
            bm = scaleImage(bm, newWidth, newHeight);
            if (bm == null) {
                Log.e(TAG, "convertToThumb, decode fail:" + null);
                return null;
            }

            return bm;
        }
        // 缩放
        options.inJustDecodeBounds = false;
        options.inSampleSize = (int) reSize;

        bm = BitmapFactory.decodeByteArray(buffer, 0, buffer.length, options);
        if (bm == null) {
            Log.e(TAG, "convertToThumb, decode fail:" + null);
            return null;
        }

        return bm;

    }

    /**
     * 检查图片是否超过一定值，是则缩小
     *
     * @param buffer
     * @param size
     */
    public static Bitmap convertToThumb(byte[] buffer, float size) {
        // 获取原图宽度
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        options.inPurgeable = true;
        options.inInputShareable = true;

        Bitmap bm = BitmapFactory.decodeByteArray(buffer, 0, buffer.length, options);

        // 计算缩放比例
        float reSize = options.outWidth / size;

        if (options.outWidth > options.outHeight) {
            reSize = options.outHeight / size;
        }

        if (reSize <= 0) {
            reSize = 1;
        }
        Log.d(TAG, "convertToThumb, reSize:" + reSize);


        // 缩放
        options.inJustDecodeBounds = false;
        options.inSampleSize = (int) reSize;

        if (bm != null && !bm.isRecycled()) {
            bm.recycle();
            bm = null;
            Log.e(TAG, "convertToThumb, recyle");
        }

        bm = BitmapFactory.decodeByteArray(buffer, 0, buffer.length, options);

        if (bm == null) {
            Log.e(TAG, "convertToThumb, decode fail:" + null);
            return null;
        }

        return bm;
    }

    /**
     * 以屏幕宽度为基准，显示图片
     *
     * @param context
     * @param data
     * @param size
     * @return
     */
    public static Bitmap decodeStream(Context context, Intent data, float size) {
        Bitmap image = null;
        try {
            Uri dataUri = data.getData();
            // 获取原图宽度
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            options.inPurgeable = true;
            options.inInputShareable = true;
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(dataUri), null, options);

            // 计算缩放比例
            float reSize = (int) (options.outWidth / size);
            if (reSize <= 0) {
                reSize = 1;
            }

            Log.d(TAG, "old-w:" + options.outWidth + ", llyt-w:" + size + ", resize:" + reSize);
            // 缩放
            options.inJustDecodeBounds = false;
            options.inSampleSize = (int) reSize;
            image = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(dataUri), null, options);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return image;
    }

    /**
     * 按新的宽高缩放图片
     *
     * @param bm
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap scaleImage(Bitmap bm, int newWidth, int newHeight) {
        if (bm == null) {
            return null;
        }

        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        if (bm != null & !bm.isRecycled()) {
            bm.recycle();
            bm = null;
        }
        return newbm;
    }

    /**
     * fuction: 设置固定的宽度，高度随之变化，使图片不会变形
     *
     * @param target   需要转化bitmap参数
     * @param newWidth 设置新的宽度
     * @return
     */
    public static Bitmap fitBitmap(Bitmap target, int newWidth) {
        int width = target.getWidth();
        int height = target.getHeight();
        Matrix matrix = new Matrix();

        float scaleWidth = ((float) newWidth) / width;
        // float scaleHeight = ((float)newHeight) / height;
        int newHeight = (int) (scaleWidth * height);
        matrix.postScale(scaleWidth, scaleWidth);
        // Bitmap result = Bitmap.createBitmap(target,0,0,width,height,
        // matrix,true);
        Bitmap bmp = Bitmap.createBitmap(target, 0, 0, width, height, matrix, true);
        if (target != null && !target.equals(bmp) && !target.isRecycled()) {
            target.recycle();
            target = null;
        }
        return bmp;// Bitmap.createBitmap(target, 0, 0, width, height, matrix,
        // true);
    }


    /**
     * 根据指定的宽度平铺图像
     *
     * @param width
     * @param src
     * @return
     */
    public static Bitmap createRepeater(int width, Bitmap src) {
        int count = (width + src.getWidth() - 1) / src.getWidth();

        Bitmap bitmap = Bitmap.createBitmap(width, src.getHeight(),
                Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        for (int idx = 0; idx < count; ++idx) {

            canvas.drawBitmap(src, idx * src.getWidth(), 0, null);
        }

        return bitmap;
    }

    /*-----------------------------------图片压缩-------------------------------------------*/

    /**
     * 图片的质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        if (baos != null) {
            try {
                baos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (isBm != null) {
            try {
                isBm.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (image != null && !image.isRecycled()) {
            image.recycle();
            image = null;
        }
        return bitmap;
    }


    /**
     * 图片按比例大小压缩方法(根据Bitmap图片压缩)
     *
     * @param image
     * @return
     */
    public static Bitmap getImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        if (isBm != null) {
            try {
                isBm.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (image != null && !image.isRecycled()) {
            image.recycle();
            image = null;
        }
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }


    /*--------------------------------圆-------------------------------------*/

    /**
     * 设置背景为圆角
     *
     * @param bitmap
     * @param pixels
     * @return
     */
    public static Bitmap removeYuanjiao(Bitmap bitmap, int pixels) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap creBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(creBitmap);

        Paint paint = new Paint();
        float roundPx = pixels;
        RectF rectF = new RectF(0, 0, bitmap.getWidth() - pixels, bitmap.getHeight() - pixels);
        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(bitmap, 0, 0, paint);
        if (!bitmap.isRecycled())
            bitmap.recycle();

        return creBitmap;
    }

    /**
     * 100*100圆形头像用
     *
     * @param bmp
     * @return
     */
    public static Bitmap getCropped2Bitmap(Bitmap bmp) {
        int radius = 50;
        Bitmap scaledSrcBmp = null;
        int diameter = radius * 2;
        if (bmp.getWidth() != diameter || bmp.getHeight() != diameter) {
            scaledSrcBmp = Bitmap.createScaledBitmap(bmp, diameter, diameter, false);
        } else {
            scaledSrcBmp = bmp;
        }
        Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(), scaledSrcBmp.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(), scaledSrcBmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(scaledSrcBmp.getWidth() / 2, scaledSrcBmp.getHeight() / 2, scaledSrcBmp.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);

        return output;
    }

    /**
     * 将图像裁剪成圆形
     *
     * @param bitmap
     * @return
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        return output;
    }

    // 将图片变成带圆边的圆形图片
    public static Bitmap getRoundBitmap(Bitmap bitmap, int width, int height) {
        if (bitmap == null) {
            return null;
        }
        // 将图片变成圆角
        Bitmap roundBitmap = Bitmap.createBitmap(width, height,
                Config.ARGB_8888);
        Canvas canvas = new Canvas(roundBitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int len = (width > height) ? height : width;
        canvas.drawCircle(width / 2, height / 2, len / 2 - 8, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, len, len, true);
        canvas.drawBitmap(scaledBitmap, 0, 0, paint);
        // 将图片加圆边
        Bitmap outBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        canvas = new Canvas(outBitmap);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xffffffff);
        canvas.drawCircle(width / 2, height / 2, len / 2 - 4, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        canvas.drawBitmap(roundBitmap, 0, 0, paint);
        bitmap.recycle();
        bitmap = null;
        roundBitmap.recycle();
        roundBitmap = null;
        scaledBitmap.recycle();
        scaledBitmap = null;
        return outBitmap;
    }

    /**
     * 图片转圆角
     *
     * @param bitmap 需要转的bitmap
     * @param pixels 转圆角的弧度
     * @return 转圆角的bitmap
     * <p/> 已测试 <p/>
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return output;
    }

    // 将图片变成带圆边的圆形图片
    public static Bitmap getRoundBitmap(Bitmap bitmap, int width, int height,
                                        int color) {
        if (bitmap == null) {
            return null;
        }
        // 将图片变成圆角
        Bitmap roundBitmap = Bitmap.createBitmap(width, height,
                Config.ARGB_8888);
        Canvas canvas = new Canvas(roundBitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int len = (width > height) ? height : width;
        canvas.drawCircle(width / 2, height / 2, len / 2 - 8, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, len, len, true);
        canvas.drawBitmap(scaledBitmap, 0, 0, paint);
        // 将图片加圆边
        Bitmap outBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        canvas = new Canvas(outBitmap);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        canvas.drawCircle(width / 2, height / 2, len / 2 - 4, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        canvas.drawBitmap(roundBitmap, 0, 0, paint);
        bitmap.recycle();
        bitmap = null;
        roundBitmap.recycle();
        roundBitmap = null;
        scaledBitmap.recycle();
        scaledBitmap = null;
        return outBitmap;
    }

    /**
     * 获取指定的圆角图片
     *
     * @param bitmap
     * @return
     */
    public static Bitmap getRadiusBitmap(Bitmap bitmap) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xffffffff);
        Bitmap radiusBitmap = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(radiusBitmap);
        RectF rectF = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
        canvas.drawRoundRect(rectF, 7, 7, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return radiusBitmap;
    }

    // 获得指定大小的圆边的bitmap数组
    public static ArrayList<Bitmap> getRadiusBitmapList(String[] pathArray,
                                                        int size, int len, float radius, int color) {
        Bitmap canvasBitmap = null;
        Canvas canvas = null;
        Paint paint = null;
        RectF rectF = new RectF(0, 0, len - radius, len - radius);
        File file = null;
        FileInputStream fis = null;
        Bitmap bitmap = null;
        Bitmap scaledBitmap = null;

        ArrayList<Bitmap> list = new ArrayList<Bitmap>();
        for (int i = 0; i < pathArray.length; i++) {
            file = new File(pathArray[i]);
            if (!file.exists())
                continue;
            try {
                fis = new FileInputStream(file);
                bitmap = BitmapFactory.decodeStream(fis);
                if (bitmap != null) {
                    canvasBitmap = Bitmap.createBitmap(len, len,
                            Config.ARGB_8888);
                    canvas = new Canvas(canvasBitmap);
                    paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    paint.setColor(color);
                    canvas.drawRoundRect(rectF, radius, radius, paint);
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

                    scaledBitmap = Bitmap.createScaledBitmap(bitmap, len, len,
                            true);
                    canvas.drawBitmap(scaledBitmap, 0, 0, paint);
                    list.add(canvasBitmap);
                }
            } catch (FileNotFoundException e) {
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e1) {
                    }
                }
            }
            if (list.size() == size)
                break;
        }
        if (scaledBitmap != null && !scaledBitmap.isRecycled()) {
            scaledBitmap.recycle();
            scaledBitmap = null;
        }
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        return list;
    }

    /*----------------------------------------Bitmap转换-------------------------------------------*/

    /**
     * Bitmap --> byte[]
     *
     * @param bmp
     * @return
     */
    private static byte[] readBitmap(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        try {
            baos.flush();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    /**
     * Bitmap --> byte[]
     *
     * @param buffer
     * @param size
     * @return
     */
    public static byte[] readBitmapFromBuffer(byte[] buffer, float size) {
        return readBitmap(convertToThumb(buffer, size));
    }

    /**
     * 通过资源id转化成Bitmap
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap ReadBitmapById(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;

        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /**
     * 通过资源id转化成Bitmap 全屏显示
     *
     * @param context
     * @param drawableId
     * @param screenWidth
     * @param screenHight
     * @return
     */
    public static Bitmap ReadBitmapById(Context context, int drawableId, int screenWidth, int screenHight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Config.ARGB_8888;
        options.inInputShareable = true;
        options.inPurgeable = true;

        InputStream stream = context.getResources().openRawResource(drawableId);
        Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
        return getBitmap(bitmap, screenWidth, screenHight);
    }

    /*----------------------图片剪切------------------------*/

    /**
     * 按正方形裁切图片
     */
    public static Bitmap ImageCrop(Bitmap bitmap, boolean isRecycled) {
        if (bitmap == null) {
            return null;
        }

        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();

        int wh = w > h ? h : w;// 裁切后所取的正方形区域边长

        int retX = w > h ? (w - h) / 2 : 0;// 基于原图，取正方形左上角x坐标
        int retY = w > h ? 0 : (h - w) / 2;

        Bitmap bmp = Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null, false);
        if (isRecycled && bitmap != null && !bitmap.equals(bmp) && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }

        return bmp;
    }

    /**
     * 按长方形裁切图片
     *
     * @param bitmap
     * @return
     */
    public static Bitmap ImageCropWithRect(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();

        int nw, nh, retX, retY;
        if (w > h) {
            nw = h / 2;
            nh = h;
            retX = (w - nw) / 2;
            retY = 0;
        } else {
            nw = w / 2;
            nh = w;
            retX = w / 4;
            retY = (h - w) / 2;
        }

        // 下面这句是关键
        Bitmap bmp = Bitmap.createBitmap(bitmap, retX, retY, nw, nh, null, false);
        if (bitmap != null && !bitmap.equals(bmp) && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        return bmp;// Bitmap.createBitmap(bitmap, retX, retY, nw, nh, null,
        // false);
    }

    /**
     * 按照一定的宽高比例裁剪图片
     *
     * @param bitmap
     * @param num1   长边的比例
     * @param num2   短边的比例
     * @return
     */
    public static Bitmap ImageCrop(Bitmap bitmap, int num1, int num2,
                                   boolean isRecycled) {
        if (bitmap == null) {
            return null;
        }
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();
        int retX, retY;
        int nw, nh;
        if (w > h) {
            if (h > w * num2 / num1) {
                nw = w;
                nh = w * num2 / num1;
                retX = 0;
                retY = (h - nh) / 2;
            } else {
                nw = h * num1 / num2;
                nh = h;
                retX = (w - nw) / 2;
                retY = 0;
            }
        } else {
            if (w > h * num2 / num1) {
                nh = h;
                nw = h * num2 / num1;
                retY = 0;
                retX = (w - nw) / 2;
            } else {
                nh = w * num1 / num2;
                nw = w;
                retY = (h - nh) / 2;
                retX = 0;
            }
        }
        Bitmap bmp = Bitmap.createBitmap(bitmap, retX, retY, nw, nh, null,
                false);
        if (isRecycled && bitmap != null && !bitmap.equals(bmp)
                && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        return bmp;// Bitmap.createBitmap(bitmap, retX, retY, nw, nh, null,
        // false);
    }

    /*---------------------------旋转---------------------------------*/

    /**
     * 读取图片旋转的角度
     *
     * @param filename
     * @return
     */
    public static int readPictureDegree(String filename) {
        int rotate = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(filename);
            int result = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            switch (result) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rotate;
    }

    /**
     * 旋转图片
     *
     * @param angle
     * @param bitmap
     * @return
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);

        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (resizedBitmap != bitmap && bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }

        return resizedBitmap;
    }

    /**
     * 读取图片旋转的角度
     *
     * @param filename
     * @return
     */
    public static void setPictureDegree(String filename, int degree) {
        try {
            if (degree == 0) {
                return;
            }

            int rotate = ExifInterface.ORIENTATION_UNDEFINED;
            switch (degree) {
                case 90:
                    rotate = ExifInterface.ORIENTATION_ROTATE_90;
                    break;
                case 180:
                    rotate = ExifInterface.ORIENTATION_ROTATE_180;
                    break;
                case 270:
                    rotate = ExifInterface.ORIENTATION_ROTATE_270;
                    break;
                default:
                    break;
            }

            ExifInterface exifInterface = new ExifInterface(filename);
            exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(rotate));
            exifInterface.saveAttributes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*---------------------------SDCard--------------------------------------*/
    private static int FREE_SD_SPACE_NEEDED_TO_CACHE = 1;
    private static int MB = 1024 * 1024;

    /**
     * 保存Bitmap到sdcard
     *
     * @param dir
     * @param bm
     * @param filename
     * @param quantity
     */
    public static boolean saveBmpToSd(String dir, Bitmap bm, String filename,
                                      int quantity, boolean recyle) {
        boolean ret = true;
        if (bm == null) {
            return false;
        }

        if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            bm.recycle();
            bm = null;
            return false;
        }

        File dirPath = new File(dir);

        if (!exists(dir)) {
            dirPath.mkdirs();
        }

        if (!dir.endsWith(File.separator)) {
            dir += File.separator;
        }

        File file = new File(dir + filename);
        OutputStream outStream = null;
        try {
            file.createNewFile();
            outStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, quantity, outStream);
            outStream.flush();
            outStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            ret = false;
        } catch (IOException e) {
            e.printStackTrace();
            ret = false;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            ret = false;
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (recyle && !bm.isRecycled()) {
                bm.recycle();
                bm = null;
                Log.e(TAG, "saveBmpToSd, recyle");
            }
        }

        return ret;
    }

    /**
     * 保存Bitmap到sdcard
     *
     * @param dir
     * @param bm
     * @param filename
     * @param quantity
     */
    public static boolean saveBmpToSd(String dir, Bitmap bm, String filename, int quantity) {
        return saveBmpToSd(dir, bm, filename, quantity, false);
    }

    /**
     * 保存Bitmap到sdcard
     *
     * @param dir
     * @param srcFile
     * @param filename
     * @param quantity
     */
    public static boolean saveBmpToSd(String dir, String srcFile, String filename, int quantity) {
        if (srcFile == null) {
            return false;
        }
        Bitmap bmp = BitmapFactory.decodeFile(srcFile);
        return saveBmpToSd(dir, bmp, filename, quantity);
    }

    /**
     * 保存Bitmap到sdcard
     *
     * @param dir
     * @param srcFile
     * @param filename
     * @param quantity
     */
    public static boolean saveBmpToSd(String dir, String srcFile, String filename, int quantity, boolean recyle) {
        if (srcFile == null) {
            return false;
        }
        Bitmap bmp = BitmapFactory.decodeFile(srcFile);
        return saveBmpToSd(dir, bmp, filename, quantity, recyle);
    }

    /**
     * 保存Bitmap到sdcard
     *
     * @param dir
     * @param srcFile
     * @param filename
     * @param quantity
     */
    public static boolean saveBmpToSd(String dir, String srcFile,
                                      String filename, int quantity, float size, boolean recyle) {
        if (srcFile == null) {
            return false;
        }
        Bitmap bmp = convertToThumb(readFileToBuffer(srcFile), size);
        return saveBmpToSd(dir, bmp, filename, quantity, recyle);
    }

    /**
     * 保存Bitmap到sdcard
     *
     * @param dir
     * @param srcFile
     * @param filename
     * @param quantity
     */
    public static boolean saveBmpToSd(String dir, String srcFile,
                                      String filename, int quantity, float size) {
        if (srcFile == null) {
            return false;
        }
        Bitmap bmp = convertToThumb(readFileToBuffer(srcFile), size);
        return saveBmpToSd(dir, bmp, filename, quantity);
    }

    /**
     * 保存Bitmap到sdcard
     *
     * @param dir
     * @param bmp
     * @param filename
     * @param quantity
     */
    public static boolean saveBmpToSd(String dir, Bitmap bmp, String filename,
                                      int quantity, float size) {
        if (bmp == null) {
            return false;
        }
        bmp = convertToThumb(readBitmap(bmp), size);
        return saveBmpToSd(dir, bmp, filename, quantity);
    }

    /**
     * 保存Bitmap到sdcard
     *
     * @param filePath
     * @param bm
     * @param quantity
     */
    public static boolean saveBmpToSd(String filePath, Bitmap bm, int quantity) {
        if (filePath == null) {
            return false;
        }

        int end = filePath.lastIndexOf(File.separator);
        String dir = filePath.substring(0, end);
        String filename = filePath.substring(end);

        return saveBmpToSd(dir, bm, filename, quantity);
    }

    /**
     * 获取sdcard路径
     *
     * @return
     */
    public static String getSdcardPath() {
        return Environment.getExternalStorageDirectory().getPath()
                + File.separator;
    }

    /**
     * 验证文件是否存在
     *
     * @param url
     * @return
     */
    public static boolean exists(String url) {
        File file = new File(url);

        return file.exists();
    }

    /**
     * 检测sdcard可用空间
     *
     * @return
     */
    public static int freeSpaceOnSd() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
                .getPath());

        double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat
                .getBlockSize()) / MB;

        return (int) sdFreeMB;
    }


    /**
     * @param fileName
     * @return
     * @description: 通过文件路径将对应文件转为byte[]
     */
    public static byte[] getByte(String fileName) {
        if (fileName == null || "".equals(fileName)) {
            return new byte[0];
        }
        File file = new File(fileName);
        if (file.exists()) {
            try {
                FileInputStream fin = new FileInputStream(fileName);
                int length = fin.available();
                byte[] buffer = new byte[length];
                fin.read(buffer);
                // res = EncodingUtils.getString(buffer, "UTF-8");
                fin.close();
                return buffer;
            } catch (Exception e) {
                Log.e(TAG, "getByte fail:" + fileName);
                return new byte[0];
            }
        } else {
            Log.e(TAG, "getByte file no exists :" + fileName);
            return new byte[0];
        }

    }

    /**
     * 将图片、语音或者文件读入到byte缓冲数组
     *
     * @param filePath
     * @return
     */
    public static byte[] readFileToBuffer(String filePath) {
        if (filePath == null || filePath.trim().equals("")) {
            Log.e(TAG, "readFileToBuffer, path is null:" + filePath);
            return null;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            Log.e(TAG, "readFileToBuffer, file is not exists:" + filePath);
            return null;
        }

        byte[] buffer = new byte[(int) file.length()];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(buffer);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return buffer;
    }
}