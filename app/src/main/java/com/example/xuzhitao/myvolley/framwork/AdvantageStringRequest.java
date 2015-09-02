package com.example.xuzhitao.myvolley.framwork;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by lee on 15/7/14.
 */
public class AdvantageStringRequest extends Request<String> {

    protected String rspCode;
    protected String rspMsg;
    //response 返回对象
    Cache.Entry responseEntry;
    private Map<String, String> mHeaders;
    private Map<String, String> mMap;
    private UIDataListener<String> uiDataListener;

    /**
     * @param method        请求类型
     * @param url           请求地址
     * @param listener      成功监听
     * @param errorListener 失败监听
     */
    public AdvantageStringRequest(int method, String url, UIDataListener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.uiDataListener = listener;
        this.setRetryPolicy(new DefaultRetryPolicy(15 * 1000, 2, 1.0f));
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mMap;
    }

    /**
     * 设置请求body
     *
     * @param mMap map类型
     */
    public void setParams(Map<String, String> mMap) {
        this.mMap = mMap;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        this.responseEntry = HttpHeaderParser.parseCacheHeaders(response);
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders != null ? mHeaders : super.getHeaders();
    }

    /**
     * 设置请求header
     *
     * @param mHeaders map类型
     */
    public void setHeaders(Map<String, String> mHeaders) {
        this.mHeaders = mHeaders;
    }

    //todo 需要根据业务逻辑自行获取返回状态码跟返回信息,同时根据服务器返回信息判断网络请求成功失败也在这里
    @Override
    protected void deliverResponse(String response) {
        uiDataListener.onResponse(response, rspCode, rspMsg);
    }

    @Override
    public void deliverError(VolleyError error) {
        if (uiDataListener != null) {
            uiDataListener.onError(error);
        }
    }

    /**
     * 返回服务器返回信息
     *
     * @return
     */
    public String getResMsg() {
        return rspMsg;
    }

    /**
     * 服务器返回码
     *
     * @return
     */
    public String getResCode() {
        return rspCode;
    }
}
