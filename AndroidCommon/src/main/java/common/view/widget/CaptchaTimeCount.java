package common.view.widget;


import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;


/**
 * 获取验证码
 *
 */
public class CaptchaTimeCount extends CountDownTimer {
	private TextView validate_btn;
	private Context context;
	/**
	 * 验证码倒计时
	 * @param millisInFuture
	 * @param countDownInterval
	 * @param validate_btn
	 * @param context
	 */
	public CaptchaTimeCount(long millisInFuture, long countDownInterval, TextView validate_btn, Context context) {
	    super(millisInFuture, countDownInterval);
	    this.validate_btn=validate_btn;
	    this.context=context;
	}
	@Override //开始
	public void onTick(long millisUntilFinished) {
		validate_btn.setClickable(false);
		validate_btn.setText(millisUntilFinished / 1000 + "s后重发");//  改为  60秒倒计时
//		validate_btn.setTextColor(context.getResources().getColorStateList(R.color.color_text)); //颜色
//		validate_btn.setBackgroundResource(R.drawable.ver_bg_hui);//背景颜色
	}
	@Override
	public void onFinish() {
		validate_btn.setClickable(true);
		validate_btn.setText("重新发送");//@color/color_63b953
//		validate_btn.setTextColor(context.getResources().getColorStateList(R.color.color_text));
//		validate_btn.setBackgroundResource(R.drawable.ver_bg_normal);
	}
	/**
	 * 重置
	 */
	public void reset(){
		validate_btn.setClickable(true);
		validate_btn.setText("获取验证码");
//		validate_btn.setTextColor(context.getResources().getColorStateList(R.color.color_text));
//		validate_btn.setBackgroundResource(R.drawable.ver_bg_normal);
	}
}
