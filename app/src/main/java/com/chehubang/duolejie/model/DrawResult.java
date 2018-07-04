package com.chehubang.duolejie.model;

/**
 * Created by user on 2018/1/31.
 *
 * @date: 2018/1/31
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @description:
 */
public class DrawResult {
    /**
    {
        "period": 期号,
        "win_user_id": 中奖用户id 和当前用户id比较如果一样表示该用户中奖了,
        "create_time": 创建时间,
        "id": 主键id,
        "need_lettory_num": 所需抽奖券数量,
        "coupon_num": 代金券面额,
        "brand_name": 商户名称,
        "status"：该用户是否中奖y中奖n未中奖
        "brand_header":商户门头logo
      }
     {
     "user_header":"http://39.107.14.118/upload/3a2171518518917191.jpg",
     "user_id":10016,
     "user_ip_address":"山东济南",
     "create_time":1518434227000,
     "need_lettory_num":20,
     "period":10000,
     "id":126,
     "brand_name":"乐乐",
     "coupon_info":"(120)元多乐街内代金券，不支持多乐街及线下授权店以外使用。",
     "nick_name":"乐乐",
     "status":"n",
     "coupon_num":"120元",
     "userID":10016,
     "brand_header":"http://39.107.14.118/upload/3a2171518518917191.jpg",
     "win_user_id":"",
     "is_activity":"n",
     "coupon_status":"进行中"
     }
    */
    private String period;
    private String win_user_id;
    private String create_time;
    private String id;
    private String brand_header;
    private String need_lettory_num;
    private String coupon_num;
    private String status;
    private String brand_name;
    private String coupon_status;
    private String user_header;
    private String nick_name;
    private String user_ip_address;

    public String getCoupon_status() {
        return coupon_status;
    }

    public void setCoupon_status(String coupon_status) {
        this.coupon_status = coupon_status;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getWin_user_id() {
        return win_user_id;
    }

    public void setWin_user_id(String win_user_id) {
        this.win_user_id = win_user_id;
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

    public String getBrand_header() {
        return brand_header;
    }

    public void setBrand_header(String brand_header) {
        this.brand_header = brand_header;
    }

    public String getNeed_lettory_num() {
        return need_lettory_num;
    }

    public void setNeed_lettory_num(String need_lettory_num) {
        this.need_lettory_num = need_lettory_num;
    }

    public String getCoupon_num() {
        return coupon_num;
    }

    public void setCoupon_num(String coupon_num) {
        this.coupon_num = coupon_num;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getUser_header() {
        return user_header;
    }

    public void setUser_header(String user_header) {
        this.user_header = user_header;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getUser_ip_address() {
        return user_ip_address;
    }

    public void setUser_ip_address(String user_ip_address) {
        this.user_ip_address = user_ip_address;
    }
}
