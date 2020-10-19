package com.tmax.hyperauth.rest;

import java.util.List;

import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.annotations.cache.NoCache;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.utils.KeycloakModelUtils;
import org.keycloak.services.resource.RealmResourceProvider;

import com.tmax.hyperauth.jpa.Agreement;

/**
 * @author taegeon_woo@tmax.co.kr
 */

public class AgreementProvider implements RealmResourceProvider { 
    @Context
    private KeycloakSession session;
    
    public AgreementProvider(KeycloakSession session) {	
        this.session = session;
    }
    
    @Override
    public Object getResource() {
        return this;
    }
    
    Status status = null;
	String out = null;

    @GET
    @Path("{clientName}")
    @NoCache
    @Produces("text/plain; charset=utf-8")
    public Response get(@PathParam("clientName") final String clientName, @QueryParam("realmName") String realmName , @QueryParam("version") String version ) {
    	System.out.println("clientName : " + clientName + ", version : " + version + "Agreement Get Service");
    	List< Agreement > agreementList = getEntityManager().createNamedQuery("findByRealmAndClient", Agreement.class)
                .setParameter("realmName", realmName).setParameter("clientName", clientName).setParameter("version", version).getResultList();
    	if (agreementList != null && agreementList.size() != 0) {
    		status = Status.OK;
        	out = agreementList.get(0).getAgreement();
    	} else {
    		status = Status.BAD_REQUEST;
        	out = "No Corresponding Agreement";
    	}
    	return Util.setCors(status, out);
    }
    
	@SuppressWarnings("null")
	@POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("")
    @NoCache
    @Produces("text/plain; charset=utf-8")
    public Response post(Agreement agreement) {
    	System.out.println("clientName : " + agreement.getClientName());
    	System.out.println("realmName : " + agreement.getRealmName());
    	System.out.println("agreement : " + agreement.getAgreement());
    	System.out.println("version : " + agreement.getVersion());
    	System.out.println("Agreement Create Service");

        String id = agreement.getId()==null ?  KeycloakModelUtils.generateId() : agreement.getId();

        //Delete If Already Exists
        List < Agreement > prevAgreementList = getEntityManager().createNamedQuery("findByRealmAndClient", Agreement.class)
        .setParameter("realmName", agreement.getRealmName()).setParameter("clientName", agreement.getClientName())
        .setParameter("version", agreement.getVersion()).getResultList();
        
        if(prevAgreementList != null && prevAgreementList.size() != 0) {
        	getEntityManager().remove(prevAgreementList.get(0));
        }
        
        //Create New Entity
        Agreement entity = new Agreement();
        
        entity.setId(id);
        entity.setClientName(agreement.getClientName());
        entity.setRealmName(agreement.getRealmName());
        entity.setAgreement(agreement.getAgreement());
        entity.setVersion(agreement.getVersion());
        
        try {
            getEntityManager().persist(entity);
            status = Status.OK;
        	out = "Agreement " + agreement.getClientName() + " is Added ( Version : " + agreement.getVersion() + " )";
        } catch (Exception e) {
        	status = Status.BAD_REQUEST;
        	out = "Agreement " + agreement.getClientName() + " ( Version : " + agreement.getVersion() + " ) Create Failed";
        }	 
    	return Util.setCors(status, out);
    }
    
    @DELETE
    @Path("{clientName}")
    @NoCache
    @Produces("text/plain; charset=utf-8")
    public Response delete(@PathParam("clientName") final String clientName, @QueryParam("realmName") String realmName , @QueryParam("version") String version ) {
    	System.out.println("clientName : " + clientName + ", version : " + version + "Agreement Delete Service");
    	List< Agreement > agreementList = getEntityManager().createNamedQuery("findByRealmAndClient", Agreement.class)
                .setParameter("realmName", realmName).setParameter("clientName", clientName).setParameter("version", version).getResultList();
    	if (agreementList != null && agreementList.size() != 0) {
        	getEntityManager().remove(agreementList.get(0));
    		status = Status.OK;
        	out = agreementList.get(0).getAgreement() + "Delete Success";
    	} else {
    		status = Status.BAD_REQUEST;
        	out = "No Corresponding Agreement";
    	}
    	return Util.setCors(status, out);
    }

    @OPTIONS
    @Path("{path : .*}")
    public Response other() {
        System.out.println("***** OPTIONS /Agreement");
        return Util.setCors( Status.OK, null);
    }

    @Override
    public void close() {
    }
    
    private EntityManager getEntityManager() {
        return session.getProvider(JpaConnectionProvider.class).getEntityManager();
    }
}