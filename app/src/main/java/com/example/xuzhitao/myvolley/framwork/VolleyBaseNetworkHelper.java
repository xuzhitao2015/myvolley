package com.example.xuzhitao.myvolley.framwork;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

/**
 * Created by lee on 15/7/13.
 */
public abstract class VolleyBaseNetworkHelper {
    /**
     * 默认tag
     */
    public static final String TAG = "VolleyNormal";
    protected UIDataListener uiDataListener;
    protected String url = "";
    private Context context;
    private RequestQueue mQueue;


    public VolleyBaseNetworkHelper(Context context) {
        this.context = context;
        mQueue = getRequestQueue();
        FakeX509TrustManager.allowAllSSL();
    }



    public Context getContext() {
        return context;
    }

    public UIDataListener getUiDataListener() {
        return uiDataListener;
    }

    public void setUiDataListener(UIDataListener uiDataListener) {
        this.uiDataListener = uiDataListener;
    }


    public RequestQueue getRequestQueue() {
        return VolleyQueue.getInstance().getmQueue();
    }

    /**
     * 将请求加入volley队列
     *
     * @param req
     */
    public void addToRequestQueue(Request req) {
        getRequestQueue().add(req);
    }


    /**
     * 将请求加入volley队列并设置tag
     *
     * @param req
     * @param tag
     */
    public void addToRequestQueue(Request req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }


    /**
     * 取消队列里当前tag的所有请求
     *
     * @param tag
     */
    public void cancelPendingRequests(String tag) {
        if (getRequestQueue() != null) {
            getRequestQueue().cancelAll(tag);
        }
    }

    /**
     * 清除队列
     */
    public void removeToRequestQueue() {
        getRequestQueue().cancelAll(this);
    }


}
