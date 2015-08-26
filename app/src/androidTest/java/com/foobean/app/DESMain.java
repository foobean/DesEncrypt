package com.foobean.app;

import android.test.AndroidTestCase;
import android.util.Log;

/**
 *
 * Created by foobean on 2015/8/26.
 */
public class DESMain extends AndroidTestCase implements IEncoder, IDecoder {

    public static final String TAG = DESMain.class.toString();
    public static final String BASE_KEY = "ACB39SS34MOPODDSFF10QW";//密钥

    //只是取其中部分字符串作为密钥
    protected String getRealKey(String key) {
        final String dyKey = key;
        int len = BASE_KEY.length();
        int len2 = dyKey.length();
        StringBuffer sb = new StringBuffer();
        sb.append(BASE_KEY.charAt(len - 1));
        sb.append(dyKey.charAt(len2 - 1));
        sb.append(BASE_KEY.charAt(2));
        sb.append(BASE_KEY.charAt(1));
        sb.append(dyKey.charAt(5));
        sb.append(BASE_KEY.charAt(2));
        sb.append(dyKey.charAt(len2 - 2));
        sb.append(dyKey.charAt(len2 - 3));
        String fKey = sb.toString();
        return fKey;
    }


    public void testMain(){
        IEncoder encoder = new DESMain();
        String mykey = DESEncryptUtil.encode("bean2015", System.currentTimeMillis() + "").substring(10);
        String json = "{\"userid\":\"186\",\"size\":\"10\",\"isRead\":\"0\", \"msgId\":\"185\"}";
        String data = encoder.encode(json, mykey);
        Log.d(TAG,"data:"+data);
        String sendData = data + "@" + mykey;//这个数据发给WEB服务端，@后面是密钥
        Log.d(TAG,"sendData:"+sendData);

        //服务端解密
        String str[] = sendData.split("@");
        IDecoder decoder = new DESMain();
        data = decoder.decode(str[0], str[1]);
        Log.d(TAG,"解密Data:"+data);
    }

    @Override
    public String decode(String data, String key) {
        return DESEncryptUtil.decode(getRealKey(key), data);
    }

    @Override
    public String encode(String data, String key) {
        return DESEncryptUtil.encode(getRealKey(key), data);
    }
}
