package com.llc.project.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.view.ContextThemeWrapper;
import android.view.Gravity;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.llc.project.R;
import com.llc.project.model.RetrofitModel;
import com.llc.project.networking.WebUrls;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by User on 13-12-2019.
 */

public class Util {

    Context mContext;
    WebUrls mWebUrls;
    ResponseCallback mResponseCallback;

    public Util(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * Retrofit initialization
     * @return
     */
    public WebUrls InitRetroFit() {
        if (mWebUrls != null)
            return mWebUrls;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Logging(message);
            }
        });
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        Interceptor mRetry = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = null;
                okhttp3.Response response = null;
                try {
                    request = chain.request();
                    response = null;
                    if (request != null) {
                        response = chain.proceed(request);
                        int tryCount = 0;
                        while (response == null || (!response.isSuccessful() && tryCount < 2)) {
                            Logging("Request is not successful - " + tryCount);
                            tryCount++;
                            response = chain.proceed(request);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (response != null) {
                        return response;
                    } else {
                        return chain.proceed(request);
                    }
                }
            }
        };

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(50000, TimeUnit.MILLISECONDS);
        builder.addInterceptor(logging);
        builder.addInterceptor(mRetry);

        OkHttpClient httpClient = builder.build();

        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(RetrofitModel.class, new ResponseParser());

        Gson gson = gsonBuilder.disableHtmlEscaping().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .client(httpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        mWebUrls = retrofit.create(WebUrls.class);
        return mWebUrls;
    }

    /**
     * Response value get from serialization method
     */
    public class ResponseParser implements JsonDeserializer<RetrofitModel> {

        @Override
        public RetrofitModel deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) {
            RetrofitModel mRetrofitModel = new RetrofitModel();
            try {
                mRetrofitModel.setResponse(json.toString());
            } catch (Exception e) {
                mRetrofitModel.setResponse("{}");
                e.printStackTrace();
            }
            return mRetrofitModel;
        }
    }

    /**
     * Call back method for api
     */
    public interface ResponseCallback {
        public void onResponseSuccess(RetrofitModel response);

        public void onResponseFailure(String reason);

        public void onRequestFailure(Throwable t);
    }

    /**
     * printing a value
     * @param msg
     */
    public static void Logging(String msg) {
        System.out.println(msg);
    }

    /**
     * call back method for all api
     * @param context
     * @param method_name
     * @param mResponseCallback
     * @param mProgressDialog
     */
    public void ExecuteRetroFit(final Activity context, String method_name, final ResponseCallback mResponseCallback, final ProgressDialog mProgressDialog) {
        this.mResponseCallback = mResponseCallback;
        Call<RetrofitModel> call = null;

        if (method_name.equals("COUNTRY_DETAILS")) {
            call = InitRetroFit().getCountryData();
        }

        call.enqueue(new Callback<RetrofitModel>() {

            @Override
            public void onResponse(Call<RetrofitModel> call, Response<RetrofitModel> response) {
                if (response != null && response.isSuccessful()) {

                    RetrofitModel mRetrofitModel = response.body();
                    try {
                        if (response.code() == 200) {
                            mResponseCallback.onResponseSuccess(mRetrofitModel);
                        } else {

                            RetrofitModel mModel = new RetrofitModel();
                            mModel.setStatus(false);
                            String myString = new Gson().toJson(mModel);
                            mRetrofitModel = new RetrofitModel();
                            mRetrofitModel.setResponse(myString);
                            mResponseCallback.onResponseFailure(new SocketTime(context.getString(R.string.socket_time_out)).getLocalizedMessage());

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    try {

                        if (response != null) {
                            ResponseBody errorBody = response.errorBody();
                            Logging("Error " + new Gson().toJson(errorBody));
                            mResponseCallback.onResponseFailure(errorBody.toString());
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<RetrofitModel> call, Throwable t) {
                try {
                    mResponseCallback.onRequestFailure(t);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * getting a server error using socket
     */
    class SocketTime extends SocketTimeoutException {
        public SocketTime() {
        }

        public SocketTime(String message) {
            super(message);
        }
    }

    /**
     * Create a progress dialogue initialization
     * @param context
     * @return
     */
    public ProgressDialog getProgressDialog(Context context) {
        @SuppressLint("RestrictedApi") ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light_Dialog);
        ProgressDialog mProgressDialog = new ProgressDialog(contextThemeWrapper);
        mProgressDialog.setMessage("Loading...");
        return mProgressDialog;
    }

    /**
     * Displaying a toast message
     * @param context
     * @param msg
     */
    public void ShowToast(Context context, String msg) {
        try {
            Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            Logging(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * Internet connection checking
     * @param context
     * @return
     */
    public static boolean isInternetConnected(final Context context) {
        try {

            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {

                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int length = 0; length < info.length; length++) {
                        if (info[length].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }

            Handler handler = new Handler(context.getMainLooper());
            handler.post(new Runnable() {
                public void run() {
                    Toast mToast = Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT);
                    mToast.setGravity(Gravity.CENTER, 0, 0);
                    mToast.show();
                }
            });
            return false;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
