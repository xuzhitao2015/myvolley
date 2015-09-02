package com.example.xuzhitao.myvolley.framwork;

import android.content.Intent;
import android.net.Uri;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by lee on 2015/6/29.
 */
public class GsonRequest<T> extends Request<T> {
    private final Gson mGson = new Gson();
    //    private final Class<T> mClazz;  //返回值类型
    Type type;
    private UIDataListener<T> uiDataListener;
    private Map<String, String> mHeaders;
    private Map<String, String> mMap;
    protected String rspCode = "";
    protected String rspMsg = "";
    //response 返回对象
    Cache.Entry responseEntry;
    byte[] body;

    /**
     * @param url           链接地址
     * @param type          返回值类型
     * @param listener      成功监听
     * @param errorListener 失败监听
     */
    public GsonRequest(String url, Type type, UIDataListener<T> listener, Response.ErrorListener errorListener) {
        this(Method.GET, url, type, null, null, listener, errorListener, true);
    }

    public GsonRequest(int method, String url, Type type, UIDataListener<T> listener, Response.ErrorListener errorListener) {
        this(method, url, type, null, null, listener, errorListener, true);
    }

    public GsonRequest(int method, String url, Type type, Map<String, String> map, UIDataListener<T> listener, Response.ErrorListener errorListener, boolean isCache) {
        this(method, url, type, null, map, listener, errorListener, isCache);
    }

    @Override
    public String getBodyContentType() {
        return "application/json";
    }

    /**
     * 设置body
     *
     * @param mMap
     */
    public void setParams(Map<String, String> mMap) {
        this.mMap = mMap;
    }


    /**
     * 设置header
     *
     * @param mHeaders
     */
    public void setHeaders(Map<String, String> mHeaders) {
        this.mHeaders = mHeaders;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return body;
    }

//    @Override
//    protected Map<String, String> getParams() throws AuthFailureError {
//        return mMap;
//    }

    /**
     * @param method        请求类型
     * @param url           请求url
     * @param type          返回值类型
     * @param headers       header
     * @param map           body
     * @param listener      成功监听
     * @param errorListener 失败监听
     * @param isCache       是否缓存
     */
    public GsonRequest(int method, String url, Type type, Map<String, String> headers, Map<String, String> map, UIDataListener<T> listener, Response.ErrorListener errorListener, boolean isCache) {
        super(method, url, errorListener);
        this.type = type;
        this.mHeaders = headers;
        this.uiDataListener = listener;
        this.mMap = map;
        this.setRetryPolicy(new DefaultRetryPolicy(15 * 1000, 2, 1.0f));
        this.setShouldCache(isCache);
    }



    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders != null ? mHeaders : super.getHeaders();
    }

    @Override
    protected void deliverResponse(T response) {
        // request返回结果
        if (rspCode.equalsIgnoreCase("00000")) {
            // success
            uiDataListener.onResponse(response, rspCode, rspMsg);
        } else {
            // fail
            // TODO
            // 失败code处理
//            if (rspCode.equalsIgnoreCase("12002")||rspCode.equalsIgnoreCase("12003")||rspCode.equalsIgnoreCase("12005")) {
//                SPManager.clear();
//                Uri uri = Uri.parse("fdd-customer://login");
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//                CustomerApplication.getInstance().startActivity(intent);
//                AndroidUtils.showMsg(CustomerApplication.getInstance(), rspMsg);
//            }
            uiDataListener.onFail(response, rspCode, rspMsg);
        }

    }

    @Override
    public void deliverError(VolleyError error) {
        if (uiDataListener != null) {
            uiDataListener.onError(error);
        }
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = "";
            if(HttpHeaderParser.parseCharset(response.headers,null)!=null) {
                json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            }else {
                json = new String(response.data, HTTP.UTF_8);
            }
//            LogUtils.debug("GsonRequest","json:"+json);
            this.responseEntry = HttpHeaderParser.parseCacheHeaders(response);
            JSONObject data = new JSONObject(json);
            rspCode = data.optString("code", "");
            rspMsg = data.optString("msg", "");
            return (Response<T>) Response.success(mGson.fromJson(data.optString("data", ""), type),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (Response<T>) Response.success(mGson.fromJson("", type),
                HttpHeaderParser.parseCacheHeaders(response));
    }

    public void setBody(byte[] body) {
        this.body = body;
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