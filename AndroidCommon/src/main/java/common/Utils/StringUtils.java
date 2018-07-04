package common.Utils;

import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.widget.EditText;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    private final static Pattern emailer = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

    private final static Pattern IMG_URL = Pattern.compile(".*?(gif|jpeg|png|jpg|bmp)");

    private final static Pattern URL = Pattern
            .compile("^(https|http)://.*?$(net|com|.com.cn|org|me|)");

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    private static final int HOUR = 60 * 60 * 1000;
    private static final int MIN = 60 * 1000;
    private static final int SEC = 1000;

    /**
     * 把duration数值转换为字符串格式，ec.01:02:03， 02:03
     */
    public static String formatDuration(int duration) {
        int hour = duration / HOUR;

        int min = duration % HOUR / MIN;

        int sec = duration % MIN / SEC;

        if (hour == 0) {
            // 没有小时数，02:03
            return String.format("%02d:%02d", min, sec);
        } else {
            // 有小时数，01:02:03
            return String.format("%02d:%02d:%02d", hour, min, sec);
        }
    }

    /**
     * 计算两个时间之间相差多少时间
     *
     * @param old
     * @return
     */
    public static String timeBetWeen(String old) {
        String time = "";
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date begin = null;
        try {
            begin = dfs.parse(old);
            Date date = new Date();
            Date end = dfs.parse(dfs.format(date));
            long between = (end.getTime() - begin.getTime()) / 1000;//除以1000是为了转换成秒
            long day1 = between / (24 * 3600);
            long hour1 = between % (24 * 3600) / 3600;
            long minute1 = between % 3600 / 60;
            if (day1 == 0) {
                if (hour1 == 0) {
                    if (minute1 == 0) {
                        time = "刚刚";
                    } else {
                        time = minute1 + "分钟前";
                    }
                } else {
                    time = hour1 + "小时前";
                }
            } else {
                if (day1 == 1) {
                    time = "昨天";
                } else if (day1 < 30) {
                    time = day1 + "天前";
                } else {
                    int month = (int) (day1 / 30);
                    time = month + "个月前";
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * 格式化当前系统时间为 01:01:01
     */
    public static String formatSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    public static boolean isBetWeen(String old) {
        if (isEmpty(old)) {
            return false;
        }
        try {
            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date begin = null;
            begin = dfs.parse(old);
            Date date = new Date();
            Date end = dfs.parse(dfs.format(date));
            long between = (end.getTime() - begin.getTime()) / 1000;//除以1000是为了转换成秒
            if (between > 30 * 60) {
                return false;
            } else {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 传递一个 集合** ,返回  ";" 好的 字符串
     *
     * @return
     */
    public static String splitArrayList(List<String> selectList) {
        String[] usersUuids = new String[selectList.size()];

        for (int i = 0; i < selectList.size(); i++) {
            usersUuids[i] = selectList.get(i);
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < usersUuids.length; i++) { // 把数组 给 用; 给分割下 ;
            if (i == usersUuids.length - 1) {
                sb.append(usersUuids[i]);
            } else {
                sb.append(usersUuids[i] + ";");
            }
        }
        return sb.toString();
    }

    /**
     * 判断一个url是否为图片url
     *
     * @param url
     * @return
     */
    public static boolean isImgUrl(String url) {
        if (url == null || url.trim().length() == 0)
            return false;
        return IMG_URL.matcher(url).matches();
    }

    /**
     * sdcard文件
     **/
    public static final String RES_SDCARD = "sdcard://";
    /**
     * 网络文件
     **/
    public static final String RES_HTTP = "http://";
    /**
     * 网络文件
     **/
    public static final String RES_HTTPS = "https://";

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return true：为空; false：不为空
     */
    public static boolean isEmpty(String str) {
        boolean result = false;
        if (str == null || str.trim().length() == 0 || "null".equals(str)) {
            result = true;
        }
        return result;
    }

    /**
     * 判断给定字符串是否空白串。
     * 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串;  为空返回true
     *
     * @param input
     * @return true：为空 ; false：不为空     ' ' 也返回true;
     */
    public static boolean isStrongEmpty(String input) {
        if (input == null || "".equals(input) || input.trim().length() == 0)
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /***
     *   不等于0.0
     * @param input
     * @return
     */
    public static boolean isTwoStrongEmpty(String input) {
        if (input == null || "".equals(input) || input.trim().length() == 0 || "0.0".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }


    /**
     * 字符串最大长度验证
     *
     * @param str
     * @param length 不能超过的最大值 比如  20;
     * @return true：通过；false：不通过
     */
    public static boolean maxLength(String str, int length) {
        if (isEmpty(str)) {
            return false;
        } else if (str.trim().length() <= length) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 字符串最小长度验证
     *
     * @param str
     * @param length
     * @return true：通过；false：不通过
     */
    public static boolean minLength(String str, int length) {
        if (isEmpty(str)) {
            return false;
        } else if (str.trim().length() >= length) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isPassword(String pass) {
        if (minLength(pass, 6) && maxLength(pass, 20)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断字符串是否是合法的手机号
     *
     * @param str
     * @return true：合法; false：不合法
     */
    public static boolean isPhone(String str) {
        if (isEmpty(str)) {
            return false;
        }
        //Pattern p = Pattern.compile("^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\\\d{8}$");
        //Matcher m = p.matcher(str);
        str = str.replaceAll(" ", "");
        return (str.length() == 11);
    }

    /**
     * 判断2个非空字符串是否相等
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean twoStringisEquals(String str1, String str2) {
        if (!isEmpty(str1) || !isEmpty(str2)) {
            if (isEmpty(str1) == isEmpty(str2)) {
                return true;
            }
            return false;
        }
        return str1.trim() == str2.trim();
    }

    /**
     * 是否为网络文件
     *
     * @param url
     * @return
     */
    public static boolean isNetworkFile(String url) {
        if (StringUtils.isEmpty(url)) {
            return false;
        }
        if (url.startsWith(RES_HTTP) || url.startsWith(RES_HTTPS)) {
            return true;
        }
        return false;
    }

    /**
     * 把大图URL地址转换为200的缩略图地址（和服务器约定好的）
     *
     * @param destUrl
     * @return
     */
    public static String imageUrlConvert200ImageUrl(String destUrl) {
        int fileFormat = destUrl.lastIndexOf(".");
        String fileUrl = destUrl + "_200." + destUrl.substring(fileFormat + 1, destUrl.length());
        return fileUrl;
    }


    /**
     * 判断email格式是否正确
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 以空格截取，返回list.get(0),省，list.get(1)市
     */
    public static List<String> getCity(String cityId) {
        List<String> city = new ArrayList<String>();
        int trim = cityId.indexOf(" ");
        String provice = cityId.substring(0, trim);
        String provice_ = cityId.substring(trim + 1);
        city.add(provice);
        city.add(provice_);
        return city;
    }
//	 ----------------------------------

    /**
     * 将字符串转位日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        return toDate(sdate, dateFormater.get());
    }

    public static Date toDate(String sdate, SimpleDateFormat dateFormater) {
        try {
            return dateFormater.parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String getDateString(Date date) {
        return dateFormater.get().format(date);
    }

    /**
     * 显示的样子不一样
     *
     * @param sdate
     * @return
     */
    public static String friendly_time2(String sdate) {


        String res = "";
        if (isEmpty(sdate))
            return "";

        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        String currentData = StringUtils.getDataTime("MM-dd");
        int currentDay = toInt(currentData.substring(3));
        int currentMoth = toInt(currentData.substring(0, 2));

        int sMoth = toInt(sdate.substring(5, 7));
        int sDay = toInt(sdate.substring(8, 10));
        int sYear = toInt(sdate.substring(0, 4));
        Date dt = new Date(sYear, sMoth - 1, sDay - 1);

        if (sDay == currentDay && sMoth == currentMoth) {
            res = "今天 / " + weekDays[getWeekOfDate(new Date())];
        } else if (sDay == currentDay + 1 && sMoth == currentMoth) {
            res = "昨天 / " + weekDays[(getWeekOfDate(new Date()) + 6) % 7];
        } else {
            if (sMoth < 10) {
                res = "0";
            }
            res += sMoth + "/";
            if (sDay < 10) {
                res += "0";
            }
            res += sDay + " / " + weekDays[getWeekOfDate(dt)];
        }

        return res;
    }


    /**
     * 字符串转布尔值
     *
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null || "".equals(obj))
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * 返回当前系统时间
     */
    public static String getDataTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }

    /**
     * 智能格式化 , 不同的显示样子
     */
    public static String friendly_time3(String sdate) {
        String res = "";
        if (isEmpty(sdate))
            return "";

        Date date = StringUtils.toDate(sdate);
        if (date == null)
            return sdate;

        SimpleDateFormat format = dateFormater2.get();

        if (isToday(date.getTime())) {
            format.applyPattern(isMorning(date.getTime()) ? "上午 hh:mm" : "下午 hh:mm");
            res = format.format(date);
        } else if (isYesterday(date.getTime())) {
            format.applyPattern(isMorning(date.getTime()) ? "昨天 上午 hh:mm" : "昨天 下午 hh:mm");
            res = format.format(date);
        } else if (isCurrentYear(date.getTime())) {
            format.applyPattern(isMorning(date.getTime()) ? "MM-dd 上午 hh:mm" : "MM-dd 下午 hh:mm");
            res = format.format(date);
        } else {
            format.applyPattern(isMorning(date.getTime()) ? "yyyy-MM-dd 上午 hh:mm" : "yyyy-MM-dd 下午 hh:mm");
            res = format.format(date);
        }
        return res;
    }

    /**
     * @return 判断一个时间是不是上午
     */
    public static boolean isMorning(long when) {
        android.text.format.Time time = new android.text.format.Time();
        time.set(when);

        int hour = time.hour;
        return (hour >= 0) && (hour < 12);
    }

    /**
     * @return 判断一个时间是不是今天
     */
    public static boolean isToday(long when) {
        android.text.format.Time time = new android.text.format.Time();
        time.set(when);

        int thenYear = time.year;
        int thenMonth = time.month;
        int thenMonthDay = time.monthDay;

        time.set(System.currentTimeMillis());
        return (thenYear == time.year)
                && (thenMonth == time.month)
                && (thenMonthDay == time.monthDay);
    }

    /**
     * @return 判断一个时间是不是昨天
     */
    public static boolean isYesterday(long when) {
        android.text.format.Time time = new android.text.format.Time();
        time.set(when);

        int thenYear = time.year;
        int thenMonth = time.month;
        int thenMonthDay = time.monthDay;

        time.set(System.currentTimeMillis());
        return (thenYear == time.year)
                && (thenMonth == time.month)
                && (time.monthDay - thenMonthDay == 1);
    }

    /**
     * @return 判断一个时间是不是今年
     */
    public static boolean isCurrentYear(long when) {
        android.text.format.Time time = new android.text.format.Time();
        time.set(when);

        int thenYear = time.year;

        time.set(System.currentTimeMillis());
        return (thenYear == time.year);
    }

    /**
     * 获取当前日期是星期几<br>
     *
     * @param dt
     * @return 当前日期是星期几
     */
    public static int getWeekOfDate(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return w;
    }

    /**
     * 判断给定字符串时间是否为今日
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        boolean b = false;
        Date time = toDate(sdate);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    /**
     * 返回long类型的今天的日期
     *
     * @return
     */
    public static long getToday() {
        Calendar cal = Calendar.getInstance();
        String curDate = dateFormater2.get().format(cal.getTime());
        curDate = curDate.replace("-", "");
        return Long.parseLong(curDate);
    }

    /**
     * 返回String类型的今天的日期
     *
     * @return
     */
    public static String getStringToday() {
        Calendar cal = Calendar.getInstance();
        String curDate = dateFormater2.get().format(cal.getTime());
        curDate = curDate.replace("-", ".");
        return curDate;
    }

    public static String getStringToday_() {
        Calendar cal = Calendar.getInstance();
        String curDate = dateFormater2.get().format(cal.getTime());
        return curDate;
    }

    /**
     * 返回String类型的_的日期
     *
     * @param date 传递一个 date
     * @return
     */
    public static String getStringFormatToday(Date date) {
        String curDate = dateFormater2.get().format(date);
        curDate = curDate.replace("-", ".");
        return curDate;
    }


    public static String getCurTimeStr() {
        Calendar cal = Calendar.getInstance();
        String curDate = dateFormater.get().format(cal.getTime());
        return curDate;
    }

    public static long getTimeLong(String date) {
        if (isEmpty(date)) return 0;
        try {
            Date date1 = dateFormater.get().parse(date);
            long l = date1.getTime();
            return l;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /***
     * 计算两个时间差，返回的是的秒s
     *
     * @author 火蚁 2015-2-9 下午4:50:06
     *
     * @return long
     * @param dete1
     * @param date2
     * @return
     */
    public static long calDateDifferent(String dete1, String date2) {

        long diff = 0;

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = dateFormater.get().parse(dete1);
            d2 = dateFormater.get().parse(date2);

            // 毫秒ms
            diff = d2.getTime() - d1.getTime();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return diff / 1000;
    }

    /**
     * 获取当前时间为每年第几周
     *
     * @return
     */
    public static int getWeekOfYear() {
        return getWeekOfYear(new Date());
    }

    /**
     * 获取当前时间为每年第几周
     *
     * @param date
     * @return
     */
    public static int getWeekOfYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        int week = c.get(Calendar.WEEK_OF_YEAR) - 1;
        week = week == 0 ? 52 : week;
        return week > 0 ? week : 1;
    }

    public static int[] getCurrentDate() {
        int[] dateBundle = new int[3];
        String[] temp = getDataTime("yyyy-MM-dd").split("-");

        for (int i = 0; i < 3; i++) {
            try {
                dateBundle[i] = Integer.parseInt(temp[i]);
            } catch (Exception e) {
                dateBundle[i] = 0;
            }
        }
        return dateBundle;
    }


    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }


    public static String getString(String s) {
        return s == null ? "" : s;
    }

    public static int compare_date(String date1, String date2) {
        DateFormat df = new SimpleDateFormat("hh:mm:ss");
        try {
            Date d1 = df.parse(date1);
            Date d2 = df.parse(date2);
            if (d1.getTime() > d2.getTime()) {
                return 1;
            } else if (d1.getTime() < d2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    public static int compareDate(String date1, String date2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d1 = df.parse(date1);
            Date d2 = df.parse(date2);
            if (d1.getTime() > d2.getTime()) {
                return 1;
            } else if (d1.getTime() < d2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    public static String getNextDay(String date, int type) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        try {
            date1 = df.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            calendar.add(Calendar.DAY_OF_MONTH, type);
            date1 = calendar.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String dateStr = df.format(date1);
        return dateStr;
    }

    /**
     * 2017-09-27 换成几月几日
     *
     * @param date
     * @return
     */
    public static String formatDay(String date) {
        String mon = date.substring(5, 7);
        String day = date.substring(8);
        String dateStr = mon + "月" + day + "日";
        return dateStr;
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public static boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(100);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }


    public static void setEmojiFilter(EditText et, int length) {
        InputFilter emojiFilter = new InputFilter() {
            Pattern pattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Matcher matcher = pattern.matcher(source);
                if (matcher.find()) {
                    return "";
                }
                return null;
            }
        };
        et.setFilters(new InputFilter[]{emojiFilter, new InputFilter.LengthFilter(length)});
    }

    /**
     * 获取ip地址
     *
     * @return
     */
    public static String getHostIP() {

        String hostIp = null;
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;// skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        hostIp = ia.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            Log.i("yao", "SocketException");
            e.printStackTrace();
        }
        return hostIp;
    }

    public static String formatFloat(float number) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(number);
    }


    /**
     * 获取当前的网络状态 ：没有网络0：WIFI网络1：3G网络2：2G网络3
     *
     * @param context
     * @return
     */
    public static int getAPNType(Context context) {
        int netType = 0;
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = 1;// wifi
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {
            int nSubType = networkInfo.getSubtype();
            TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS && !mTelephony.isNetworkRoaming()) {
                netType = 2;// 3G
            } else {
                netType = 3;// 2G
            }
        }
        return netType;
    }


    public static String timestampToDate(long time) {
        SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat formatNow = new SimpleDateFormat("MM-dd HH:mm");
        SimpleDateFormat formatToday = new SimpleDateFormat("HH:mm");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = formatYear.format(new Date(time));
        Calendar calendar = Calendar.getInstance();
        int nYear = calendar.get(calendar.YEAR);
        if (nYear > Integer.parseInt(date)) {   //之前年份的显示年月日，时分
            String dateFormate = format.format(new Date(time));
            return dateFormate;
        } else if (nYear == Integer.parseInt(date)) {   //本年内本周内，昨天，今天
            if (isThisTime(time, "yyyy-MM-dd")) {
                return formatToday.format(new Date(time));
            } else if (isThisWeek(time)) {
                return getWeekNumber(new Date(time)) + "\t" + formatToday.format(new Date(time));
            } else {
                String dateFormate = formatNow.format(new Date(time));
                return dateFormate;
            }
        } else {
            return "时间格式错误";
        }
    }


    /**
     * 获取星期几
     *
     * @param date
     * @return
     */
    public static String getWeekNumber(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (cal.get(7) == 1) {
            return "星期日";
        } else if (cal.get(7) == 2) {
            return "星期一";
        } else if (cal.get(7) == 3) {
            return "星期二";
        } else if (cal.get(7) == 4) {
            return "星期三";
        } else if (cal.get(7) == 5) {
            return "星期四";
        } else if (cal.get(7) == 6) {
            return "星期五";
        } else if (cal.get(7) == 7) {
            return "星期六";
        }
        return null;
    }

    //判断选择的日期是否是本周
    public static boolean isThisWeek(long time) {
        Calendar calendar = Calendar.getInstance();
        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        calendar.setTime(new Date(time));
        int paramWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        if (paramWeek == currentWeek) {
            return true;
        }
        return false;
    }

    ////判断选择的日期是否是今天
    private static boolean isThisTime(long time, String pattern) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String param = sdf.format(date);//参数时间
        String now = sdf.format(new Date());//当前时间
        if (param.equals(now)) {
            return true;
        }
        return false;
    }


}
