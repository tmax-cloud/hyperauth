package com.tmax.hyperauth.rest;

import org.jboss.resteasy.spi.HttpResponse;
import org.keycloak.common.ClientConnection;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;

import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.ArrayList;
import java.util.List;

/**
 * @author taegeon_woo@tmax.co.kr
 */

public class SessionProvider implements RealmResourceProvider {
    @Context
    private KeycloakSession session;

    @Context
    private HttpResponse response;

    @Context
    private ClientConnection clientConnection;

    public SessionProvider(KeycloakSession session) {
        this.session = session;
    }
    
    @Override
    public Object getResource() {
        return this;
    }

    Status status = null;
	String out = null;

    @GET
    @Path("{sessionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("sessionId") final String sessionId) {
        System.out.println("***** GET /session");
        System.out.println("sessionId : " + sessionId);
        try {
            out = "off";
            boolean isRememberMe = session.sessions().getUserSession(session.realms().getRealmByName("tmax"), sessionId).isRememberMe();
            if (isRememberMe) out = "on";
            status = Status.OK;
            return Util.setCors(status, out);

        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception " + e.getMessage());
            status = Status.BAD_REQUEST;
            out = "Get Session IsRememberMe failed";
            return Util.setCors(status, out);
        }
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