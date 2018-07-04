package com.chehubang.duolejie.modules.chargecenter.presenter

import android.text.TextUtils
import com.chehubang.duolejie.base.MvpPresenter
import com.chehubang.duolejie.config.Constant
import com.chehubang.duolejie.model.CarLifePicBean
import com.chehubang.duolejie.modules.chargecenter.activity.ChargeCenterActivity
import com.chehubang.duolejie.utils.RsaTool
import com.chehubang.duolejie.utils.log
import common.Log.LogUtils
import common.Utils.JSONUtils
import common.http.LoadDataSubscriber
import common.http.RequestResult
import okhttp3.ResponseBody
import org.json.JSONObject
import rx.Observable
import java.util.ArrayList
import java.util.HashMap

/**
 * Created by Thinkpad on 2017/12/14.
 */

class ChargeCenterPresenter(activity: ChargeCenterActivity) : MvpPresenter<ChargeCenterActivity>() {

    init {
        attachView(activity)
    }

    fun getChargeData(action: Int, id: String) {
        val map = HashMap<String, String>()
        val time = System.currentTimeMillis().toString() + ""
        try {
            val s = "$id|$|$time"
            val sign = RsaTool.encrypt(Constant.OUR_RSA_PUBLIC, s)
            map.put("sign", sign)
        } catch (e: Exception) {
            map.put("sign", "")
            e.printStackTrace()
        }
        map.put("request_time", time)
        map.put("user_id", id)
        val params = JSONUtils.mapToJson(map)
        map.clear()
        map.put("param", params)
        val typelist = service.getChargeData(map)
        loadData(action, typelist)

    }

    fun getOrderNumber(action: Int, id: String, fee: String, type: String, tel: String) {
        val map = HashMap<String, String>()
        val time = System.currentTimeMillis().toString() + ""
        try {
            val s = "$id|$|$fee|$|$type|$|$tel|$|$time"
            val sign = RsaTool.encrypt(Constant.OUR_RSA_PUBLIC, s)
            map.put("sign", sign)
        } catch (e: Exception) {
            map.put("sign", "")
            e.printStackTrace()
        }
        map.put("request_time", time)
        map.put("user_id", id)
        map.put("total_fee", fee)
        map.put("type", type)
        map.put("tel", tel)
        val params = JSONUtils.mapToJson(map)
        map.clear()
        map.put("param", params)
        val typelist = service.getOrderNumber(map)
        loadData(action, typelist)
    }

    fun loadData(action: Int, observable: Observable<ResponseBody>) {

        addSubscription(observable, object : LoadDataSubscriber<RequestResult>() {
            override fun _error(message: String?) {
                mvpView.getDataFail(message, action)
            }

            override fun _success(t: RequestResult?) {
                val result = t as RequestResult
                log.d(result.toString())
                val data = result.data
                val status = result.status
                if (TextUtils.equals(Constant.request_success, status)) {
                    if (action == ACTION_DEFAULT + 1) {
                        mvpView.getDataSuccess(data, action)
                    } else if (action == ACTION_DEFAULT + 2) {
                        mvpView.getDataSuccess(data, action)
                    }
                }
            }
        })
    }
}
