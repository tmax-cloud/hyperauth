package com.tmax.hyperauth.rest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.io.ByteArrayInputStream;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

public class testMain {
    public static void main(String[] args) throws Throwable {
        String tokenString = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJKWXVFeGl5clVvcG1XbDMyUl80Njh5QmZ3ZVhoMHE0NXhDN05CZ1Z6ZklFIn0.eyJleHAiOjE2MTg2NDQzMDQsImlhdCI6MTYxODY0MDcwNCwianRpIjoiMzEwMjI1YTQtODdjMC00ODE3LWE0MTYtMDI2ZGYxYWIyZTk5IiwiaXNzIjoiaHR0cHM6Ly8xNzIuMjIuNi4xMS9hdXRoL3JlYWxtcy9tYXN0ZXIiLCJzdWIiOiI3ZjA2MjBkYS03NjkzLTQ2NWItYTNjZC03Njc4MTNlNTdjOWYiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJhZG1pbi1jbGkiLCJzZXNzaW9uX3N0YXRlIjoiMWNhNjE1NGYtMTJiOS00ZWE5LTgwZGQtOTE0ZDI4MzE4MzM0IiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIiXSwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsInByZWZlcnJlZF91c2VybmFtZSI6InRhZWdlb25fd29vIiwiZW1haWwiOiJ0YWVnZW9uX3dvb0B0bWF4LmNvLmtyIn0.Vhg4f-ikSC46064_OMmUG7qJ6YmuDPPQPYfe9uDycX9Cj5kTI36CMphHhykAxYIF5UYPTTrc8brzJOUEsdtHd2Cr5nwV4zAIz8YkJC4tPvQy68Auh5B-cg5DrUQKQMJ9S9D0Tkzvw3dsmkA_RD2UoMRdqJerDY6YOEKszs1Xop897F2srDHRKInvPGDE6rk2SumJfOke3ftpMsUuIHeG30cShMVNsL7R_86Wo_q0LS6dG5OzOr5G5S8BDJAaICIGLPxuYZ2fZGOh_gyP223Qg3Gmzdk_MNB9QwmkJ_dfmRegZVW50amAYZE3wyvpA_IUbRhiiHNHCiKVJcPDVAoeVg";
        DecodedJWT token = verifyToken(tokenString);
        System.out.println(token.getIssuer());
        System.out.println(token.getClaim("email").asString());
    }

    public static DecodedJWT verifyToken(String token) throws Exception {
        byte[] certificateData = Base64.getDecoder().decode("MIICmzCCAYMCBgFzb8gVyzANBgkqhkiG9w0BAQsFADARMQ8wDQYDVQQDDAZtYXN0ZXIwHhcNMjAwNzIxMDUwOTEwWhcNMzAwNzIxMDUxMDUwWjARMQ8wDQYDVQQDDAZtYXN0ZXIwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCKtdhhI4ihouJFwkNmo0gWIDFyBy8Tn45HUJQkZVCJKyLIoY3+f+f2kHq3s8OIY6KgpVNHip5qcvR9i9cVcZqTLemAtHcqHVAxzbsR5GIT/6EL8sS/mKtzSh71EdJ6h3n2v285Nfe4lGJltSUVllOXaA/gQDZZY6ohyxCwKpuJepingAv73KHT109ZgHxaO2SqmyxoQrJ7gkEY6t0AVJ5RQi4tUheeXGAg+B9yK8INwyEedjyum+QGhtIEDQduDsX+GT1jolVOLMjJ+QyIDeYBv0NOym+CfF9gIiDj7hD+Us+ItDIgTjId5zyUgmnYyxSjzjS0uktt0Y8P9gKn/26tAgMBAAEwDQYJKoZIhvcNAQELBQADggEBAEaMrHry/EzFtN35QH/bVcaS+Vu2Vx12DkfKyWw0Zlbqmg6LMhXF5TtN0u4BQ/r2p/wyChEGbdlqjH3RiLfnSa4e/f6+RX71mWz3UStvbUERV3QrqxJdiBY+fura/hbnSclrIX9ZpeUTTcwfkvG0RmC2t+jqY3bDOiK81MJzlgLPc5s0eZ07/e49U/hrecuQeezlU0ouoeoN4YW53dFuUSbN0ZQEJaGk82iVWbiytudEVka6KmfxBDXbyfZi/hkuQhQTZGRMX704sjKVXDcRDTRUbs4D5rJi4YJSX1avkcMdU+aXMGcC7BtcynaHkZIpg9Z0U27vErFF8ASXCsQ/bAg=");
        CertificateFactory cf = CertificateFactory.getInstance("X509");
        X509Certificate certificate = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(certificateData));
        PublicKey publicKey = certificate.getPublicKey();
        JWTVerifier verifier = JWT.require(Algorithm.RSA256((RSAPublicKey) publicKey, null)).build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt;
    }

}
