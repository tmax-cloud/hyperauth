//package com.tmax.hyperauth.jpa;
//
//import javax.persistence.*;
//
//@Entity
//@Table(name = "USER_PICTURE")
//@NamedQueries({ @NamedQuery(name = "findByUserID", query = "from ProfilePicture where userName = :userName and realmId = :realmId") })
//public class ProfilePicture {
//	@Id
//	@Column(name = "ID")
//	private String id;
//
//	@Column(name = "USER_NAME", nullable = false)
//	private String userName;
//
//	@Column(name = "REALM_ID", nullable = false)
//	private String realmId;
//
//	@Column(name = "IMAGE", nullable = false)
//	private byte[] image;
//
//	public String getId() {
//		return id;
//	}
//
//	public String getUserName() {
//		return userName;
//	}
//
//	public String getRealmId() { return realmId; }
//
//	public byte[] getImage() {
//		return image;
//	}
//
//	public void setId(String id) {
//		this.id = id;
//	}
//
//	public void setUserName(String userName) {
//		this.userName = userName;
//	}
//
//	public void setRealmId(String realmId) {
//		this.realmId = realmId;
//	}
//
//	public void setImage(byte[] image) {
//	        this.image = image;
//	    }
//
//}
