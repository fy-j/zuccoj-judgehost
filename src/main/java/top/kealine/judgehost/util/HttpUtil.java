package top.kealine.judgehost.util;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import top.kealine.judgehost.config.Config;

import java.util.Map;

public class HttpUtil {
    public static Response post(String url, Map<String,Object> params) {
        return post(url, params, true);
    }

    public static Response post(String url, Map<String,Object> params, boolean withToken) {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        params.forEach( (k,v) -> {
            builder.add(k, v.toString());
        });

        if (withToken) {
            // token key
            String key = PasswordUtil.encrypt(Long.toString(System.currentTimeMillis()/1000/60/5)+ Config.JUDGEHOST_TOKEN);
            builder.add("key", key);
            builder.add("judgehost", Config.JUDGEHOST_USERNAME);
        }

        Request request = new Request.Builder().url(Config.SEVER_HOST+url).post(builder.build()).build();
        Call call = okHttpClient.newCall(request);
        try {
            return call.execute();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
