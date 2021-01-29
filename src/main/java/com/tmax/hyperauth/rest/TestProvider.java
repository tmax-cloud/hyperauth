package com.tmax.hyperauth.rest;

import org.jboss.resteasy.spi.HttpResponse;
import org.keycloak.common.ClientConnection;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.authentication.AuthenticationFlowContext;

import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;


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
    @Produces(MediaType.APPLICATION_JSON)
    public Response post() throws Throwable {
        System.out.println("***** POST /test");
        return Util.setCors(status, out);
    }

    @GET
    @Path("{sessionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("sessionId") final String sessionId) {
        System.out.println("***** GET /test");
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