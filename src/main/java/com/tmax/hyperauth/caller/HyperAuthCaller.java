package com.tmax.hyperauth.caller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;

@Slf4j
public class HyperAuthCaller {
    static OkHttpClient client = new OkHttpClient();

    private static String setHyperAuthURL(String serviceName) {
        int hyperauthHttpPort = 8080;
        if (StringUtil.isNotEmpty(System.getenv("HYPERAUTH_HTTP_PORT"))) {
            hyperauthHttpPort = Integer.parseInt(System.getenv("HYPERAUTH_HTTP_PORT"));
        }
        return Constants.HYPERAUTH_URL + ":" + hyperauthHttpPort + "/" + serviceName;
    }

    public static String loginAsAdmin() throws IOException {
        log.info(" [HyperAuth] Login as Admin Service");

        Request request = null;

        //POST svc
        HttpUrl.Builder urlBuilder = HttpUrl.parse(setHyperAuthURL(Constants.SERVICE_NAME_LOGIN_AS_ADMIN)).newBuilder();
        String url = urlBuilder.build().toString();

        RequestBody formBody = new FormBody.Builder().add("grant_type", "password")
                .add("username", System.getenv("KEYCLOAK_USER")).add("password", System.getenv("KEYCLOAK_PASSWORD")).add("client_id", "admin-cli").build();
        request = new Request.Builder().header("Content-Type", "application/x-www-form-urlencoded").url(url).post(formBody).build();
        log.debug(" Login As Admin request !! : " + request.toString());

        Response response = client.newCall(request).execute();
        String result = response.body().string();
        log.debug(" Login As Admin result : " + result);

        Gson gson = new Gson();
        JsonObject resultJson = gson.fromJson(result, JsonObject.class);

        log.debug(" accessToken : " + resultJson.get("access_token").toString());

        return resultJson.get("access_token").toString().replace("\"", "");
    }


    public static JsonObject getUser(String userId, String token) throws IOException {
        log.info(" [HyperAuth] HyperAuth Get User Detail Service");

        Request request = null;

        //GET svc
        HttpUrl.Builder urlBuilder = HttpUrl.parse(setHyperAuthURL(Constants.SERVICE_NAME_USER_DETAIL) + userId).newBuilder();
        log.info(" setHyperAuthURL(Constants.SERVICE_NAME_USER_DETAIL ) + userId" + setHyperAuthURL(Constants.SERVICE_NAME_USER_DETAIL) + userId);

        String url = urlBuilder.build().toString();
        request = new Request.Builder().url(url).addHeader("Authorization", "Bearer " + token).get().build();

        log.debug(" request" + request.toString());

        Response response = client.newCall(request).execute();
        String result = response.body().string();
        log.debug(" UserDetailResult : " + result);

        Gson gson = new Gson();
        JsonObject resultJson = gson.fromJson(result, JsonObject.class);

        return resultJson;
    }

    public static JsonArray getUserList(String token, int first, int max) throws IOException {
        log.info(" [HyperAuth] HyperAuth Get User List Service");

        Request request = null;

        //GET svc
        HttpUrl.Builder urlBuilder = HttpUrl.parse(setHyperAuthURL(Constants.SERVICE_NAME_USER_DETAIL)).newBuilder().addQueryParameter("first", Integer.toString(first));
        if (max != 0) urlBuilder.addQueryParameter("max", Integer.toString(max));

        String url = urlBuilder.build().toString();
        request = new Request.Builder().url(url).addHeader("Authorization", "Bearer " + token).get().build();

        log.debug(" request" + request.toString());

        Response response = client.newCall(request).execute();
        String result = response.body().string();
        log.debug(" UserListResult : " + result);

        Gson gson = new Gson();
        JsonArray resultJson = gson.fromJson(result, JsonArray.class);

        return resultJson;
    }

    public static int getUserCount(String token) throws IOException {
        log.info(" [HyperAuth] HyperAuth Get User Count Service");

        Request request = null;

        //GET svc
        HttpUrl.Builder urlBuilder = HttpUrl.parse(setHyperAuthURL(Constants.SERVICE_NAME_USER_COUNT)).newBuilder();

        String url = urlBuilder.build().toString();
        request = new Request.Builder().url(url).addHeader("Authorization", "Bearer " + token).get().build();

        log.debug(" UserCount Request" + request.toString());

        Response response = client.newCall(request).execute();
        String result = response.body().string();
        log.debug(" UserCount Result : " + result);

        return Integer.parseInt(result);
    }

    public static JsonObject deleteUser(String userId, String token) throws IOException {
        log.info(" [HyperAuth] HyperAuth User Delete Service");

        Request request = null;

        //Delete svc
        HttpUrl.Builder urlBuilder = HttpUrl.parse(setHyperAuthURL(Constants.SERVICE_NAME_USER_DETAIL) + userId).newBuilder();
        log.debug(" setHyperAuthURL(Constants.SERVICE_NAME_USER_DETAIL ) + userId" + setHyperAuthURL(Constants.SERVICE_NAME_USER_DETAIL) + userId);

        String url = urlBuilder.build().toString();
        request = new Request.Builder().url(url).addHeader("Authorization", "Bearer " + token).delete().build();

        log.debug(" request" + request.toString());

        Response response = client.newCall(request).execute();
        String result = response.body().string();
        log.debug(" UserDeleteResult : " + result);

        Gson gson = new Gson();
        JsonObject resultJson = gson.fromJson(result, JsonObject.class);

        return resultJson;
    }

    public static JsonObject getRealmInfo(String realmName, String token) throws IOException {
        log.info(" [HyperAuth] Realm Info Get Service");

        Request request = null;

        //GET svc
        HttpUrl.Builder urlBuilder = HttpUrl.parse(setHyperAuthURL(Constants.SERVICE_NAME_REALM_DETAIL) + realmName).newBuilder();

        String url = urlBuilder.build().toString();
        request = new Request.Builder().url(url).addHeader("Authorization", "Bearer " + token).get().build();

        log.debug(" request" + request.toString());

        Response response = client.newCall(request).execute();
        String result = response.body().string();
        log.debug(" RealmInfoResult : " + result);

        Gson gson = new Gson();
        JsonObject realmInfoJson = gson.fromJson(result, JsonObject.class);

        return realmInfoJson;
    }
}
