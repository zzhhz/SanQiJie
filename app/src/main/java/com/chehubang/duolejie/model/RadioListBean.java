package com.chehubang.duolejie.model;

/**
 * Created by Thinkpad on 2017/12/14.
 */

public class RadioListBean {


    /**
     * good_name : http://192.168.3.25:8080   /GetTreasureAppJinDong/FtpUpload/140.png
     * good_period : 3
     * id : 522860656942
     * nick_name : 张三丰
     */

    private String coupon_img;
    private String id;
    private String period;
    private String coupon_num;
    private String nick_name;


    public String getCoupon_num() {
        return coupon_num;
    }

    public void setCoupon_num(String coupon_num) {
        this.coupon_num = coupon_num;
    }

    public String getCoupon_img() {
        return coupon_img;
    }

    public void setCoupon_img(String coupon_img) {
        this.coupon_img = coupon_img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id

                = id;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }
}
