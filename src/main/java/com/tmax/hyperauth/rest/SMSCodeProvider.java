package com.tmax.hyperauth.rest;

import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.spi.HttpResponse;
import org.keycloak.common.ClientConnection;
import org.keycloak.models.*;
import org.keycloak.services.resource.RealmResourceProvider;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.sql.*;

/**
 * @author taegeon_woo@tmax.co.kr
 */

@Slf4j
public class SMSCodeProvider implements RealmResourceProvider {
    @Context
    private KeycloakSession session;

    @Context
    private HttpResponse response;

    @Context
    private ClientConnection clientConnection;

    public SMSCodeProvider(KeycloakSession session) {
        this.session = session;
    }
    
    @Override
    public Object getResource() {
        return this;
    }

    Connection conn = null;
    long time = System.currentTimeMillis();
    Status status = null;
	String out = null;

    @POST
    @Path("{phone}")
    @QueryParam("sender")
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@PathParam("phone") final String phone, @QueryParam("sender") String sender) {
        log.info("***** POST /sms");
        log.info("phone : " + phone);
        log.info("sender : " + sender);
        String code = Util.numberGen(4, 1);
        log.debug("code : " + code);
        String msg = "[Web발신]\n[인증번호:" + code + "] - HyperAuth\n(타인노출금지)";
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            log.error("Error Occurs!!", e);
        } catch (ClassNotFoundException e) {
            log.error("Error Occurs!!", e);
        }

        try {
            String query = "insert into keycloak.MTS_SMS_MSG(TRAN_PR,TRAN_PHONE,TRAN_CALLBACK,TRAN_MSG,TRAN_DATE) "
                     + "values (KEYCLOAK.MTS_SMS_MSG_ID.NEXTVAL,?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString( 1, phone );
            pstmt.setString( 2, sender );
            pstmt.setString( 3, msg );
            pstmt.setTimestamp( 4, new Timestamp(time) );

            pstmt.executeQuery();
            pstmt.close();
            conn.commit();
            conn.close();
            log.info("Insert into MTS_SMS_MSG Success!!");
            status = Status.OK;
            out = "SMS Send Success";
            return Util.setCors(status, out);

        } catch (SQLException e) {
            log.error("Error Occurs!!", e);
            status = Status.BAD_REQUEST;
            out = "SMS Send Failed";
            return Util.setCors(status, out);

        } catch (Exception e) {
            log.error("Error Occurs!!", e);
            status = Status.BAD_REQUEST;
            out = "SMS Send Failed";
            return Util.setCors(status, out);
        }
    }

	@GET
    @Path("{phone}")
    @QueryParam("code")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("phone") final String phone, @QueryParam("code") String code) {
        log.info("***** GET /sms");
        log.info("phone : " + phone);
        log.info("code : " + code);
        String codeDB = "";
        Timestamp insertTime = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            log.error("Error Occurs!!", e);
        } catch (ClassNotFoundException e) {
            log.error("Error Occurs!!", e);
        }

        try {
            String query = "select * from KEYCLOAK.MTS_SMS_MSG where TRAN_PHONE = ? order by TRAN_DATE desc;";
            PreparedStatement pstmt = conn.prepareStatement(query);

            pstmt.setString( 1, phone );
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                String msg = rs.getString("TRAN_MSG");
                insertTime = rs.getTimestamp("TRAN_DATE");
                codeDB = msg.substring(14,18);
                log.info("codeDB : " + codeDB);
                log.info("insertTime : " + insertTime);
            }
            pstmt.close();
            conn.commit();
            conn.close();
            Timestamp currentTimeMinus5min = new Timestamp(time-5*1000*60);
            if ( currentTimeMinus5min.before(insertTime)){
                if (code.equalsIgnoreCase(codeDB)){
                    status = Status.OK;
                    out = "Phone Validation Passed";
                } else {
                    status = Status.UNAUTHORIZED;
                    out = "Wrong Verification Code";
                }
            }else {
                status = Status.UNAUTHORIZED;
                out = "SMS Verification Time Expired";
            }
            return Util.setCors(status, out);

        } catch (SQLException e) {
            status = Status.BAD_REQUEST;
            out = "Phone Validation Failed";
            return Util.setCors(status, out);
        }catch (Exception e) {
            log.error("Error Occurs!!", e);
            status = Status.BAD_REQUEST;
        	out = "Phone Validation Failed";
            return Util.setCors(status, out);
        }
    }

    private Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName( "com.tmax.tibero.jdbc.TbDriver" );
        return DriverManager.getConnection( "jdbc:tibero:thin:@tibero.hyperauth:8629:tibero"
                , "keycloak", "keycloak" );
    }

    @OPTIONS
    @Path("{path : .*}")
    public Response other() {
        log.info("***** OPTIONS /sms");
        return Util.setCors( Status.OK, null);
    }

    @Override
    public void close() {
    }

}