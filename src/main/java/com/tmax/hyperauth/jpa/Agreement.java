package com.tmax.hyperauth.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "CLIENT_AGREEMENT")
@NamedQueries({ @NamedQuery(name = "findByRealmAndClient", query = "from Agreement where realmName = :realmName and clientName = :clientName and version = :version") })
public class Agreement {
	 	@Id
	    @Column(name = "ID")
	    private String id;

	    @Column(name = "CLIENT_NAME", nullable = false)
	    private String clientName;

	    @Column(name = "REALM_NAME", nullable = false)
	    private String realmName;
	    
	    @Column(name = "AGREEMENT", nullable = false)
	    private String agreement;

	    @Column(name = "VERSION", nullable = false)
	    private String version;
	    
	    public String getId() {
			return id;
		}
	    
	    public String getClientName() {
	        return clientName;
	    }
	    
	    public String getRealmName() {
			return realmName;
		}
	    
	    public String getAgreement() {
			return agreement;
		}
	    
	    public String getVersion() {
			return version;
		}

	    public void setId(String id) {
	        this.id = id;
	    }

	    public void setClientName(String clientName) {
	        this.clientName = clientName;
	    }

	    public void setRealmName(String realmName) {
	        this.realmName = realmName;
	    }
	    
	    public void setAgreement(String agreement) {
	        this.agreement = agreement;
	    }
	    
	    public void setVersion(String version) {
	        this.version = version;
		}
}
