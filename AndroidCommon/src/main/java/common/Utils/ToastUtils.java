package common.Utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Bob on 2016/8/25.
 */

public class ToastUtils {

    private static Toast mtoast = null;


    public static void showToast(Context context, String text) {
        if (mtoast == null) {
            mtoast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            mtoast.show();
        } else {
            mtoast.setText(text);
            mtoast.show();
        }
    }

    public static void centerToastWhite(Context context, String text) {
        if (mtoast == null) {
            mtoast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            mtoast.setGravity(Gravity.CENTER, 0, 0);
            mtoast.show();
        } else {
            mtoast.setText(text);
            mtoast.setGravity(Gravity.CENTER, 0, 0);
            mtoast.show();
        }
    }
}
