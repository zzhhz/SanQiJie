package common.http;

import java.io.Serializable;

/**
 * Created by ljx on 2017/10/25.
 */

public class RequestResult implements Serializable {

    /**
     * status : 100
     * desc : 接口文档
     * data : {"return_param":"","remark":"备注","request_param":"请求参数格式{app_login_id:用户登陆账号,type:[1.注册,2.找回密码,3.修改电话号码和微信绑定]}"}
     */

    private String status;
    private String desc;
    private String data;
    private String token;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "RequestResult{" +
                "status='" + status + '\'' +
                ", desc='" + desc + '\'' +
                ", data='" + data + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}