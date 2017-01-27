package intellichef.intellichef;
import android.util.Log;

import org.json.*;
import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;

/**
 * Created by zachjustice and jnanda3 on 1/25/17.
 */

public class IntelliServerAPI {
    private static IntelliServerRestClient api;

    public static void initialize() {
        api = new IntelliServerRestClient();
    }

    public static void login( String email, String password, final JsonHttpResponseHandler callback ) throws JSONException {
        final JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
            public void onFailure(int statusCode, Header[] headers, JSONObject response) {
                Log.v("JSONObject", response.toString() );
            }

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Pull out the first event on the public timeline
                // Do something with the response
                Log.v("JSONObject", response.toString() );
                callback.onSuccess(statusCode, headers, response);
            }
        };

        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("password", password);

        IntelliServerRestClient.post("login", params, responseHandler );
    }
}
