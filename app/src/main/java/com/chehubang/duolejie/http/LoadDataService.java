package com.chehubang.duolejie.http;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by jxl on 2017/6/28.
 */

public interface LoadDataService {

    //注册
    @FormUrlEncoded
    @POST("appInterface/addUser.jhtml")
    Observable<ResponseBody> register(@FieldMap Map<String, String> data);

    //修改密码
    @FormUrlEncoded
    @POST("appInterface/updateUserPassword.jhtml")
    Observable<ResponseBody> updataPassword(@FieldMap Map<String, String> data);

    //获取验证码
    @FormUrlEncoded
    @POST("appInterface/getCode.jhtml")
    Observable<ResponseBody> getVerificationCode(@FieldMap Map<String, String> data);

    //开始登录
    @FormUrlEncoded
    @POST("appInterface/userLogin.jhtml")
    Observable<ResponseBody> login(@FieldMap Map<String, String> data);

    //获取首页轮播图数据
    @FormUrlEncoded
    @POST("appInterface/getIndexData.jhtml")
    Observable<ResponseBody> getViewPagerData(@FieldMap Map<String, String> data);

    //获取商品数据
    @FormUrlEncoded
    @POST("appInterface/getIndexGoodsList.jhtml")
    Observable<ResponseBody> getGoodsList(@FieldMap Map<String, String> data);

    //获取购物车列表
    @FormUrlEncoded
    @POST("appInterface/getShopCartList.jhtml")
    Observable<ResponseBody> getShoppinglist(@FieldMap Map<String, String> data);

    //获取所有商品类别接口
    @FormUrlEncoded
    @POST("appInterface/getGoodsTypeData.jhtml")
    Observable<ResponseBody> getTypelist(@FieldMap Map<String, String> data);

    //获取二级商品类别接口
    @FormUrlEncoded
    @POST("appInterface/goodsTypeChilderList.jhtml")
    Observable<ResponseBody> getTypeChildlist(@FieldMap Map<String, String> data);

    //获取分类三级接口
    @FormUrlEncoded
    @POST("appInterface/getGoodsTypeList.jhtml")
    Observable<ResponseBody> getGoodsInfoData(@FieldMap Map<String, String> data);

    //获取商品详情接口
    @FormUrlEncoded
    @POST("appInterface/getGoodsInfoData.jhtml")
    Observable<ResponseBody> getGoodsDetailsData(@FieldMap Map<String, String> data);

    //获取轮播图接口
    @FormUrlEncoded
    @POST("appInterface/getBanner.jhtml")
    Observable<ResponseBody> getCarLifeBanner(@FieldMap Map<String, String> data);

    //获取加油卡信息
    @FormUrlEncoded
    @POST("appInterface/getOilCardList.jhtml")
    Observable<ResponseBody> getOilCardRecord(@FieldMap Map<String, String> data);

    //购买加油卡接口
    @FormUrlEncoded
    @POST("appInterface/addOilCardOrder.jhtml")
    Observable<ResponseBody> addOilCardOrder(@FieldMap Map<String, String> data);

    //添加加油卡
    @FormUrlEncoded
    @POST("appInterface/setBuildOilCard.jhtml")
    Observable<ResponseBody> addOilCard(@FieldMap Map<String, String> data);

    //获取搜索商品或店铺接口
    @FormUrlEncoded
    @POST("appInterface/getGoodsSearchList.jhtml")
    Observable<ResponseBody> getGoodsSearchList(@FieldMap Map<String, String> data);

    //获取热门搜索词接口
    @FormUrlEncoded
    @POST("appInterface/getHotSearchData.jhtml")
    Observable<ResponseBody> getHotSearchData(@FieldMap Map<String, String> data);

    //获取商品规格接口
    @FormUrlEncoded
    @POST("appInterface/getGoodsInfoSpecList.jhtml")
    Observable<ResponseBody> getGoodsInfoSpecList(@FieldMap Map<String, String> data);

    //获取商品颜色接口
    @FormUrlEncoded
    @POST("appInterface/getGoodsInfoColorList.jhtml")
    Observable<ResponseBody> getGoodsInfoColorList(@FieldMap Map<String, String> data);


    //抽奖页面数据接口
    @FormUrlEncoded
    @POST("appInterface/getDrawIndexData.jhtml")
    Observable<ResponseBody> getLuckDrawData(@FieldMap Map<String, String> data);


    //提交车险数据接口
    @FormUrlEncoded
    @POST("appInterface/addCarinsurance.jhtml")
    Observable<ResponseBody> addCarinsurance(@FieldMap Map<String, String> data);


    //话费流量充值接口
    @FormUrlEncoded
    @POST("appInterface/getRechargeBill.jhtml")
    Observable<ResponseBody> getChargeData(@FieldMap Map<String, String> data);

    //获取用户余额
    @FormUrlEncoded
    @POST("appInterface/payInfo.jhtml")
    Observable<ResponseBody> getBalance(@FieldMap Map<String, String> data);


    //获取订单号
    @FormUrlEncoded
    @POST("appInterface/addRechargeOrder.jhtml")
    Observable<ResponseBody> getOrderNumber(@FieldMap Map<String, String> data);


