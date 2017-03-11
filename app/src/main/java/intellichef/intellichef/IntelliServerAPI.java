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


    public static void login(String email, String password, Context context, final JsonHttpResponseHandler callback) throws JSONException {
        final JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
            public void onFailure(int statusCode, Header[] headers, String errorMsg, Throwable throwable) {
                Log.v("JSONObject", errorMsg.toString() );
                callback.onFailure(statusCode, headers, errorMsg, throwable);
            }

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Pull out the first event on the public timeline
                // Do something with the response
                Log.v("JSONObject", response.toString() );
                callback.onSuccess(statusCode, headers, response);
            }
        };

        // add authentication to the request
        IntelliServerRestClientv2.initialize(email, password);
        IntelliServerRestClientv2.get("v2.0/entities/current", null, responseHandler);
    }

    public static void logout( String email, Context context, final JsonHttpResponseHandler callback ) throws JSONException {
        final JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
            public void onFailure(int statusCode, Header[] headers, JSONObject response) {
                Log.v("JSONObject", response.toString());
            }

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Pull out the first event on the public timeline
                // Do something with the response
                Log.v("JSONObject", response.toString());
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
            public void onFailure(int statusCode, Header[] headers, String errorMsg, Throwable throwable) {
                Log.v("JSONObject", errorMsg.toString() );
                callback.onFailure(statusCode, headers, errorMsg, throwable);
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
            public void onFailure(int statusCode, Header[] headers, String errorMsg, Throwable throwable) {
                Log.v("JSONObject", errorMsg.toString());
                callback.onFailure(statusCode, headers, errorMsg, throwable);
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                Log.v("JSONObject", response.toString());
                callback.onFailure(statusCode, headers, "", throwable);
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
        IntelliServerRestClientv2.initialize();
        IntelliServerRestClientv2.post(context, "v2.0/entities", requestData, "application/json", responseHandler);
    }

    public static void getMealPlans(String date, final JsonHttpResponseHandler callback) throws JSONException {
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

    //TODO: getUserInfo
    //(method in LoginActivity) params.put("entity_pk", *user info from static method*)
    public static void getUserInfo(int entity_pk, String email, String password, final JsonHttpResponseHandler callback) throws JSONException {
        final JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
            public void onFailure(int statusCode, Header[] headers, JSONObject response) {
                Log.v("JSONObject", response.toString());
            }

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Pull out the first event on the public timeline
                // Do something with the response
                Log.v("JSONObject", response.toString() );
                callback.onSuccess(statusCode, headers, response);
            }
        };

        IntelliServerRestClientv2.initialize(email, password);
        IntelliServerRestClientv2.get("v2.0/entities/" + entity_pk, null, responseHandler);

    }

    public static void updateUserInfo(int entity_pk, JSONObject userInfo, Context context, final JsonHttpResponseHandler callback) throws JSONException {

        final JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {

            public void onFailure(int statusCode, Header[] headers, JSONArray response) {
                Log.v("JSONObject", response.toString() );
            }

            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Pull out the first event on the public timeline
                // Do something with the response
                Log.v("JSONObject", response.toString() );
                callback.onSuccess(statusCode, headers, response);
            }
        };

        StringEntity requestData = null;

        try {
            requestData = new StringEntity(userInfo.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        IntelliServerRestClient.put(context, "v2.0/entities/" + entity_pk, requestData, "application/json", responseHandler);
    }

    public static void getCalibratedMeals(final JsonHttpResponseHandler callback ) throws JSONException {
        final JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {

            public void onFailure(int statusCode, Header[] headers, JSONArray response) {
                Log.v("JSONObject", response.toString() );
            }

            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Pull out the first event on the public timeline
                // Do something with the response
                Log.v("JSONObject", response.toString() );
                callback.onSuccess(statusCode, headers, response);
            }
        };

        RequestParams params = new RequestParams();
        params.put("sort_by", "calibration_recipes");

        IntelliServerRestClient.get("v2.0/recipes", params, responseHandler);
    }

    //TODO: Fix getRecipe
    public static void getRecipe(int recipePK, final JsonHttpResponseHandler callback) throws JSONException {
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

        IntelliServerRestClient.get("v2.0/meal_plans" + recipePK, params, responseHandler);
    }
}
