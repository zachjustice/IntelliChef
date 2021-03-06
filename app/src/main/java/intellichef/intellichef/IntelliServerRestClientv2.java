package intellichef.intellichef;
import android.content.Context;
import android.util.Log;

import com.loopj.android.http.*;

import cz.msebera.android.httpclient.HttpEntity;

/**
 * Created by zachjustice on 1/25/17.
 */

public class IntelliServerRestClientv2 {
    private static final String BASE_URL = "http://intellichef.pro/api/";

    private static AsyncHttpClient client;

    public static void initialize(String username, String password) {
        client = new AsyncHttpClient();
        client.setTimeout(10000);
        client.setBasicAuth(username, password);
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
        Log.wtf("PINEAPPLE", getAbsoluteUrl(url));
    }

    public static void post(Context context, String url, HttpEntity entity, String contentType, ResponseHandlerInterface responseHandler) {
        client.post(context, getAbsoluteUrl(url), entity, contentType, responseHandler);
    }

    public static void put(Context context, String url, HttpEntity entity, String contentType, ResponseHandlerInterface responseHandler) {
        client.put(context, getAbsoluteUrl(url), entity, contentType, responseHandler);
    }


    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}

