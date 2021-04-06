package com.tmax.hyperauth.rest;

import org.jboss.resteasy.spi.HttpResponse;
import org.keycloak.common.ClientConnection;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.authentication.AuthenticationFlowContext;

import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author taegeon_woo@tmax.co.kr
 */

public class GroupProvider implements RealmResourceProvider {
    @Context
    private KeycloakSession session;

    @Context
    private HttpResponse response;

    @Context
    private ClientConnection clientConnection;

    public GroupProvider(KeycloakSession session) {
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
    public Response post() {
        System.out.println("***** POST /group");
        return Util.setCors(status, out);
    }

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response list(@QueryParam("startsWith") String startsWith, @QueryParam("except") List<String> except, @QueryParam("exceptDefault") boolean exceptDefault) {
        System.out.println("***** LIST /group");
        List<String> groupListOut;
        System.out.println("startsWith request : " + startsWith);
        System.out.println("except request : " + except);

        try{
            StringBuilder query = new StringBuilder();
            query.append("select g.name from GroupEntity g where g.realm = '"+ session.getContext().getRealm().getName() +"'");

            if (startsWith != null){
                startsWith = startsWith.toLowerCase();
                query.append(" and lower(g.name) like '" + startsWith + "%'");
            }

            if (except != null && except.size() > 0){
                query.append(" and not g.name in (");
                for (int i=0 ; i < except.size() ; i ++){
                    query.append("'" + except.get(i) + "'");
                    if (i != except.size()-1){
                        query.append(",");
                    }
                }
                query.append(" )");
            }

            System.out.println("query : " + query.toString());
            groupListOut = getEntityManager().createQuery(query.toString(), String.class).getResultList();

            if (exceptDefault){
                List<GroupModel> defaultGroups = session.getContext().getRealm().getDefaultGroups();
                if (defaultGroups != null && defaultGroups.size() > 0){
                    groupListOut.removeIf(
                            group ->(
                                    defaultGroups.stream()
                                            .map(GroupModel::getName)
                                            .collect(Collectors.toList())
                                            .contains(group)));
                }
            }
            status = Status.OK;
            return Util.setCors(status, groupListOut);
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception " + e.getMessage());
            status = Status.BAD_REQUEST;
            out = "Group ListGet Failed";
            return Util.setCors(status, out);
        }
    }

    @OPTIONS
    @Path("{path : .*}")
    public Response other() {
        System.out.println("***** OPTIONS /group");
        return Util.setCors( Status.OK, null);
    }

    @Override
    public void close() {
    }

    private EntityManager getEntityManager() {
        return session.getProvider(JpaConnectionProvider.class).getEntityManager();
    }
}