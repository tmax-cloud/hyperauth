//package com.tmax.hyperauth.caller;
//
//import com.google.gson.Gson;
//
//import javax.net.ssl.*;
//import java.io.*;
//import java.net.URL;
//import java.security.KeyStore;
//import java.security.cert.Certificate;
//import java.security.cert.CertificateFactory;
//
//
//public class HypercloudWebhookCaller {
//
//	private static final String CALLER_NAME = "HypercloudWebhookCaller";
//
//	private static String setWebhookURL( String serviceName )  {
//		return Constants.HYPERCLOUD4_WEBHOOK_URL + "/" + serviceName;
//	}
//
//
//	public static void auditAuthentication(AuditEvent.Event event) throws Exception {
//		System.out.println(" [Hypercloud4-webhook] Call Hypercloud4 webhook Audit Authentiaction POST Service" );
//
//		Gson gson = new Gson();
//		String json = gson.toJson(event);
//		System.out.println(json);
//
//		//POST svc
//		URL obj = new URL(setWebhookURL(Constants.SERVICE_NAME_AUDIT_AUTHENTICATION));
//
//		// Load CAs from an InputStream
//		CertificateFactory cf = CertificateFactory.getInstance("X.509");
//		InputStream caInput = new BufferedInputStream(new FileInputStream("/etc/x509/hc4-webhook/webhook_ca.crt"));
//		Certificate ca;
//		try {
//			ca = cf.generateCertificate(caInput);
////			System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
//		} finally {
//			caInput.close();
//		}
//
//		// Create a KeyStore containing our trusted CAs
//		String keyStoreType = KeyStore.getDefaultType();
//		KeyStore keyStore = KeyStore.getInstance(keyStoreType);
//		keyStore.load(null, null);
//		keyStore.setCertificateEntry("ca", ca);
//
//		// Create a TrustManager that trusts the CAs in our KeyStore
//		String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
//		TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
//		tmf.init(keyStore);
//
//		// Create an SSLContext that uses our TrustManager
//		SSLContext context = SSLContext.getInstance("TLS");
//		context.init(null, tmf.getTrustManagers(), null);
//
//		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
//		con.setSSLSocketFactory(context.getSocketFactory());
//
//		//add reuqest header
//		con.setRequestMethod("POST");
//		con.setRequestProperty("User-Agent", "Mozilla/5.0");
//		con.setRequestProperty("Content-Type", "application/json");
//		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
//
//		con.setDoOutput(true);
//		OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
//		out.write(json);
//		out.close();
//
////		System.out.println("Sending 'POST' request to URL : " + setWebhookURL(Constants.SERVICE_NAME_AUDIT_AUTHENTICATION));
//
//		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//		String inputLine;
//		StringBuffer response = new StringBuffer();
//
//		while ((inputLine = in.readLine()) != null) {
//			response.append(inputLine);
//		}
//		in.close();
//		System.out.println("webhook authentication Success");
//	}
//}
