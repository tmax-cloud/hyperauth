package com.tmax.hyperauth.rest;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.tmax.hyperauth.eventlistener.prometheus.PrometheusExporter;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.crypto.KeyWrapper;
import org.keycloak.models.KeycloakSession;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.stream.Stream;

@Slf4j
public class Util {

	public static class MailImage {
		private String path;
		private String cid;
		public MailImage(String path, String cid ) {
			this.path = path;
			this.cid = cid;
		}
		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public String getCid() {
			return cid;
		}

		public void setCid(String cid) {
			this.cid = cid;
		}
	}

    public static Response setCors( Status status, Object out ) {
		return Response.status(status).entity(out)
    			.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Credentials", "true")
    			.header("Access-Control-Max-Age", "3628800")
    			.header("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE, HEAD, PATCH")
        		.header("Access-Control-Allow-Headers", "*" ).build();
    }

	public static String numberGen(int len, int dupCd ) {

		Random rand = new Random();
		String numStr = ""; //난수가 저장될 변수

		for(int i=0;i<len;i++) {
			String ran = null;
			//0~9 까지 난수 생성 ( 첫자리에 0 인 경우는 제외 )
			if (i == 0) {
				ran = Integer.toString(rand.nextInt(9)+1);
			}else {
				ran = Integer.toString(rand.nextInt(10));
			}
			if(dupCd==1) {
				//중복 허용시 numStr에 append
				numStr += ran;
			}else if(dupCd==2) {
				//중복을 허용하지 않을시 중복된 값이 있는지 검사한다
				if(!numStr.contains(ran)) {
					//중복된 값이 없으면 numStr에 append
					numStr += ran;
				}else {
					//생성된 난수가 중복되면 루틴을 다시 실행한다
					i-=1;
				}
			}
		}
		return numStr;
	}