    //充值话费流量
    @FormUrlEncoded
    @POST("appInterface/payOrder.jhtml")
    Observable<ResponseBody> payRechargeOrder(@FieldMap Map<String, String> data);


    //获取商户信息接口
    @FormUrlEncoded
    @POST("appInterface/getGoodsBrandList.jhtml")
    Observable<ResponseBody> getGoodsBrandInfo(@FieldMap Map<String, String> data);

    //获取个人信息接口
    @FormUrlEncoded
    @POST("appInterface/getUserData.jhtml")
    Observable<ResponseBody> getUserData(@FieldMap Map<String, String> data);

    //获取分期订单列表
    @FormUrlEncoded
    @POST("appInterface/getRechargeList.jhtml")
    Observable<ResponseBody> getRechargeList(@FieldMap Map<String, String> data);

    //获取分期订单详情
    @FormUrlEncoded
    @POST("appInterface/getRechargeDetailList.jhtml")
    Observable<ResponseBody> getRechargeDetailList(@FieldMap Map<String, String> data);

    //查询订单接口
    @FormUrlEncoded
    @POST("appInterface/getOrderData.jhtml")
    Observable<ResponseBody> getOrderData(@FieldMap Map<String, String> data);

    //支付宝签名串
    @FormUrlEncoded
    @POST("appInterface/zfbAppClient.jhtml")
    Observable<ResponseBody> getappClicet(@FieldMap Map<String, String> data);

    //获取通知消息
    @FormUrlEncoded
    @POST("appInterface/getMessageInfoList.jhtml")
    Observable<ResponseBody> getNoticeList(@FieldMap Map<String, String> data);

    //更新用户信息
    @FormUrlEncoded
    @POST("appInterface/updateUser.jhtml")
    Observable<ResponseBody> updataUserInfo(@FieldMap Map<String, String> data);

   /* //抽奖页面确认接口
    @FormUrlEncoded
    @POST("appInterface/setConfirm.jhtml")
    Observable<ResponseBody> getConfirm(@FieldMap Map<String, String> data);*/

    //抽奖页面确认接口
    @FormUrlEncoded
    @POST("appInterface/setCouponSure.jhtml")
    Observable<ResponseBody> getConfirm(@FieldMap Map<String, String> data);

    //获取奖券详情页面
    @FormUrlEncoded
    @POST("appInterface/getGoodsBrandInfoData.jhtml")
    Observable<ResponseBody> getLuckDrawDetailsData(@FieldMap Map<String, String> data);

    //第三方登录
    @FormUrlEncoded
    @POST("appInterface/otherAddUser.jhtml")
    Observable<ResponseBody> loginByThirdPart(@FieldMap Map<String, String> data);

    //添加到购物车
    @FormUrlEncoded
    @POST("appInterface/addShopCart.jhtml")
    Observable<ResponseBody> addToShoppingList(@FieldMap Map<String, String> data);

    //获取支付详情页
    @FormUrlEncoded
    @POST("appInterface/getOrderIndexData.jhtml")
    Observable<ResponseBody> getChargeDetails(@FieldMap Map<String, String> data);

    //获取地址列表页面
    @FormUrlEncoded
    @POST("appInterface/getUserAddressListData.jhtml")
    Observable<ResponseBody> getUserAddressList(@FieldMap Map<String, String> data);

    //删除地址
    @FormUrlEncoded
    @POST("appInterface/delAddress.jhtml")
    Observable<ResponseBody> deleteAddressItem(@FieldMap Map<String, String> data);

    //保存地址接口
    @FormUrlEncoded
    @POST("appInterface/saveAddressAndroid.jhtml")
    Observable<ResponseBody> saveLocationData(@FieldMap Map<String, String> data);

    //获取用户地址接口
    @FormUrlEncoded
    @POST("appInterface/getUserAddressAndroidListData.jhtml")
    Observable<ResponseBody> getUserAddressListData(@FieldMap Map<String, String> data);

    //更改默认地址接口
    @FormUrlEncoded
    @POST("appInterface/changeDefaultAddress.jhtml")
    Observable<ResponseBody> setDefaultAddress(@FieldMap Map<String, String> data);

    //获取商品订单
    @FormUrlEncoded
    @POST("appInterface/genOrder.jhtml")
    Observable<ResponseBody> getGoodsOrderNo(@FieldMap Map<String, String> data);

    //获取商品规格颜色
    @FormUrlEncoded
    @POST("appInterface/getGoodsInfoMoreList.jhtml")
    Observable<ResponseBody> getGoodsInfoMoreList(@FieldMap Map<String, String> data);

    //根据商品颜色获取商品数量
    @FormUrlEncoded
    @POST("appInterface/getGoodsInfoSpecByColorList.jhtml")
    Observable<ResponseBody> getGoodsInfoSpecByColorList(@FieldMap Map<String, String> data);

    //确定抽奖
    @FormUrlEncoded
    @POST("appInterface/setCouponSure.jhtml")
    Observable<ResponseBody> confirmLottery(@FieldMap Map<String, String> data);

