package com.chehubang.duolejie.model;

/**
 * Created by Administrator on 2017/12/26 0026.
 */

public class InstallDetailsBean {


    private String all_price;
    private String real_name;
    private long create_time;
    private String goods_buy_nums;
    private String goods_ids;
    private String order_id;

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getAll_price() {
        return all_price;
    }

    public void setAll_price(String all_price) {
        this.all_price = all_price;
    }


    public String getGoods_buy_nums() {
        return goods_buy_nums;
    }

    public void setGoods_buy_nums(String goods_buy_nums) {
        this.goods_buy_nums = goods_buy_nums;
    }

    public String getGoods_ids() {
        return goods_ids;
    }

    public void setGoods_ids(String goods_ids) {
        this.goods_ids = goods_ids;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }
}
