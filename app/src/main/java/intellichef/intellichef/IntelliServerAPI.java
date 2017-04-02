package intellichef.intellichef;
import android.content.Context;
import android.util.Log;

import org.joda.time.DateTime;
import org.json.*;
import com.loopj.android.http.*;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.annotation.ThreadSafe;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;

/**
 * Created by zachjustice and jnanda3 on 1/25/17.
 */

class IntelliServerAPI {


    static void login(String email, String password, Context context, final JsonHttpResponseHandler callback) throws JSONException {
        final JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
            public void onFailure(int statusCode, Header[] headers, String errorMsg, Throwable throwable) {
                Log.v("JSONObject", errorMsg);
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

    static void logout(String email, Context context, final JsonHttpResponseHandler callback) throws JSONException {
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

    static void removeAccount(String email, Context context, final JsonHttpResponseHandler callback) throws JSONException {
        final JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
            public void onFailure(int statusCode, Header[] headers, String errorMsg, Throwable throwable) {
                Log.v("JSONObject", errorMsg);
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

    static void register(RegistrationInfo registrationInfo, Context context, final JsonHttpResponseHandler callback) throws JSONException {
        final JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
            public void onFailure(int statusCode, Header[] headers, String errorMsg, Throwable throwable) {
                Log.v("JSONObject", errorMsg);
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
        IntelliServerRestClientv2.initialize(registrationInfo.getEmail(), registrationInfo.getPassword());
        IntelliServerRestClientv2.post(context, "v2.0/entities", requestData, "application/json", responseHandler);
    }

    static void getMealPlan(int entityPk, String date, final JsonHttpResponseHandler callback) throws JSONException {
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

        IntelliServerRestClientv2.get("v2.0/entities/" + entityPk + "/meal_plans", params, responseHandler);
    }

    static void generateMealPlan(Context context, int entityPk, final JsonHttpResponseHandler callback) throws JSONException {
        final JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
            public void onFailure(int statusCode, Header[] headers, JSONObject response) {
                Log.v("LMAO", "" + statusCode );
            }

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.v("LMAO", "" + statusCode);
                callback.onSuccess(statusCode, headers, response);
            }
        };


        IntelliServerRestClientv2.post(context, "v2.0/entities/" + entityPk + "/meal_plans", null, "application/json", responseHandler);

    }

    static void insertUserCalibrationPick(Context context, int entityPk, int recipePk, final JsonHttpResponseHandler callback) throws JSONException {
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
        params.put("is_calibration_recipe", true);
        StringEntity requestData = null;
        try {
            requestData = new StringEntity(params.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        IntelliServerRestClientv2.post(context, "v2.0/entities/" + entityPk + "/recipes/" + recipePk, requestData, "application/json", responseHandler);

    }

    public static void insertUserRating(Context context, int entityPk, int recipePk, int rating, final JsonHttpResponseHandler callback) throws JSONException {
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
        params.put("rating", rating);
        StringEntity requestData = null;
        try {
            requestData = new StringEntity(params.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        IntelliServerRestClientv2.post(context, "v2.0/entities/" + entityPk + "/recipes/" + recipePk, requestData, "application/json", responseHandler);

    }

    public static void updateUserRating(Context context, int entityPk, int recipePk, int rating, final JsonHttpResponseHandler callback) throws JSONException {
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
        params.put("rating", rating);
        StringEntity requestData = null;
        try {
            requestData = new StringEntity(params.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        IntelliServerRestClientv2.put(context, "v2.0/entities/" + entityPk + "/recipes/" + recipePk, requestData, "application/json", responseHandler);

    }

    public static void getUserRating(int entityPk, int recipePk, final JsonHttpResponseHandler callback) throws JSONException {
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


        RequestParams params = new RequestParams();


        IntelliServerRestClientv2.get("v2.0/entities/" + entityPk + "/recipes/" + recipePk, params, responseHandler);

    }



    public static void getUserInfo(int entity_pk, final JsonHttpResponseHandler callback) throws JSONException {
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

        IntelliServerRestClientv2.get("v2.0/entities/" + entity_pk, null, responseHandler);

    }

    static void updateUserInfo(int entity_pk, JSONObject userInfo, Context context, final JsonHttpResponseHandler callback) throws JSONException {

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

        IntelliServerRestClientv2.put(context, "v2.0/entities/" + entity_pk, requestData, "application/json", responseHandler);
    }

    static void getCalibratedMeals(final JsonHttpResponseHandler callback) throws JSONException {
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
        params.put("is_calibration_recipe", true);

        IntelliServerRestClientv2.get("v2.0/recipes", params, responseHandler);
    }

    static void getRecipe(int recipePk, final JsonHttpResponseHandler callback) throws JSONException {
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

        RequestParams params = new RequestParams();

        IntelliServerRestClientv2.get("v2.0/recipes/" + recipePk, params, responseHandler);
    }

    static void searchRecipes(String name, int page, int page_size, final JsonHttpResponseHandler callback) throws JSONException {
        final JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                Log.v("JSONObject", response.toString());
            }

            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Pull out the first event on the public timeline
                // Do something with the response
                Log.v("JSONObject", response.toString());
                callback.onSuccess(statusCode, headers, response);
            }
        };

        RequestParams params = new RequestParams();
        params.put("name", name);
        params.put("page", page);
        params.put("page_size", page_size);

        IntelliServerRestClientv2.get("v2.0/recipes", params, responseHandler);
    }

    static void updateMealPlan(int mealPlanPK, int recipePK, Context context, final JsonHttpResponseHandler callback) {
        final JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                Log.v("JSONObject", response.toString());
            }

            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                Log.v("JSONObject", response);
            }

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Pull out the first event on the public timeline
                // Do something with the response
                Log.v("JSONObject", response.toString());
                callback.onSuccess(statusCode, headers, response);
            }
        };

        StringEntity requestData = null;

        try {
            requestData = new StringEntity("{\"recipe_pk\":" + recipePK +"}");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Log.v("replace", "" + mealPlanPK);
        IntelliServerRestClientv2.put(context, "v2.0/meal_plans/" + mealPlanPK, requestData, "application/json", responseHandler);
    }

    static void getMealPlanHistory(DateTime startDate, DateTime endDate, int entityPk, final JsonHttpResponseHandler callback) {
        final JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                Log.v("JSONObject", response.toString());
            }

            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                Log.v("JSONObject", response);
            }

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Pull out the first event on the public timeline
                // Do something with the response
                Log.v("JSONObject", response.toString());
                callback.onSuccess(statusCode, headers, response);
            }
        };

        RequestParams params = new RequestParams();
        params.put("start_date", startDate);
        params.put("end_date", endDate);

        IntelliServerRestClientv2.get("v2.0/entities/" + entityPk + "/meal_plans", params, responseHandler);
    }
}
