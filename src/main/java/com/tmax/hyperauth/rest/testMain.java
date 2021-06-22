package com.tmax.hyperauth.rest;

import java.util.Base64;

public class testMain {
    public static void main(String[] args){
        String tokenString = "Basic YWRtaW46YWRtaW4=";
        System.out.println(tokenString.substring(6));
        byte[] targetBytes = tokenString.substring(6).getBytes();
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decodedBytes = decoder.decode(targetBytes);
        System.out.println(new String(decodedBytes));
        String authString = new String(decodedBytes);
        System.out.println("username : " + authString.split(":")[0]);
        System.out.println("password : " + authString.split(":")[1]);


    }

}
