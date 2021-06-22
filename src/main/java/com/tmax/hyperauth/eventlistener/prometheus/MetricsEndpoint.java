package com.tmax.hyperauth.eventlistener.prometheus;

import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

public class MetricsEndpoint implements RealmResourceProvider {

    public final static String ID = "metrics";

    @Context
    private KeycloakSession session;
    public MetricsEndpoint(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public Object getResource() {
        return this;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response get() {
        session.getContext().getRequestHeaders().getRequestHeaders().keySet().forEach( k -> {
            System.out.println("[ Header ] key : " + k + ",  value : " + session.getContext().getRequestHeaders().getRequestHeader(k));
        });

        System.out.println("uri : " + session.getContext().getUri().getPath());
        // for User Count
        PrometheusExporter.instance().recordUserCount(session);

        final StreamingOutput stream = output -> PrometheusExporter.instance().export(output);
        return Response.ok(stream).build();
    }

    @Override
    public void close() {
    }
}