	public static void sendMail(KeycloakSession keycloakSession, String recipient, String subject, String body, List<MailImage> imgParts, String realmName) throws Throwable {
		log.info( " Send Mail to User [ " + recipient + " ] Start");

		String host = "mail.tmax.co.kr";
		int port = 25;
		String sender = "tmaxcloud_ck@tmax.co.kr";
		if(System.getenv("DEFAULT_EMAIL_SENDER")!= null){
			sender = System.getenv("DEFAULT_EMAIL_SENDER");
		}
		log.info( " Default Sender : "  + sender );

		String un = "tmaxcloud_ck@tmax.co.kr";
		String pw = "Miracle!";
		try{
			if (keycloakSession != null) {
				host = keycloakSession.getContext().getRealm().getSmtpConfig().get("host");
				if ((keycloakSession.getContext().getRealm().getSmtpConfig().get("port") !=  null)) {
					port = Integer.parseInt(keycloakSession.getContext().getRealm().getSmtpConfig().get("port"));
				}
				sender = keycloakSession.getContext().getRealm().getSmtpConfig().get("from");
				un = keycloakSession.getContext().getRealm().getSmtpConfig().get("user");
				pw = keycloakSession.getContext().getRealm().getSmtpConfig().get("password");
			}
		}catch( Exception e){
			log.error( " Failed to get SmtpConfig from Session " );
			log.error("Error Occurs!!", e);
		}

		log.info( " sender : "  + sender );
		log.info( " host : "  + host );
		log.info( " port : "  + port );
		log.info( " un : "  + un );
		log.debug( " pw : "  + pw );

		String charSetUtf = "UTF-8" ;
		Properties props = System.getProperties();
		props.put( "mail.transport.protocol", "smtp" );
		props.put( "mail.smtp.host", host );
		props.put( "mail.smtp.port", port );
		props.put( "mail.smtp.ssl.trust", host );
		props.put( "mail.smtp.auth", "true" );
		props.put( "mail.smtp.starttls.enable", "true" );
		props.put("mail.smtp.ssl.protocols", "TLSv1.2");

		String finalUn = un;
		String finalPw = pw;

		// For recordMailRequestCount Metric Collect
		PrometheusExporter.instance().recordMailRequestCount(realmName, host);
		////

		Session session = Session.getInstance( props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(finalUn, finalPw);
			}
		});

		session.setDebug( true );
		MimeMessage mimeMessage = new MimeMessage(session);

		// Sender
		mimeMessage.setFrom( new InternetAddress(sender, sender, charSetUtf));

		// Receiver
		mimeMessage.setRecipient( Message.RecipientType.TO, new InternetAddress( recipient ) );

		// Make Subject
		mimeMessage.setSubject( MimeUtility.encodeText(subject,  charSetUtf, "B") );

		// Make Body ( text/html + img )
		MimeMultipart multiPart = new MimeMultipart();

		log.debug( " Mail Body : "  + body );
		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(body, "text/html; charset="+charSetUtf);
		multiPart.addBodyPart(messageBodyPart);

		// Image Parts
		if (imgParts != null && imgParts.size() > 0){
			for ( MailImage img : imgParts ){
				BodyPart messageImgPart = new MimeBodyPart();
				if (img != null){
					DataSource ds = new FileDataSource(img.getPath());
					messageImgPart.setDataHandler(new DataHandler(ds));
//					messageImgPart.setHeader("Content-Type", "image/svg");
					messageImgPart.setHeader("Content-ID", "<"+img.getCid()+">");
					log.debug( " img.getPath() : "  + img.getPath() );
					log.debug( " img.getCid() : "  + img.getCid() );
					multiPart.addBodyPart(messageImgPart);
				}
			}
		}

		mimeMessage.setContent(multiPart);
		log.info( " Ready to Send Mail to " + recipient);
		try {
			//Send Mail
			Transport.send( mimeMessage );
			log.info( " Sent E-Mail to " + recipient);
			// For recordMailSendCount Metric Collect
			PrometheusExporter.instance().recordMailSendCount(realmName, host);
			////
		}catch (MessagingException e) {
			// For recordFailedMailSendCount Metric Collect
			PrometheusExporter.instance().recordFailedMailSendCount(realmName, host);
			////

			log.error("Error Occurs!!", e);
			throw e;
		}
	}

	public static String readLineByLineJava8(String filePath)
	{
		StringBuilder contentBuilder = new StringBuilder();

		try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8)) {
			stream.forEach(s -> contentBuilder.append(s).append("\n"));
		}
		catch (IOException e){
			log.error("Error Occurs!!", e);
		}
		return contentBuilder.toString();
	}

	public static DecodedJWT verifyAdminToken(String token, X509Certificate certificate) throws Exception {
		try{
			PublicKey publicKey = certificate.getPublicKey();
			JWTVerifier verifier = JWT.require(Algorithm.RSA256((RSAPublicKey) publicKey, null)).build();
			DecodedJWT jwt = verifier.verify(token);
			return jwt;
		}catch (Exception e){
			throw e;
		}
	}

	public static boolean isHyperauthAdmin( KeycloakSession session, String tokenString ){
		try{
			String userName = null;
			KeyWrapper kw = session.keys().getKeys(session.realms().getRealmByName("master")).stream().filter(k ->
					k.getAlgorithm().equalsIgnoreCase("RS256")
			).findFirst().get();
			DecodedJWT adminToken = verifyAdminToken( tokenString, kw.getCertificate());
			userName = adminToken.getClaim("preferred_username").asString();
			log.info("User Who Requested : " + userName);
			if(session.users().getUserByUsername(userName, session.realms().getRealmByName("master")).hasRole(session.realms().getRealmByName("master").getRole("admin"))) {
				log.info("Admin User : " + userName);
				return true;
			}else {
				log.info("Master Realm user but not Admin");
				return false;
			}
		} catch(Exception e){
			log.info("Error Occurred When Decoding Token, Not Admin User");
			return false;
		}
	}
}
