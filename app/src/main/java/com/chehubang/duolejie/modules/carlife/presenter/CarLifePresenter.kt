package com.chehubang.duolejie.modules.carlife.presenter

import android.text.TextUtils
import common.Log.LogUtils
import common.Utils.JSONUtils
import common.http.LoadDataSubscriber
import common.http.RequestResult
import okhttp3.ResponseBody
import org.json.JSONObject
import rx.Observable
import com.chehubang.duolejie.base.MvpPresenter
import com.chehubang.duolejie.config.Constant
import com.chehubang.duolejie.model.CarLifePicBean
import com.chehubang.duolejie.modules.carlife.activity.CarLifeActivity
import com.chehubang.duolejie.utils.RsaTool
import java.util.*

/**
 * Created by Thinkpad on 2017/12/14.
 */

class CarLifePresenter(activity: CarLifeActivity) : MvpPresenter<CarLifeActivity>() {

    init {
        attachView(activity)
    }

    fun getCarLifeBanner(action:Int,type:String){  //(int action, String type)
        val map = HashMap<String, String>()
        val time = System.currentTimeMillis().toString() + ""
        try {
            val s = "$type|$|$time"
            val sign = RsaTool.encrypt(Constant.OUR_RSA_PUBLIC, s)
            map.put("sign", sign)
        } catch (e: Exception) {
            map.put("sign", "")
            e.printStackTrace()
        }

        map.put("request_time", time)
        map.put("type", type)
        val params = JSONUtils.mapToJson(map)
        map.clear()
        map.put("param", params)
        val typelist = service.getCarLifeBanner(map)
        loadData(action, typelist)
    }

    fun loadData(action: Int, observable: Observable<ResponseBody>) {

        addSubscription(observable, object :LoadDataSubscriber<RequestResult>() {
            override fun _error(message: String?) {
                mvpView.getDataFail(message,action)
            }

            internal lateinit var carLifePicBeans: ArrayList<CarLifePicBean>
            override fun _success(t: RequestResult?) {
                val result = t as RequestResult
                val data = result.data
                val status = result.status

                if (TextUtils.equals(Constant.request_success, status)) {
                    if (action == MvpPresenter.ACTION_DEFAULT + 1) {
                        val jsonObject = JSONObject(data)
                        val advertisList = jsonObject.getJSONArray("advertisementList")
                        carLifePicBeans = ArrayList()
                        for (i in 0 until advertisList.length()) {
                            val o1 = advertisList.get(i) as JSONObject
                            val carLifePicBean = JSONUtils.GsonToBean(o1.toString(), CarLifePicBean::class.java)
                            carLifePicBeans.add(carLifePicBean)
                            mvpView.getDataSuccess(carLifePicBeans, action)
                        }
                    }
                }
            }
        })
    }
}
