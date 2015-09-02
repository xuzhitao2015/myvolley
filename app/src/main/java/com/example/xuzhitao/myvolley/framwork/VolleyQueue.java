package com.example.xuzhitao.myvolley.framwork;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by xuzhitao on 2015/9/2.
 */
public class VolleyQueue {
    private static VolleyQueue mInstance;
    private RequestQueue mQueue;

    private VolleyQueue() {
        // TODO
        // 获取ApplicationContext
        Context context = null;
        mQueue = Volley .newRequestQueue(context);
    }

    public static VolleyQueue getInstance() {
        if (mInstance == null) {
            mInstance = new VolleyQueue();
        }
        return mInstance;
    }

    public RequestQueue getmQueue() {
        // TODO
        // 获取ApplicationContext
        Context context = null;
        if(mQueue==null){
            mQueue = Volley.newRequestQueue(context);
        }
        return mQueue;
    }
}
