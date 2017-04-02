package intellichef.intellichef;
import android.content.Context;

import com.loopj.android.http.*;

import cz.msebera.android.httpclient.HttpEntity;

/**
 * Created by zachjustice on 1/25/17.
 */

public class IntelliServerRestClient {
    private static final String BASE_URL = "https://intelliserver-156421.appspot.com/api/";

    private static AsyncHttpClient client = new AsyncHttpClient();

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