    //删除购物车
    @FormUrlEncoded
    @POST("appInterface/delShopCart.jhtml")
    Observable<ResponseBody> deleteItem(@FieldMap Map<String, String> data);

    //上传头像
    @FormUrlEncoded
    @POST("appInterface/uploadImgNew.jhtml")
    Observable<ResponseBody> loadpic(@FieldMap Map<String, Object> data);

    //确定抽奖
    @FormUrlEncoded
    @POST("appInterface/getOrderReceive.jhtml")
    Observable<ResponseBody> confirmGoods(@FieldMap Map<String, String> data);

    //生成充值订单
    @FormUrlEncoded
    @POST("appInterface/recharge.jhtml")
    Observable<ResponseBody> recharge(@FieldMap Map<String, String> data);

    /**
     * 商家信息
     *
     * @param data
     * @return
     */
    @FormUrlEncoded
    @POST("appInterface/getGoodsBrandByQRCodeList.jhtml")
    Observable<ResponseBody> getBrandMsg(@FieldMap Map<String, String> data);

    /**
     * 扫码付款
     *
     * @param data
     * @return payMoneyGoodsBrandByQRCode.jhtml
     */
    @FormUrlEncoded
    @POST("appInterface/payMoneyGoodsBrandByQRCode.jhtml")
    Observable<ResponseBody> payMoneyGoodsBrandByQRCode(@FieldMap Map<String, String> data);

    /**
     * 物流公司
     *
     * @param data
     * @return getLogistics.jhtml
     */
    @FormUrlEncoded
    @POST("appInterface/getLogistics.jhtml")
    Observable<ResponseBody> getLogisticList(@FieldMap Map<String, String> data);

    /**
     * 退货退款
     * RequestBody
     *
     * @param map 参数
     * @return getLogistics.jhtml
     */
    @FormUrlEncoded
    @POST("appInterface/getOrderReturn.jhtml")
    Observable<ResponseBody> getOrderReturn(@FieldMap Map<String, String> map);

    /**
     * 获取可退货退款金额
     *
     * @param data
     * @return getReturnList.jhtml
     */
    @FormUrlEncoded
    @POST("appInterface/getReturnList.jhtml")
    Observable<ResponseBody> getReturnList(@FieldMap Map<String, String> data);

    /**
     * 抽奖记录
     *
     * @param data
     * @return drawResultList.jhtml
     */
    @FormUrlEncoded
    @POST("appInterface/getDrawResultList.jhtml")
    Observable<ResponseBody> getDrawResultList(@FieldMap Map<String, String> data);

    /**
     * 商户入驻
     * RequestBody
     *
     * @param data
     * @return getLogistics.jhtml
     */
    @FormUrlEncoded
    @POST("appInterface/addBrandList.jhtml")
    Observable<ResponseBody> addBrand(@FieldMap Map<String, String> data);

    /**
     * 快递信息
     * RequestBody
     *
     * @param data
     * @return getLogistics.jhtml
     */
    @FormUrlEncoded
    @POST("appInterface/getCourier.jhtml")
    Observable<ResponseBody> getCourier(@FieldMap Map<String, String> data);

    /**
     * 我的订单
     * RequestBody
     *
     * @param data
     * @return getLogistics.jhtml
     */
    @FormUrlEncoded
    @POST("appInterface/getCarInsuranceList.jhtml")
    Observable<ResponseBody> getCarOrder(@FieldMap Map<String, String> data);

    /**
     * 我的订单
     * RequestBody
     *
     * @param data
     * @return getLogistics.jhtml
     */
    @FormUrlEncoded
    @POST("appInterface/getQrcodeImg.jhtml")
    Observable<ResponseBody> getQrCodeImg(@FieldMap Map<String, String> data);

    /**
     * 我的订单
     * RequestBody
     *
     * @param data
     * @return getLogistics.jhtml
     */
    @FormUrlEncoded
    @POST("appInterface/insertSuggest.jhtml")
    Observable<ResponseBody> submitSuggest(@FieldMap Map<String, String> data);

    /**
     * RequestBody
     *
     * @param data
     * @return getLogistics.jhtml
     */
    @FormUrlEncoded
    @POST("appInterface/getDrawResultUserList.jhtml")
    Observable<ResponseBody> getDrawResultUserList(@FieldMap Map<String, String> data);
    /**
     * RequestBody
     *
     * @return getLogistics.jhtml
     */
    @FormUrlEncoded
    @POST("appInterface/getBrandTypeList.jhtml")
    Observable<ResponseBody> getBrandType(@FieldMap Map<String, String> data);
    /**
     * RequestBody
     *
     * @return getLogistics.jhtml
     */
    @FormUrlEncoded
    @POST("appInterface/getRechargeDetailStagesList.jhtml")
    Observable<ResponseBody> getRechargeDetailStagesList(@FieldMap Map<String, String> data);
    /**
     * RequestBody
     *
     * @return getLogistics.jhtml
     */
    @FormUrlEncoded
    @POST("appInterface/writeInviteCode.jhtml")
    Observable<ResponseBody> writeInviteCode(@FieldMap Map<String, String> data);

}
