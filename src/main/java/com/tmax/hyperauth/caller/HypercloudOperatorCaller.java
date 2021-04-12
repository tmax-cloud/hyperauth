package com.tmax.hyperauth.caller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Slf4j
public class HypercloudOperatorCaller {
	
    static OkHttpClient client = new OkHttpClient();

	private static String setHypercloudOperatorURL( String serviceName )  {
		int operatorHttpPort = 28677;
		if ( StringUtil.isNotEmpty(System.getenv( "HYPERCLOUD4_OPERATOR_HTTP_PORT" ))){
			operatorHttpPort = Integer.parseInt( System.getenv( "HYPERCLOUD4_OPERATOR_HTTP_PORT" ) );
		}
		return Constants.HYPERCLOUD4_OPERATOR_URL + ":" + operatorHttpPort +"/" + serviceName;
	}
	
	public static String createNewUserRole(String userId) throws Exception {
		log.info(" [Hypercloud4-operator] Call Hypercloud4 operator user Role Create Service" );
		
		JsonObject UserInDO = new JsonObject();		
		Gson gson = new Gson();
	    String json = gson.toJson(UserInDO);
	    Request request = null;

	    //POST svc	      
	    HttpUrl.Builder urlBuilder = HttpUrl.parse(setHypercloudOperatorURL( Constants.SERVICE_NAME_USER_NEW_ROLE_CREATE )).newBuilder();
	    urlBuilder.addQueryParameter("userId", userId);
	    String url = urlBuilder.build().toString();   
	    request = new Request.Builder().url(url).post(RequestBody.create(MediaType.parse("application/json"), json)).build();	    
	   
		Response response = client.newCall(request).execute();
		String result = response.body().string();
		log.debug("createNewUserRole result : " + result);
	    return result; 
	}
	
	public static String deleteNewUserRole(String userId) throws Exception {
		log.info(" [Hypercloud4-operator] Call Hypercloud4 operator user Role Delete Service" );
		
		JsonObject UserInDO = new JsonObject();		
		Gson gson = new Gson();
	    String json = gson.toJson(UserInDO);
	    Request request = null;

	    //Delete svc	      
	    HttpUrl.Builder urlBuilder = HttpUrl.parse(setHypercloudOperatorURL( Constants.SERVICE_NAME_USER_NEW_ROLE_CREATE )).newBuilder();
	    urlBuilder.addQueryParameter("userId", userId);
	    String url = urlBuilder.build().toString();
	    request = new Request.Builder().url(url).delete(RequestBody.create(MediaType.parse("application/json"), json)).build();
	   
		Response response = client.newCall(request).execute();
		String result = response.body().string();
		log.debug("deleteNewUserRole result : " + result);
	    return result; 
	}
}
