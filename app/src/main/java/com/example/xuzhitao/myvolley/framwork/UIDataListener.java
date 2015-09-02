package com.example.xuzhitao.myvolley.framwork;

import com.android.volley.VolleyError;

/**
 * Created by lee on 15/7/13.
 */
public interface UIDataListener<T> {

    void onResponse(T response, String rspCode, String rspMsg);

    void onFail(T response, String rspCode, String rspMsg);

    void onError(VolleyError error);
}
