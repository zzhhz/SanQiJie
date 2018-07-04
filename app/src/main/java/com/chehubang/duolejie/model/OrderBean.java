package com.chehubang.duolejie.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Thinkpad on 2017/12/26.
 */

public class OrderBean implements Parcelable {


    /**
     * brand_name : Adidas
     * buy_nums : 2
     * child_order_id : CH1514000708349
     * create_time : 2017-12-23 11:45
     * freight : 0
     * good_header : http://192.168.3.25:8080   /GetTreasureAppJinDong/yiyuanduobao/assets/images/upload/goods/1.jpg
     * good_name : 水中神智慧猫系列带盖别致有茶漏玻璃杯子透明创意可爱水杯牛奶杯耐高温保温杯
     * good_price : 28
     * good_single_price : 1
     * goods_color :
     * goods_spec :
     * order_price : 56
     * parent_order_id : c97d69bffb_1514000702_115220
     * status : 待发货
     */

    private String brand_name;
    private String buy_nums;
    private String child_order_id;
    private String create_time;
    private String freight;
    private String good_header;
    private String good_name;
    private String good_price;
    private String good_single_price;
    private String goods_color;
    private String goods_spec;
    private String order_price;
    private String parent_order_id;
    private String status;
    private String all_price;
    private String child_order_price;
    private String child_freight;

    public String getChild_order_price() {
        return child_order_price;
    }

    public void setChild_order_price(String child_order_price) {
        this.child_order_price = child_order_price;
    }

    public String getChild_freight() {
        return child_freight;
    }

    public void setChild_freight(String child_freight) {
        this.child_freight = child_freight;
    }

    public String getAll_price() {
        return all_price;
    }

    public void setAll_price(String all_price) {
        this.all_price = all_price;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getBuy_nums() {
        return buy_nums;
    }

    public void setBuy_nums(String buy_nums) {
        this.buy_nums = buy_nums;
    }

    public String getChild_order_id() {
        return child_order_id;
    }

    public void setChild_order_id(String child_order_id) {
        this.child_order_id = child_order_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public String getGood_header() {
        return good_header;
    }

    public void setGood_header(String good_header) {
        this.good_header = good_header;
    }

    public String getGood_name() {
        return good_name;
    }

    public void setGood_name(String good_name) {
        this.good_name = good_name;
    }

    public String getGood_price() {
        return good_price;
    }

    public void setGood_price(String good_price) {
        this.good_price = good_price;
    }

    public String getGood_single_price() {
        return good_single_price;
    }

    public void setGood_single_price(String good_single_price) {
        this.good_single_price = good_single_price;
    }

    public String getGoods_color() {
        return goods_color;
    }

    public void setGoods_color(String goods_color) {
        this.goods_color = goods_color;
    }

    public String getGoods_spec() {
        return goods_spec;
    }

    public void setGoods_spec(String goods_spec) {
        this.goods_spec = goods_spec;
    }

    public String getOrder_price() {
        return order_price;
    }

    public void setOrder_price(String order_price) {
        this.order_price = order_price;
    }

    public String getParent_order_id() {
        return parent_order_id;
    }

    public void setParent_order_id(String parent_order_id) {
        this.parent_order_id = parent_order_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OrderBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.brand_name);
        dest.writeString(this.buy_nums);
        dest.writeString(this.child_order_id);
        dest.writeString(this.create_time);
        dest.writeString(this.freight);
        dest.writeString(this.good_header);
        dest.writeString(this.good_name);
        dest.writeString(this.good_price);
        dest.writeString(this.good_single_price);
        dest.writeString(this.goods_color);
        dest.writeString(this.goods_spec);
        dest.writeString(this.order_price);
        dest.writeString(this.parent_order_id);
        dest.writeString(this.status);
        dest.writeString(this.all_price);
    }

    protected OrderBean(Parcel in) {
        this.brand_name = in.readString();
        this.buy_nums = in.readString();
        this.child_order_id = in.readString();
        this.create_time = in.readString();
        this.freight = in.readString();
        this.good_header = in.readString();
        this.good_name = in.readString();
        this.good_price = in.readString();
        this.good_single_price = in.readString();
        this.goods_color = in.readString();
        this.goods_spec = in.readString();
        this.order_price = in.readString();
        this.parent_order_id = in.readString();
        this.status = in.readString();
        this.all_price = in.readString();
    }

    public static final Creator<OrderBean> CREATOR = new Creator<OrderBean>() {
        @Override
        public OrderBean createFromParcel(Parcel source) {
            return new OrderBean(source);
        }

        @Override
        public OrderBean[] newArray(int size) {
            return new OrderBean[size];
        }
    };
}
