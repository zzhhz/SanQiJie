package com.chehubang.duolejie.model;

/**
 * Created by fuyi on 2017/12/20.
 */

public class GiftListBean {


    /**
     * brand_header : http://192.168.3.25:8070   /GetTreasureAppJinDong/upload/353da1515488910.jpg
     * brand_id : 123456
     * coupon_info : 111
     * coupon_num : 20
     * guide_img : http://192.168.3.25:8070   /GetTreasureAppJinDong/upload/19d4f1515404544.png
     * id : 67
     * is_activity : n
     * memory_table : fight_record_1
     * need_lettory_num : 2
     * need_people : 10
     * now_people : 3
     * period : 10000
     * progress : 30
     * is_user_new
     */

    private String brand_header;
    private String brand_id;
    private String coupon_info;
    private String coupon_num;
    private String guide_img;
    private String id;
    private String is_activity;
    private String memory_table;
    private String need_lettory_num;
    private String need_people;
    private String now_people;
    private String period;
    private int progress;
    private String is_user_new;

    public String getIs_user_new() {
        return is_user_new;
    }

    public void setIs_user_new(String is_user_new) {
        this.is_user_new = is_user_new;
    }

    public String getBrand_header() {
        return brand_header;
    }

    public void setBrand_header(String brand_header) {
        this.brand_header = brand_header;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getCoupon_info() {
        return coupon_info;
    }

    public void setCoupon_info(String coupon_info) {
        this.coupon_info = coupon_info;
    }

    public String getCoupon_num() {
        return coupon_num;
    }

    public void setCoupon_num(String coupon_num) {
        this.coupon_num = coupon_num;
    }

    public String getGuide_img() {
        return guide_img;
    }

    public void setGuide_img(String guide_img) {
        this.guide_img = guide_img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIs_activity() {
        return is_activity;
    }

    public void setIs_activity(String is_activity) {
        this.is_activity = is_activity;
    }

    public String getMemory_table() {
        return memory_table;
    }

    public void setMemory_table(String memory_table) {
        this.memory_table = memory_table;
    }

    public String getNeed_lettory_num() {
        return need_lettory_num;
    }

    public void setNeed_lettory_num(String need_lettory_num) {
        this.need_lettory_num = need_lettory_num;
    }

    public String getNeed_people() {
        return need_people;
    }

    public void setNeed_people(String need_people) {
        this.need_people = need_people;
    }

    public String getNow_people() {
        return now_people;
    }

    public void setNow_people(String now_people) {
        this.now_people = now_people;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
