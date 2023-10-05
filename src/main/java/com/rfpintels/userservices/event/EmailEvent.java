package com.rfpintels.userservices.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.web.util.UriComponentsBuilder;

import com.rfpintels.userservices.model.User;
import com.rfpintels.userservices.model.payload.EmailType;

public class EmailEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;
	private transient UriComponentsBuilder redirectUrl;
	private User user;
	private EmailType emailType;
	private String recipientEmailId;
	private String password;
	private String firstName;
	private String lastName;
	private String jobTittle;
	private String workEmail;
	private String phoneNo;
	private String companyName;
	private String companySize;
	private String comments;

	public EmailEvent(UriComponentsBuilder redirectUrl, User user, EmailType emailType, String recipientEmailId) {
		super(user);
		this.redirectUrl = redirectUrl;
		this.user = user;
		this.emailType = emailType;
		this.recipientEmailId = recipientEmailId;
	}

	public EmailEvent(User user, EmailType emailType, String password, String recipientEmailId) {
		super(user);
		this.user = user;
		this.emailType = emailType;
		this.password = password;
		this.recipientEmailId = recipientEmailId;
	}

	public EmailEvent(User user, EmailType emailType, String firstName, String lastName, String jobTittle,String workEmail,
			String phoneNo, String companyName, String companySize, String comments, String recipientEmailId) {
		super(user);
		this.user = user;
		this.emailType = emailType;
		this.firstName = firstName;
		this.lastName = lastName;
		this.jobTittle = jobTittle;
		this.workEmail = workEmail;
		this.phoneNo = phoneNo;
		this.companyName = companyName;
		this.companySize = companySize;
		this.comments = comments;
		this.recipientEmailId = recipientEmailId;
	}

	public String getWorkEmail() {
		return workEmail;
	}

	public void setWorkEmail(String workEmail) {
		this.workEmail = workEmail;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getJobTittle() {
		return jobTittle;
	}

	public void setJobTittle(String jobTittle) {
		this.jobTittle = jobTittle;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanySize() {
		return companySize;
	}

	public void setCompanySize(String companySize) {
		this.companySize = companySize;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRecipientEmailId() {
		return recipientEmailId;
	}

	public void setRecipientEmailId(String recipientEmailId) {
		this.recipientEmailId = recipientEmailId;
	}

	public UriComponentsBuilder getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(UriComponentsBuilder redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public EmailType getEmailType() {
		return emailType;
	}

	public void setEmailType(EmailType emailType) {
		this.emailType = emailType;
	}

	public EmailEvent(Object source, EmailType emailType, String recipientEmailId) {
		super(source);
		this.emailType = emailType;
		this.recipientEmailId = recipientEmailId;
	}

}
