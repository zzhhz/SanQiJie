package com.chehubang.duolejie.model;

/**
 * Created by ZZH on 2018/3/6.
 *
 * @Date: 2018/3/6
 * @Email: zzh_hz@126.com
 * @QQ: 1299234582
 * @Author: zzh
 * @Description:
 */
public class MoreInstall {

    /**
     * order_id : 617e457589_1520336487_10457
     * last_modify_time : 1520336488000
     * goods_buy_nums : 1
     * create_time : 1520336488000
     * month : 10
     * stages_status : 未充值
     * real_name : null
     * goods_ids : 1000111100018130498
     * stages_price : 100.0
     */

    private String order_id;
    private long last_modify_time;
    private String goods_buy_nums;
    private long create_time;
    private String month;
    private String stages_status;
    private Object real_name;
    private String goods_ids;
    private String stages_price;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public long getLast_modify_time() {
        return last_modify_time;
    }

    public void setLast_modify_time(long last_modify_time) {
        this.last_modify_time = last_modify_time;
    }

    public String getGoods_buy_nums() {
        return goods_buy_nums;
    }

    public void setGoods_buy_nums(String goods_buy_nums) {
        this.goods_buy_nums = goods_buy_nums;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getStages_status() {
        return stages_status;
    }

    public void setStages_status(String stages_status) {
        this.stages_status = stages_status;
    }

    public Object getReal_name() {
        return real_name;
    }

    public void setReal_name(Object real_name) {
        this.real_name = real_name;
    }

    public String getGoods_ids() {
        return goods_ids;
    }

    public void setGoods_ids(String goods_ids) {
        this.goods_ids = goods_ids;
    }

    public String getStages_price() {
        return stages_price;
    }

    public void setStages_price(String stages_price) {
        this.stages_price = stages_price;
    }
}
