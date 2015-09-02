package com.example.xuzhitao.myvolley.framwork;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lee on 15/7/14.
 */
public class VolleyJsonHelper<T> extends VolleyBaseNetworkHelper implements Response.ErrorListener{

    private GsonRequest<T> gsonRequest;
    VolleyJsonHelper instance;

    /**
     * @param context
     * @param method 请求类型
     * @param type  返回值类型
     * @param mUrl   请求地址
     * @param uiDataListener 回调监听
     */
    public VolleyJsonHelper(Context context, int method, Type type, String mUrl, UIDataListener<T> uiDataListener) {
        super(context);
        this.url =  mUrl;
        this.setUiDataListener(uiDataListener);
        gsonRequest = new GsonRequest<T>(method,url,type,uiDataListener,this);
    }


    /**
     * 设置请求头文件
     * @param mHeaders
     */
    public void setHeader(Map<String, String> mHeaders){
        gsonRequest.setHeaders(mHeaders);
    }

    /**
     * 设置body map
     * @param mParams
     */
    public void setParams(Map<String, String> mParams){
        gsonRequest.setParams(mParams);
    }

    /**
     * 设置body byte
     * @param body
     */
    public void setBody( byte[] body){
        gsonRequest.setBody(body);
    }


    /**
     * 调用网络请求
     */
    public void request (){
        addSign();
        addToRequestQueue(gsonRequest);
    }

    /**
     * 添加安全验证头
     */
    private void addSign(){
        try {
            if(gsonRequest.getHeaders()!=null&&!gsonRequest.getHeaders().isEmpty()){
                String timeStamp = gsonRequest.getHeaders().get("Timestamp");
//                ((HashMap)gsonRequest.getHeaders()).put("Sign",
//                        CustomerApplication.getSignature(getSignedString(timeStamp)
//                                .getBytes(Charset.forName("UTF-8"))));
                // TODO
                // 添加签名验证
            }
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }

    }

    /**
     * 获取代签名字符串
     * @param timeStamp
     * @return
     */
    private String getSignedString(String timeStamp){
        String result = "";
//        if(SPManager.isLogin()){
//            result+=SPManager.getUserId();
//        }
//        if (!TextUtils.isEmpty(SPManager.getToken())){
//            result+=SPManager.getToken();
//        }
//        result+=timeStamp+getMethodString(gsonRequest.getMethod())+getSignUrl()+getSignBody();
//        System.out.println("signed string =====_:"+result);
        // TODO
        // 获取签名验证string
        return  result;
    }

    private String getSignBody(){
        String resutl = "";
        try {
            if(gsonRequest.getBody()!=null) {
                resutl = (new String(gsonRequest.getBody(), "UTF-8")).replace("\n", "").replace("\r", "");
            }
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return resutl;
    }

    private String getSignUrl(){
        Uri uri = Uri.parse(url);
        return uri.getPath();
    }

    private String getMethodString(int type){
        switch (type){
            case 0:
                return "GET";
            case 1:
                return "POST";
            case 2:
                return "PUT";
            case 3:
                return "DELETE";
        }
        return "";
    }

    /**
     * 给当前请求设置一个tag
     * @param tag
     */
    public void getResponseInfo(String tag){
        addSign();
        addToRequestQueue(gsonRequest, tag);
    }

    /**
     * 取消当前列队里所以指定tag的网络请求
     * @param tag
     */
    public void cancelRequest(String tag){
        cancelPendingRequests(tag);
    }



    @Override
    public void onErrorResponse(VolleyError error){
        if(uiDataListener!=null){
            uiDataListener.onError(error);
        }
    }



}
