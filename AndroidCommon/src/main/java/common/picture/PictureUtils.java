package common.picture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;

/**
 * Created by Beidouht on 2017/7/4.
 */

public class PictureUtils {

    public static void loadPicture(Context context, String url, ImageView imageView, int resouce) {
        if (TextUtils.equals("", url)) {
            imageView.setImageResource(resouce);
            return;
        }
        Picasso.with(context).load(url)
//                .networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE)
//                .memoryPolicy(NO_CACHE, NO_STORE)
                .config(Bitmap.Config.RGB_565)
                .placeholder(resouce)
                .error(resouce)
                .into(imageView);

    }

    public static void loadPictureCircle(Context context, String url, ImageView imageView, int resouce) {
        if (TextUtils.equals("", url)) {
            imageView.setImageResource(resouce);
            return;
        }
        Picasso.with(context).load(url)
//                .networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE)
//                .memoryPolicy(NO_CACHE, NO_STORE)
                .config(Bitmap.Config.RGB_565)
                .transform(new CircleTransforms(context))
                .placeholder(resouce)
                .error(resouce)
                .into(imageView);
    }

    public static void loadPictureLoc(Context context, String url, ImageView imageView, int resouce) {
        if (TextUtils.equals("", url)) {
            imageView.setImageResource(resouce);
            return;
        }
        File file = new File(url);
        Picasso.with(context).load(file)
//                .networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE)
//                .memoryPolicy(NO_CACHE, NO_STORE)
                .config(Bitmap.Config.RGB_565)
                .placeholder(resouce)
                .error(resouce)
                .into(imageView);

    }

    public static void loadPictureLocSize(Context context, String url, ImageView imageView, int resouce, int w, int h) {
        if (TextUtils.equals("", url)) {
            imageView.setImageResource(resouce);
            return;
        }
        File file = new File(url);
        Picasso.with(context).load(file)
//                .networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE)
//                .memoryPolicy(NO_CACHE, NO_STORE)
                .resize(w, h)
                .config(Bitmap.Config.RGB_565)
                .placeholder(resouce)
                .error(resouce)
                .into(imageView);

    }

    public static void loadPictureReSize(Context context, String url, ImageView imageView, int w, int h, int resouce) {
        if (TextUtils.equals("", url)) {
            imageView.setImageResource(resouce);
            return;
        }
        Picasso.with(context).load(url)
                .resize(w, h)
                .placeholder(resouce)
                .error(resouce)
                .into(imageView);
    }

    public static void loadRoundImageView(Context context, String url, ImageView imageView, int resouce) {
        if (TextUtils.equals("", url)) {
            imageView.setImageResource(resouce);
            return;
        }
        Picasso.with(context).load(url)
                .transform(new RoundedTransformationBuilder(context))
                .placeholder(resouce)
                .error(resouce)

                .into(imageView);
    }

    public static class RoundedTransformationBuilder implements Transformation {
        private Context mContext;

        public RoundedTransformationBuilder(Context context) {
            mContext = context;
        }

        @Override
        public Bitmap transform(Bitmap source) {

            int widthLight = source.getWidth();
            int heightLight = source.getHeight();
            int radius = 15; // 圆角半径

            Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(output);
            Paint paintColor = new Paint();
            paintColor.setFlags(Paint.ANTI_ALIAS_FLAG);

            RectF rectF = new RectF(new Rect(0, 0, widthLight, heightLight));

            canvas.drawRoundRect(rectF, radius, radius, paintColor);
//        canvas.drawRoundRect(rectF, widthLight / 5, heightLight / 5, paintColor);

            Paint paintImage = new Paint();
            paintImage.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
            canvas.drawBitmap(source, 0, 0, paintImage);
            source.recycle();
            return output;
        }

        @Override
        public String key() {
            return "roundcorner";
        }
    }

}
