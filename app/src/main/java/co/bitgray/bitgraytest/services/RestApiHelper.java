package co.bitgray.bitgraytest.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by andrescamacho on 17/11/15.
 */
public class RestApiHelper {
    private static SharedPreferences preferences;

    public RestApiHelper() {
    }

    public static <S> S createService(Class<S> serviceClass, String baseUrl, Context context) {


        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(baseUrl)
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL);

        RestAdapter adapter = builder.build();
        return adapter.create(serviceClass);
    }
}
