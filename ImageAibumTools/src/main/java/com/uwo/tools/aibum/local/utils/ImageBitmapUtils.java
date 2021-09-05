package com.uwo.tools.aibum.local.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import com.edmodo.cropper.util.ComuteSizeUtils;

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
 * bitmap旋转	setRotationBitmap 90°
 * 将图片旋转成正位
 * bitmap圆角/圆形	getRoundedBitmap
 * bitmap大图片处理
 * bitmap计算所在空间大小	computeSampleSize
 * 提取图像Alpha位图	getAlphaBitmap
 * 图片灰化处理	getGrayBitmap
 * 将View转换为Bitmap       	getDrawingCache
 * 设置图片压缩参数	setOption
 * 图像缩放	getScaleBitmap
 * 图片旋转	getRotatedBitmap
 * 图像倾斜	getScrewBitmap
 * 图像倒影	getReflectedBitmap
 * 图像剪切	cropper
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

    /**
     * 图片旋转
     * Sets a Bitmap and initializes the image rotation according to the EXIT data.
     * 设置和初始化位图图像旋转根据出口数据。
     * <p/>
     * The EXIF can be retrieved by doing the following:
     * <code>ExifInterface exif = new ExifInterface(path);</code>
     *
     * @param bitmap the original bitmap to set; if null, this
     * @param exif   the EXIF information about this bitmap; may be null
     */
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public Bitmap setRotationBitmap(Bitmap bitmap, ExifInterface exif) {
        if (bitmap == null) {
            return null;
        }

        if (exif == null) {
            return bitmap;
        }

        final Matrix matrix = new Matrix();
        final int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
        int rotate = -1;

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotate = 270;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotate = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotate = 90;
                break;
        }

        if (rotate == -1) {
            return bitmap;
        } else {
            matrix.postRotate(rotate);
            final Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return rotatedBitmap;
        }
    }

    /**
     * 设置压缩参数
     *
     * @param inSampleSize 压缩尺寸 为1时，图片压缩为原来的1/2
     * @return
     */
    public BitmapFactory.Options setOption(int inSampleSize) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        opts.inJustDecodeBounds = true;
        if (inSampleSize == 1) {
            opts.inSampleSize = ComuteSizeUtils.computeSampleSize(opts, -1, 128 * 128);
        } else {
            opts.inSampleSize = 2 * inSampleSize;
        }
        opts.inJustDecodeBounds = false;
        return opts;
    }

    /**
     * 将View转换为Bitmap
     * mImageView1.setImageResource(R.drawable.android);
     * mImageView2.setImageResource(R.drawable.pet);
     * getDrawingCache(mImageView1, mImageView2, viewId);
     */
    public void getDrawingCache(final ImageView sourceImageView, final ImageView destImageView, final int viewId) {

        final long DELAY_TIME = 1000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                //开启bitmap缓存
                sourceImageView.setDrawingCacheEnabled(true);
                //获取bitmap缓存
                Bitmap mBitmap = sourceImageView.getDrawingCache();
                //显示 bitmap
                destImageView.setImageBitmap(mBitmap);

//                                Bitmap mBitmap = sourceImageView.getDrawingCache();
//                                Drawable drawable = (Drawable) new BitmapDrawable(mBitmap);
//                                destImageView.setImageDrawable(drawable);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        //不再显示bitmap缓存
                        //destImageView.setImageBitmap(null);
                        destImageView.setImageResource(viewId);

                        //使用这句话而不是用上一句话是错误的，空指针调用
                        //destImageView.setBackgroundDrawable(null);

                        //关闭bitmap缓存
                        sourceImageView.setDrawingCacheEnabled(false);
                        //释放bitmap缓存资源
                        sourceImageView.destroyDrawingCache();
                    }
                }, DELAY_TIME);
            }
        }, DELAY_TIME);
    }

    //图片圆角处理

    /**
     * 图片圆角处理
     * http://blog.csdn.net/dahuaishu2010_/article/details/28622417
     *
     * @param context
     * @param viewId
     * @return
     */
    public Bitmap getRoundedBitmap(Context context, int viewId) {
        //在这里更改是资源文件/bitmap/图片文件
        Bitmap mBitmap = BitmapFactory.decodeResource(context.getResources(), viewId);
        //创建新的位图
        Bitmap bgBitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        //把创建的位图作为画板
        Canvas mCanvas = new Canvas(bgBitmap);

        Paint mPaint = new Paint();
        Rect mRect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        RectF mRectF = new RectF(mRect);
        //设置圆角半径为20
        float roundPx = 15;
        mPaint.setAntiAlias(true);
        //先绘制圆角矩形
        mCanvas.drawRoundRect(mRectF, roundPx, roundPx, mPaint);

        //设置图像的叠加模式
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //绘制图像
        mCanvas.drawBitmap(mBitmap, mRect, mRect, mPaint);

        return bgBitmap;
    }

    /**
     * 图片灰化处理
     *
     * @param context
     * @param viewId
     * @return
     */
    public Bitmap getGrayBitmap(Bitmap mBitmap) {
        Bitmap mGrayBitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(mGrayBitmap);
        Paint mPaint = new Paint();
        //创建颜色变换矩阵
        ColorMatrix mColorMatrix = new ColorMatrix();
        //设置灰度影响范围
        mColorMatrix.setSaturation(0);
        //创建颜色过滤矩阵
        ColorMatrixColorFilter mColorFilter = new ColorMatrixColorFilter(mColorMatrix);
        //设置画笔的颜色过滤矩阵
        mPaint.setColorFilter(mColorFilter);
        //使用处理后的画笔绘制图像
        mCanvas.drawBitmap(mBitmap, 0, 0, mPaint);
        if (mBitmap != null) {
            mBitmap.recycle();
        }
        return mGrayBitmap;
    }

    /**
     * 提取图像Alpha位图
     * http://blog.csdn.net/dahuaishu2010_/article/details/28622417
     *
     * @param mBitmap
     * @return
     */
    public Bitmap getAlphaBitmap(Bitmap mBitmap) {
        //BitmapDrawable的getIntrinsicWidth（）方法，Bitmap的getWidth（）方法
        //注意这两个方法的区别
        //Bitmap mAlphaBitmap = Bitmap.createBitmap(mBitmapDrawable.getIntrinsicWidth(), mBitmapDrawable.getIntrinsicHeight(), Config.ARGB_8888);
        Bitmap mAlphaBitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas mCanvas = new Canvas(mAlphaBitmap);
        Paint mPaint = new Paint();

        mPaint.setColor(Color.BLUE);
        //从原位图中提取只包含alpha的位图
        Bitmap alphaBitmap = mBitmap.extractAlpha();
        //在画布上（mAlphaBitmap）绘制alpha位图
        mCanvas.drawBitmap(alphaBitmap, 0, 0, mPaint);
        if (mBitmap != null) {
            mBitmap.recycle();
        }
        return mAlphaBitmap;
    }

    /**
     * 图像缩放
     *
     * @return
     */
    public Bitmap getScaleBitmap(Bitmap mBitmap) {
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(0.75f, 0.75f);
        Bitmap mScaleBitmap = Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, true);
        if (mBitmap != null) {
            mBitmap.recycle();
        }
        return mScaleBitmap;
    }

    /**
     * 图片旋转
     *
     * @return
     */
    public Bitmap getRotatedBitmap(Bitmap mBitmap) {
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.preRotate(45);
        Bitmap mRotateBitmap = Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, true);
        if (mBitmap != null) {
            mBitmap.recycle();
        }
        return mRotateBitmap;
    }

    /**
     * 图像倾斜
     */
    public Bitmap getScrewBitmap(Bitmap mBitmap) {
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.preSkew(1.0f, 0.15f);
        Bitmap mScrewBitmap = Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, true);
        if (mBitmap != null) {
            mBitmap.recycle();
        }
        return mScrewBitmap;
    }

    /**
     * 图像倒影
     *
     * @return
     */
    private Bitmap getReflectedBitmap(Bitmap mBitmap) {
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();

        Matrix matrix = new Matrix();
        // 图片缩放，x轴变为原来的1倍，y轴为-1倍,实现图片的反转
        matrix.preScale(1, -1);

        //创建反转后的图片Bitmap对象，图片高是原图的一半。
        //Bitmap mInverseBitmap = Bitmap.createBitmap(mBitmap, 0, height/2, width, height/2, matrix, false);
        //创建标准的Bitmap对象，宽和原图一致，高是原图的1.5倍。
        //注意两种createBitmap的不同
        //Bitmap mReflectedBitmap = Bitmap.createBitmap(width, height*3/2, Config.ARGB_8888);

        Bitmap mInverseBitmap = Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false);
        Bitmap mReflectedBitmap = Bitmap.createBitmap(width, height * 2, Bitmap.Config.ARGB_8888);

        // 把新建的位图作为画板
        Canvas mCanvas = new Canvas(mReflectedBitmap);
        //绘制图片
        mCanvas.drawBitmap(mBitmap, 0, 0, null);
        mCanvas.drawBitmap(mInverseBitmap, 0, height, null);

        //添加倒影的渐变效果
        Paint mPaint = new Paint();
        Shader mShader = new LinearGradient(0, height, 0, mReflectedBitmap.getHeight(), 0x70ffffff, 0x00ffffff, Shader.TileMode.MIRROR);
        mPaint.setShader(mShader);
        //设置叠加模式
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        //绘制遮罩效果
        mCanvas.drawRect(0, height, width, mReflectedBitmap.getHeight(), mPaint);
        if (mBitmap != null) {
            mBitmap.recycle();
        }
        return mReflectedBitmap;
    }

    /**
     * 图片剪切
     */
    public void cropper(Bitmap bitmap) {
//        创建一个新位图作为画板，然后把原图像画到新位图上面
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawBitmap(bitmap, 0, 0, mPaint);

        // 绘制一个剪切区域
        int deltX = 76;
        int deltY = 98;
        DashPathEffect dashStyle = new DashPathEffect(new float[]{10, 5, 5, 5}, 2);//创建虚线边框样式
        RectF faceRect = new RectF(0, 0, 88, 106);
        float[] faceCornerii = new float[]{30, 30, 30, 30, 75, 75, 75, 75};
        Paint mPaint1 = new Paint();//创建画笔
        mPaint1.setColor(0xFF6F8DD5);
        mPaint1.setStrokeWidth(6);
        mPaint1.setPathEffect(dashStyle);
        Path clip = new Path();//创建路径
        clip.reset();
        clip.addRoundRect(faceRect, faceCornerii, Path.Direction.CW);//添加圆角矩形路径
        canvas.save();//保存画布
        canvas.translate(deltX, deltY);
        canvas.clipPath(clip, Region.Op.DIFFERENCE);
        canvas.drawColor(0xDF222222);
        canvas.drawPath(clip, mPaint1);//绘制路径
        canvas.restore();

        // 从原图像上获取指定区域的图像，并绘制到屏幕上
        Rect srcRect = new Rect(0, 0, 88, 106);
        srcRect.offset(deltX, deltY);
        PaintFlagsDrawFilter dfd = new PaintFlagsDrawFilter(Paint.ANTI_ALIAS_FLAG, Paint.FILTER_BITMAP_FLAG);
        canvas.setDrawFilter(dfd);
        canvas.clipPath(clip);//使用路径剪切画布
        canvas.drawBitmap(bitmap, srcRect, faceRect, mPaint1);
    }
}
