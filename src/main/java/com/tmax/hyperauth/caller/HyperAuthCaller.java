package com.tmax.hyperauth.caller;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class HyperAuthCaller {
	
	private static final String CALLER_NAME = "HyperAuthCaller";
    static OkHttpClient client = new OkHttpClient();	

	private static String setHyperAuthURL( String serviceName )  {
		int hyperauthHttpPort = 8080;
		if ( StringUtil.isNotEmpty(System.getenv( "HYPERAUTH_HTTP_PORT" ))){
			hyperauthHttpPort = Integer.parseInt( System.getenv( "HYPERAUTH_HTTP_PORT" ) );
		}
		return Constants.HYPERAUTH_URL + ":" + hyperauthHttpPort +"/" + serviceName;
	}
	
	public static String loginAsAdmin() throws IOException {
		System.out.println(" [HyperAuth] Login as Admin Service" );
	    
		Request request = null;

	    //POST svc	      
	    HttpUrl.Builder urlBuilder = HttpUrl.parse(setHyperAuthURL( Constants.SERVICE_NAME_LOGIN_AS_ADMIN )).newBuilder();
	    String url = urlBuilder.build().toString();
	    RequestBody formBody = new FormBody.Builder().add("grant_type", "password")
	    		.add("username", "admin").add("password", "admin").add("client_id", "admin-cli").build(); 
	    request = new Request.Builder().header("Content-Type", "application/x-www-form-urlencoded").url(url).post(formBody).build(); 
	       
		Response response = client.newCall(request).execute();
		String result = response.body().string();
//		System.out.println(" Login As Admin result : " + result);
		
		Gson gson = new Gson();
	    JsonObject resultJson = gson.fromJson(result, JsonObject.class);
	    
//		System.out.println(" accessToken : " + resultJson.get("access_token").toString());

	    return resultJson.get("access_token").toString().replace("\"","");
	}

	
	public static JsonObject getUser(String userId, String token) throws IOException {
		System.out.println(" [HyperAuth] HyperAuth Get User Detail Service" );

	    Request request = null;

		 //GET svc
	    HttpUrl.Builder urlBuilder = HttpUrl.parse(setHyperAuthURL( Constants.SERVICE_NAME_USER_DETAIL ) + userId).newBuilder();
		System.out.println(" setHyperAuthURL(Constants.SERVICE_NAME_USER_DETAIL ) + userId" + setHyperAuthURL(Constants.SERVICE_NAME_USER_DETAIL ) + userId );

	    String url = urlBuilder.build().toString();
	    request = new Request.Builder().url(url).addHeader("Authorization", "Bearer " + token).get().build();
	    
		System.out.println(" request" + request.toString() );

		Response response = client.newCall(request).execute();
		String result = response.body().string();
		System.out.println(" UserDetailResult : " + result);
		
		Gson gson = new Gson();
	    JsonObject resultJson = gson.fromJson(result, JsonObject.class);
	    
	    return resultJson; 
	}

	public static JsonArray getUserList(String token) throws IOException {
		System.out.println(" [HyperAuth] HyperAuth Get User List Service" );

		Request request = null;

		//GET svc
		HttpUrl.Builder urlBuilder = HttpUrl.parse(setHyperAuthURL( Constants.SERVICE_NAME_USER_DETAIL )).newBuilder();

		String url = urlBuilder.build().toString();
		request = new Request.Builder().url(url).addHeader("Authorization", "Bearer " + token).get().build();

//		System.out.println(" request" + request.toString() );

		Response response = client.newCall(request).execute();
		String result = response.body().string();
//		System.out.println(" UserListResult : " + result);

		Gson gson = new Gson();
		JsonArray resultJson = gson.fromJson(result, JsonArray.class);

		return resultJson;
	}

	public static JsonObject deleteUser(String userId, String token) throws IOException {
		System.out.println(" [HyperAuth] HyperAuth User Delete Service" );

		Request request = null;

		//Delete svc
		HttpUrl.Builder urlBuilder = HttpUrl.parse(setHyperAuthURL( Constants.SERVICE_NAME_USER_DETAIL ) + userId).newBuilder();
		System.out.println(" setHyperAuthURL(Constants.SERVICE_NAME_USER_DETAIL ) + userId" + setHyperAuthURL(Constants.SERVICE_NAME_USER_DETAIL ) + userId );

		String url = urlBuilder.build().toString();
		request = new Request.Builder().url(url).addHeader("Authorization", "Bearer " + token).delete().build();

//		System.out.println(" request" + request.toString() );

		Response response = client.newCall(request).execute();
		String result = response.body().string();
//		System.out.println(" UserDeleteResult : " + result);

		Gson gson = new Gson();
		JsonObject resultJson = gson.fromJson(result, JsonObject.class);

		return resultJson;
	}
}
