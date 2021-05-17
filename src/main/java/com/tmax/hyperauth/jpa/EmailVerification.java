package com.tmax.hyperauth.jpa;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "EMAIL_VERIFICATION")
@NamedQueries({ @NamedQuery(name = "findByEmail", query = "from EmailVerification where email = :email order by verificationDate desc")
				, @NamedQuery(name = "updateIsVerified", query = "update EmailVerification set isVerified = :isVerified where email = :email and code = :code")})
public class EmailVerification {
		@Id
		@Column(name = "ID")
		private String id;

	    @Column(name = "EMAIL")
	    private String email;

	    @Column(name = "CODE", nullable = false)
	    private String code;

		@Column(name = "IS_VERIFIED")
		private boolean isVerified;
	    
	    @Column(name = "VERIFICATION_DATE", nullable = false)
	    private Timestamp verificationDate;

		public String getId() {
			return id;
		}

		public String getEmail() {
			return email;
		}
	    
	    public String getCode() {
	        return code;
	    }

	    public boolean getIsVerified() { return isVerified; }

	    public Timestamp getVerificationDate() {
			return verificationDate;
		}

		public void setId(String id) {
			this.id = id;
		}

		public void setEmail(String email) {
		this.email = email;
	}

	    public void setCode(String code) {
	        this.code = code;
	    }

		public void setVerified(boolean verified) { this.isVerified = verified; }

	    public void setVerificationDate(Timestamp verificationDate) {
	        this.verificationDate = verificationDate;
	    }
}
