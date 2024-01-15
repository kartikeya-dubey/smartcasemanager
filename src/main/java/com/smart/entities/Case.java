package com.smart.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.ToString;

@ToString
@Entity
@Table(name="CASES")
public class Case {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cid;
	
	@Column(name = "client_name")
	private String clientName;     //Name
	private String caseName;   //secondName
	private String caseType;   //work / criminal, civil, corporate
	private String email;
	private String phone;
	private String image;     // to store picture of power of attorney/waqalatnama
	
	@Column(length=1000)
	private String caseDetails;

	@ManyToOne
	@JsonIgnore
	private User user;

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getCaseName() {
		return caseName;
	}

	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}

	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getCaseDetails() {
		return caseDetails;
	}

	public void setCaseDetails(String caseDetails) {
		this.caseDetails = caseDetails;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

//	@Override
//	public String toString() {
//		return "Case [cid=" + cid + ", clientName=" + clientName + ", caseName=" + caseName + ", caseType=" + caseType
//				+ ", email=" + email + ", phone=" + phone + ", image=" + image + ", caseDetails=" + caseDetails
//				+ ", user=" + user + "]";
//	}
	
	
}
