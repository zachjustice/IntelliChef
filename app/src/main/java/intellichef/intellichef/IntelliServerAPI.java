package intellichef.intellichef;
import android.content.Context;
import android.util.Log;

import org.json.*;
import com.loopj.android.http.*;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;

/**
 * Created by zachjustice and jnanda3 on 1/25/17.
 */

public class IntelliServerAPI {
    private static IntelliServerRestClient api;

    public static void initialize() {
        api = new IntelliServerRestClient();
    }

    public static void login( String email, String password, Context context, final JsonHttpResponseHandler callback ) throws JSONException {
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

        JSONObject params = new JSONObject();
        params.put("email", email);
        params.put("password", password);
        StringEntity requestData = null;

        try {
            requestData = new StringEntity(params.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        IntelliServerRestClient.post(context, "v1.0/login", requestData, "application/json", responseHandler);
    }

    public static void logout( String email, Context context, final JsonHttpResponseHandler callback ) throws JSONException {
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

        JSONObject params = new JSONObject();
        params.put("email", email);
        StringEntity requestData = null;

        try {
            requestData = new StringEntity(params.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        IntelliServerRestClient.put(context, "v1.0/logout", requestData, "application/json", responseHandler);
    }

    public static void removeAccount( String email, Context context, final JsonHttpResponseHandler callback ) throws JSONException {
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

        JSONObject params = new JSONObject();
        params.put("email", email);
        StringEntity requestData = null;

        try {
            requestData = new StringEntity(params.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        IntelliServerRestClient.post(context, "v1.0/remove_account", requestData, "application/json", responseHandler);
    }

    public static void register( RegistrationInfo registrationInfo, Context context, final JsonHttpResponseHandler callback ) throws JSONException {
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

        JSONObject params = registrationInfo.toJSONObject();
        StringEntity requestData = null;

        try {
            requestData = new StringEntity(params.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.v("JSON", "" + requestData);
        IntelliServerRestClient.post(context, "v1.0/register", requestData, "application/json", responseHandler);
    }

    public static void getRecipes(String date, final JsonHttpResponseHandler callback) throws JSONException {
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
        params.put("date", date);

        IntelliServerRestClient.get("v2.0/meal_plans", params, responseHandler);
    }

}
