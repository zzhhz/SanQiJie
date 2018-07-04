package common.Utils;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Activity跳转工具管理
 *
 */
public class ActivityUtils {
	/**
	 * 带参数的Activity跳转：
	 * <p> 参数都封装在bundle里面
	 * @param activity 当前的activity
	 * @param targetActivity 目标activity
	 * @param bundle 参数
	 */
	public static void switchTo(Activity activity, Class<? extends Activity> targetActivity, Bundle bundle){
		Intent intent =new Intent(activity,targetActivity);
		intent.putExtras(bundle);
		switchTo(activity, intent);
	}
	/**
	 * 不带参数的Activity跳转：
	 * <p> 参数都封装在bundle里面
	 * @param activity 当前的activity
	 * @param class1 目标activity
	 */
	public static void switchTo(Activity activity, Class<? extends Activity> class1){
		Intent intent =new Intent(activity,class1);
		switchTo(activity, intent);
	}

	/**
	 * 不带参数的Activity跳转：
	 * <p> 参数都封装在bundle里面
	 * @param activity 当前的activity
	 * @param targetActivity 目标activity
	 */
	public static void switchTo(Activity activity, Class<? extends Activity> targetActivity, boolean isJump){
		if(isJump){
			Intent intent =new Intent(activity,targetActivity);
			switchTo(activity, intent);
		}
	}
	/**
	 * 跳转到指定的activity，并需要返回结果
	 * @param activity
	 * @param targetActivity
	 * @param requestCode
	 */
	public static void switchToForResult(Activity activity, Class<? extends Activity> targetActivity, int requestCode){
		Intent intent =new Intent(activity,targetActivity);
		switchToForResult(activity, intent, requestCode);
	}
	/**
	 *  跳转到指定的activity，并需要返回结果
	 * @param activity
	 * @param targetActivity
	 * @param bundle
	 * @param requestCode
	 */
	public static void switchToForResult(Activity activity, Class<? extends Activity> targetActivity, Bundle bundle, int requestCode){
		Intent intent =new Intent(activity,targetActivity);
		intent.putExtras(bundle);
		switchToForResult(activity, intent, requestCode);
	}

	/**
	 *  跳转到指定的activity，并需要返回结果
	 * @param activity
	 * @param intent
	 * @param requestCode
	 */
	public static void switchToForResult(Activity activity, Intent intent, int requestCode){
		activity.startActivityForResult(intent, requestCode);
	}
	/**
	 * 根据给定的Intent进行Activity跳转
	 * @param activity
	 * @param intent
	 */
	public static void switchTo(Activity activity, Intent intent){
		activity.startActivity(intent);
	}
}
