package com.chehubang.duolejie.model;

/**
 * Created by Thinkpad on 2018/1/8.
 */

public class UserListBean {


    /**
     * coupon_id : 48
     * create_time : 1515065164000
     * id : 10002
     * nick_name : 2号楼
     * period : 10003
     * user_header : http://39.107.14.118   /upload/1515112174080.jpg
     * user_ip_address : 内蒙古呼和浩特
     */

    private String coupon_id;
    private String create_time;
    private String id;
    private String nick_name;
    private String period;
    private String user_header;
    private String user_ip_address;

    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getUser_header() {
        return user_header;
    }

    public void setUser_header(String user_header) {
        this.user_header = user_header;
    }

    public String getUser_ip_address() {
        return user_ip_address;
    }

    public void setUser_ip_address(String user_ip_address) {
        this.user_ip_address = user_ip_address;
    }
}
