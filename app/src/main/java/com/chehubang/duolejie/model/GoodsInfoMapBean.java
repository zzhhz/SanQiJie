package com.chehubang.duolejie.model;

/**
 * Created by Thinkpad on 2017/12/18.
 */

public class GoodsInfoMapBean {


    /*"brand_id": 4,
            "brand_name": "李宁2",
            "cart_id": "",
            "collect_id": "",
            "commentNum": "0",
            "content": "123",
            "create_time": 1482398515000,
            "good_header": "http://192.168.3.25:8070/GetTreasureAppJinDong/upload/1514367255733.png",
            "good_imgs": "http://192.168.3.25:8070/GetTreasureAppJinDong/upload/1514367255734.png,http://192.168.3.25:8070/GetTreasureAppJinDong/upload/1514367255735.png,http://192.168.3.25:8070/GetTreasureAppJinDong/upload/1514367255737.png",
            "good_name": "现货包邮车厘子新鲜水果进口大樱桃jj级大果顺丰包邮",
            "good_price": 50,
            "good_single_price": 60,
            "goods_detail": "<p><img src="http://39.107.14.118/upload/goodsdetail/522860622624.jpg" width="100%" /></p>",
            "goods_type_id": "10018237",
            "id": 522860622624,
            "is_brand": "y",
            "is_cart": "n",
            "is_collect": "n",
            "lottery": "10",
            "order_id": 7,
            "percent": "0",
            "saleNum": 377,
            "status": "y"
*/
    private String commentNum;
    private String content;
    private String create_time;
    private String good_header;
    private String good_imgs;
    private String good_name;
    private String good_price;
    private String good_single_price;
    private String goods_type_id;
    private String id;
    private String is_auto_robot_win;
    private String is_ios_check_show;
    private String lottery;
    private String order_id;
    private String percent;
    private String status;
    private String saleNum;
    private String goods_detail;
    private int goods_num;

    public int getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(int goods_num) {
        this.goods_num = goods_num;
    }

    public String getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(String saleNum) {
        this.saleNum = saleNum;
    }

    public String getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getGood_header() {
        return good_header;
    }

    public void setGood_header(String good_header) {
        this.good_header = good_header;
    }

    public String getGood_imgs() {
        return good_imgs;
    }

    public void setGood_imgs(String good_imgs) {
        this.good_imgs = good_imgs;
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

    public String getGoods_type_id() {
        return goods_type_id;
    }

    public void setGoods_type_id(String goods_type_id) {
        this.goods_type_id = goods_type_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIs_auto_robot_win() {
        return is_auto_robot_win;
    }

    public void setIs_auto_robot_win(String is_auto_robot_win) {
        this.is_auto_robot_win = is_auto_robot_win;
    }

    public String getIs_ios_check_show() {
        return is_ios_check_show;
    }

    public void setIs_ios_check_show(String is_ios_check_show) {
        this.is_ios_check_show = is_ios_check_show;
    }

    public String getLottery() {
        return lottery;
    }

    public void setLottery(String lottery) {
        this.lottery = lottery;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGoods_detail() {
        return goods_detail;
    }

    public void setGoods_detail(String goods_detail) {
        this.goods_detail = goods_detail;
    }
}
