package com.rfpintels.userservices.event.listener;

import java.io.IOException;
import javax.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import com.rfpintels.userservices.event.EmailEvent;
import com.rfpintels.userservices.exception.MailSendException;
import com.rfpintels.userservices.model.User;
import com.rfpintels.userservices.model.payload.EmailType;
import com.rfpintels.userservices.service.EmailVerificationTokenService;
import com.rfpintels.userservices.service.MailService;
import freemarker.template.TemplateException;
import lombok.Data;

@Data
@Component
public class EmailListener implements ApplicationListener<EmailEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailListener.class);

	@Autowired
	private final EmailVerificationTokenService emailVerificationTokenService;

	@Autowired
	private final MailService mailService;

	@Async
	@Override
	public void onApplicationEvent(EmailEvent onUserRegistrationCompleteEvent) {
		sendEmailVerification(onUserRegistrationCompleteEvent);
	}

	private void sendEmail(EmailEvent event) {
		User user = event.getUser();
		EmailType emailType = event.getEmailType();
		String recipientEmailAdress = event.getRecipientEmailId();
		// String recipientEmailAdress = user.getEmail(
		String emailURL = event.getRedirectUrl().toUriString();
		try {
			mailService.sendEmailVerification(emailURL, recipientEmailAdress, emailType);
		} catch (Exception e) {
			String msg = "Email Registration";
			LOGGER.error(msg, e);
			throw new MailSendException(recipientEmailAdress, msg);
		}
	}

