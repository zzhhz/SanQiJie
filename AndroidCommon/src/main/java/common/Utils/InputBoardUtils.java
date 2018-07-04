package common.Utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Beidouht on 2017/4/14.
 */

public class InputBoardUtils {

    /**
     * 打开输入法面板
     *
     * @param activity
     */
    public static void showInputMethod(final Activity activity) {
        if (activity == null) return;
        InputMethodManager inputMethodManager = ((InputMethodManager) activity.getSystemService(
                Activity.INPUT_METHOD_SERVICE));
        if (activity.getCurrentFocus() != null) {
            inputMethodManager.showSoftInput(activity.getCurrentFocus(), 0);
        }
    }

    /**
     * 关闭输入法面板
     */
    public static void hideInputMethod(Activity activity) {
        if (activity == null || activity.getCurrentFocus() == null)
            return;
        InputMethodManager inputMethodManager = ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE));
        if (inputMethodManager.isActive()) {
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
