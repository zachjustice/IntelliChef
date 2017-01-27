package intellichef.intellichef;
import com.loopj.android.http.*;

/**
 * Created by zachjustice on 1/25/17.
 */

public class IntelliServerRestClient {
    private static final String BASE_URL = "https://intelliserver-156421.appspot.com/api/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