//	private void sendEmailVerification(EmailEvent event) {
//		User user = event.getUser();
//		String userId = user.getId();
//		String emailId = user.getEmail();
//		EmailType emailType = event.getEmailType();
//		String token = emailVerificationTokenService.generateNewToken();
//		emailVerificationTokenService.createVerificationToken(user, userId);
//		emailVerificationTokenService.createVerificationToken(user, token);
//		emailVerificationTokenService.createVerificationToken(user, emailId);
//		String recipientEmailAddress = event.getRecipientEmailId();
//		String emailConfirmationURL = event.getRedirectUrl().queryParam("token", token).queryParam("userId", userId)
//				.queryParam("emailId", emailId).toUriString();
//		try {
//			mailService.sendEmailVerification(emailConfirmationURL, recipientEmailAddress, emailType);
//		} catch (IOException | TemplateException | MessagingException e) {
//			String msg = "Email Verification";
//			LOGGER.error(msg, e);
//			throw new MailSendException(recipientEmailAddress, msg);
//		}
//	}

	private void sendEmailVerification(EmailEvent event) {
		EmailType emailType = event.getEmailType();
		User user = event.getUser();
		String emailId = user.getEmail();

		if (emailType != null) {
			switch (emailType) {
			case REGISTRATION_SUCCESFULL_EMAIL_LINK:
				String userId1 = user.getId();
				String token1 = emailVerificationTokenService.generateNewToken();

				emailVerificationTokenService.createVerificationToken(user, userId1);
				emailVerificationTokenService.createVerificationToken(user, token1);
				emailVerificationTokenService.createVerificationToken(user, emailId);
				String recipientEmailAddress1 = event.getRecipientEmailId();
				String emailConfirmationURL1 = event.getRedirectUrl().queryParam("token", token1)
						.queryParam("userId", userId1).queryParam("emailId", emailId).toUriString();
				try {
					mailService.sendEmailVerification(emailConfirmationURL1, recipientEmailAddress1, emailType);
				} catch (IOException | TemplateException | MessagingException e) {
					String msg = "Email Verification";
					LOGGER.error(msg, e);
					throw new MailSendException(recipientEmailAddress1, msg);
				}
				break;
			case REGISTRATION_EMAIL_LINK:
				String userId2 = user.getId();
				String token2 = emailVerificationTokenService.generateNewToken();

				emailVerificationTokenService.createVerificationToken(user, userId2);
				emailVerificationTokenService.createVerificationToken(user, token2);
				emailVerificationTokenService.createVerificationToken(user, emailId);
				String recipientEmailAddress2 = event.getRecipientEmailId();
				String emailConfirmationURL2 = event.getRedirectUrl().queryParam("token", token2)
						.queryParam("userId", userId2).queryParam("emailId", emailId).toUriString();
				try {
					mailService.sendEmailVerification(emailConfirmationURL2, recipientEmailAddress2, emailType);
				} catch (IOException | TemplateException | MessagingException e) {
					String msg = "Email Verification";
					LOGGER.error(msg, e);
					throw new MailSendException(recipientEmailAddress2, msg);
				}
				break;
			case FORGOT_PASSWORD:
				String userId3 = user.getId();
				String token3 = emailVerificationTokenService.generateNewToken();
				emailVerificationTokenService.createVerificationToken(user, userId3);
				emailVerificationTokenService.createVerificationToken(user, token3);
				emailVerificationTokenService.createVerificationToken(user, emailId);
				String recipientEmailAddress3 = event.getRecipientEmailId();
				String emailConfirmationURL3 = event.getRedirectUrl().queryParam("token", token3)
						.queryParam("userId", userId3).queryParam("emailId", emailId).toUriString();
				try {
					mailService.sendEmailVerification(emailConfirmationURL3, recipientEmailAddress3, emailType);
				} catch (IOException | TemplateException | MessagingException e) {
					String msg = "Email Verification";
					LOGGER.error(msg, e);
					throw new MailSendException(recipientEmailAddress3, msg);
				}
				break;
			case USER_REGISTERED_SUCCESFULL:
				String userId4 = user.getId();
				String token4 = emailVerificationTokenService.generateNewToken();

				emailVerificationTokenService.createVerificationToken(user, userId4);
				emailVerificationTokenService.createVerificationToken(user, token4);
				emailVerificationTokenService.createVerificationToken(user, emailId);
				String recipientEmailAddress4 = event.getRecipientEmailId();
				String emailConfirmationURL4 = event.getRedirectUrl().queryParam("token", token4)
						.queryParam("userId", userId4).queryParam("emailId", emailId).toUriString();
				try {
					mailService.sendEmailVerification(emailConfirmationURL4, recipientEmailAddress4, emailType);
				} catch (IOException | TemplateException | MessagingException e) {
					String msg = "Email Verification";
					LOGGER.error(msg, e);
					throw new MailSendException(recipientEmailAddress4, msg);
				}
				break;
			case USER_APPROVE_EMAIL_LINK:
				String userId5 = user.getId();
				String token5 = emailVerificationTokenService.generateNewToken();

				emailVerificationTokenService.createVerificationToken(user, userId5);
				emailVerificationTokenService.createVerificationToken(user, token5);
				emailVerificationTokenService.createVerificationToken(user, emailId);
				String recipientEmailAddress5 = event.getRecipientEmailId();
				String emailConfirmationURL5 = event.getRedirectUrl().queryParam("token", token5)
						.queryParam("userId", userId5).queryParam("emailId", emailId).toUriString();
				try {
					mailService.sendEmailVerification(emailConfirmationURL5, recipientEmailAddress5, emailType);
				} catch (IOException | TemplateException | MessagingException e) {
					String msg = "Email Verification";
					LOGGER.error(msg, e);
					throw new MailSendException(recipientEmailAddress5, msg);
				}
				break;
			case USER_DENY_EMAIL_LINK:
				String userId6 = user.getId();
				String token6 = emailVerificationTokenService.generateNewToken();

				emailVerificationTokenService.createVerificationToken(user, userId6);
				emailVerificationTokenService.createVerificationToken(user, token6);
				emailVerificationTokenService.createVerificationToken(user, emailId);
				String recipientEmailAddress6 = event.getRecipientEmailId();
				String emailConfirmationURL6 = event.getRedirectUrl().queryParam("token", token6)
						.queryParam("userId", userId6).queryParam("emailId", emailId).toUriString();
				try {
					mailService.sendEmailVerification(emailConfirmationURL6, recipientEmailAddress6, emailType);
				} catch (IOException | TemplateException | MessagingException e) {
					String msg = "Email Verification";
					LOGGER.error(msg, e);
					throw new MailSendException(recipientEmailAddress6, msg);
				}
				break;
			case PASSWORD_EMAIL:
				String recipientEmailAddress55 = event.getRecipientEmailId();
				String password = event.getPassword();
				try {
					mailService.sendEmailVerification(recipientEmailAddress55, emailType, password);
				} catch (IOException | TemplateException | MessagingException e) {
					String msg = "Email Password";
					LOGGER.error(msg, e);
					throw new MailSendException(recipientEmailAddress55, msg);
				}
				break;
			case CONTACT_EMAIL:
				String recipientEmailAddress66 = event.getRecipientEmailId();
				String firstName = event.getFirstName();
				String lastName = event.getLastName();
				String jobTittle = event.getJobTittle();
				String workEmail = event.getWorkEmail();
				String phoneNo = event.getPhoneNo();
				String companyName = event.getCompanyName();
				String companySize = event.getCompanySize();
				String comments = event.getComments();
				try {
					mailService.sendEmailVerification(recipientEmailAddress66, emailType, firstName, lastName,
							jobTittle,workEmail, phoneNo, companyName, companySize, comments);
				} catch (IOException | TemplateException | MessagingException e) {
					String msg = "Contact Email";
					LOGGER.error(msg, e);
					throw new MailSendException(recipientEmailAddress66, msg);
				}
				break;
			}
		}
	}
};
