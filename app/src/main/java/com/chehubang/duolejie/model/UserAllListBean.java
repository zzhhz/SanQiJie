package com.chehubang.duolejie.model;

/**
 * Created by fuyi on 2017/12/25.
 */

public class UserAllListBean {
//     "id": 用户id,
//            "coupon_id": 代金券主键id,
//            "user_header": "用户头像",
//            "period": 期号


    private  String id;
    private  String coupon_id;
    private  String user_header;
    private  String period;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getUser_header() {
        return user_header;
    }

    public void setUser_header(String user_header) {
        this.user_header = user_header;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

}
