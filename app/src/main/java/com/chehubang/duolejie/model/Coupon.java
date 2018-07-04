package com.chehubang.duolejie.model;

/**
 * Created by ZZH on 2018/1/29.
 *
 * @Date: 2018/1/29
 * @Email: zzh_hz@126.com
 * @QQ: 1299234582
 * @Author: zzh
 * @Description: 优惠券
 */
public class Coupon {

    private long create_time;
    private String ticket_condition;
    private String id;
    private String ticket_value;
    private String status;
    private long valid_date;
    private String ticket_name;


    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getTicket_condition() {
        return ticket_condition;
    }

    public void setTicket_condition(String ticket_condition) {
        this.ticket_condition = ticket_condition;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTicket_value() {
        return ticket_value;
    }

    public void setTicket_value(String ticket_value) {
        this.ticket_value = ticket_value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getValid_date() {
        return valid_date;
    }

    public void setValid_date(long valid_date) {
        this.valid_date = valid_date;
    }

    public String getTicket_name() {
        return ticket_name;
    }

    public void setTicket_name(String ticket_name) {
        this.ticket_name = ticket_name;
    }
}
