package com.tmax.hyperauth.rest;

import com.tmax.hyperauth.caller.Constants;
import com.tmax.hyperauth.jpa.EmailVerification;
import org.jboss.resteasy.spi.HttpResponse;
import org.keycloak.common.ClientConnection;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.utils.KeycloakModelUtils;
import org.keycloak.services.resource.RealmResourceProvider;

import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author taegeon_woo@tmax.co.kr
 */

public class TestProvider implements RealmResourceProvider {
    @Context
    private KeycloakSession session;

    @Context
    private HttpResponse response;

    @Context
    private ClientConnection clientConnection;

    public TestProvider(KeycloakSession session) {
        this.session = session;
    }
    
    @Override
    public Object getResource() {
        return this;
    }

    Status status = null;
	String out = null;

    @POST
    @Path("{path}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@PathParam("path") final String email, @QueryParam("username") String username) throws Throwable {
        System.out.println("***** POST /test");
        System.out.println( session.users().getUserByEmail("admin@tmax.co.kr", session.realms().getRealmByName("tmax")).getAttribute("test").get(0) );
        System.out.println();
        System.out.println( session.users().getUserByEmail("admin@tmax.co.kr", session.realms().getRealmByName("tmax")).getAttribute("test").get(1) );
        System.out.println();
        System.out.println( session.users().getUserByEmail("admin@tmax.co.kr", session.realms().getRealmByName("tmax")).getAttribute("test1").get(0) );
        System.out.println();
        System.out.println( session.users().getUserByEmail("admin@tmax.co.kr", session.realms().getRealmByName("tmax")).getAttribute("test1").get(1) );
        System.out.println();
        System.out.println( session.users().getUserByEmail("admin@tmax.co.kr", session.realms().getRealmByName("tmax")).getAttribute("test2").get(0) );
        System.out.println();
        System.out.println( session.users().getUserByEmail("admin@tmax.co.kr", session.realms().getRealmByName("tmax")).getAttribute("test2").get(1) );
        System.out.println();

        status = Status.OK;
        out = "test post svc Success";
        return Util.setCors(status, out);
    }



    @OPTIONS
    @Path("{path : .*}")
    public Response other() {
        System.out.println("***** OPTIONS /email");
        return Util.setCors( Status.OK, null);
    }

    @Override
    public void close() {
    }

    private EntityManager getEntityManager() {
        return session.getProvider(JpaConnectionProvider.class).getEntityManager();
    }
}