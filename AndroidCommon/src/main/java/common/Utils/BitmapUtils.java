package common.Utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import common.Log.LogUtils;

/**
 * Created by ljx on 2017/9/26.
 */

public class BitmapUtils {

    public static Bitmap getBitmapForWindow(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(false);
        view.buildDrawingCache();
        view.getMatrix();
        return view.getDrawingCache();
    }

    public static Bitmap addBitmap(Bitmap first, Bitmap second) {
        int width = first.getWidth();
        int height = second.getHeight()+first.getHeight()-65;
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(first, 0, 0, null);
        canvas.drawBitmap(second,0, first.getHeight()-65, null);
        return result;
    }
    private byte[] bmpToByteArray(Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte [] bitmapByte =baos.toByteArray();
        return bitmapByte;
    }

    public static Bitmap getViewBitmap(View view) {
        int me = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(me,me);
        view.layout(0 ,0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }
    /**
     * 压缩传图片,不存相册
     *
     * @param fileDir
     * @return
     */
    public static boolean compressImage(String fileDir) {
        // 0-100 100为不压缩
        if (TextUtils.isEmpty(fileDir)) {
            return false;
        }
        File file = new File(fileDir);
        if (!file.exists()) {
            return false;
        }
        if(file.length() / 1024> 1024) {
            FileOutputStream fos = null;
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                BitmapFactory.Options options = new BitmapFactory.Options();
                int sampleSize=(int) (file.length()/102400);
                sampleSize=(int) Math.sqrt(sampleSize);
                options.inSampleSize =sampleSize ;//图片宽高都为原来的二分之一，即图片为原来的四分之一
                Bitmap bitmap = BitmapFactory.decodeFile(fileDir, options);
                int quality = 70;
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
                // 压缩图片
                while (bos.toByteArray().length / 1024 > 1024*5) {
                    bos.reset();
                    quality -= 5;
                    if (quality <= 0) {
                        return false;
                    }
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
                }
                // 生成文件
                File f = new File(fileDir);
                fos = new FileOutputStream(f);
                fos.write(bos.toByteArray());
                fos.flush();

                bitmap.recycle();

                f.renameTo(new File(fileDir));
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return true;
    }

}
