package com.rfpintels.userservices.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.rfpintels.userservices.controller.SuperAdminController;
import com.rfpintels.userservices.model.payload.EmailType;
import com.rfpintels.userservices.model.payload.Mail;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

@Service
public class MailService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SuperAdminController.class);

	private final JavaMailSender mailSender;

	private final Configuration templateConfiguration;

	@Value("${app.templates.location}")
	private String basePackagePath;

	@Value("${spring.mail.username}")
	private String mailFrom;

	@Value("${app.token.password.reset.duration}")
	private Long expiration;

	@Autowired
	public MailService(JavaMailSender mailSender, Configuration templateConfiguration) {
		super();
		this.mailSender = mailSender;
		this.templateConfiguration = templateConfiguration;
	}

	public void sendEmailVerification(String emailVerificationURL, String to, EmailType emailType)
			throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException,
			TemplateException, MessagingException {
		Mail mail = null;
		Template template = null;
		String mailContent = null;
		if (emailType != null) {
			switch (emailType) {
			case REGISTRATION_SUCCESFULL_EMAIL_LINK:
				mail = Mail.builder().subject("Email Verification").to(to).from(mailFrom)
						.model(Map.of("userName", to, "userEmailTokenVerificationLink", emailVerificationURL)).build();
				templateConfiguration.setClassForTemplateLoading(getClass(), basePackagePath);
				template = templateConfiguration.getTemplate("email-verification.ftl");
				mailContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, mail.getModel());
				mail.setContent(mailContent);
				this.send(mail);
				break;
			case REGISTRATION_EMAIL_LINK:
				mail = Mail.builder().subject("Email Registration").to(to).from(mailFrom)
						.model(Map.of("userName", to, "userEmailTokenRegistrationLink", emailVerificationURL)).build();
				templateConfiguration.setClassForTemplateLoading(getClass(), basePackagePath);
				template = templateConfiguration.getTemplate("email-registration.ftl");
				mailContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, mail.getModel());
				mail.setContent(mailContent);
				this.send(mail);
				break;
			case FORGOT_PASSWORD:
				mail = Mail.builder().subject("Forgot Password").to(to).from(mailFrom)
						.model(Map.of("userName", to, "resetPasswordLink", emailVerificationURL)).build();
				templateConfiguration.setClassForTemplateLoading(getClass(), basePackagePath);
				template = templateConfiguration.getTemplate("email-resetPassword.ftl");
				mailContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, mail.getModel());
				mail.setContent(mailContent);
				this.send(mail);
				break;
			case USER_REGISTERED_SUCCESFULL:
				mail = Mail.builder().subject("User Registered Succesfull").to(to).from(mailFrom)
						.model(Map.of("userName", to, "userRegisteredSuccessfull", emailVerificationURL)).build();
				templateConfiguration.setClassForTemplateLoading(getClass(), basePackagePath);
				template = templateConfiguration.getTemplate("email-registerSucess.ftl");
				mailContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, mail.getModel());
				mail.setContent(mailContent);
				this.send(mail);
				break;
			case USER_APPROVE_EMAIL_LINK:
				mail = Mail.builder().subject("Email For Approval").to(to).from(mailFrom)
						.model(Map.of("userName", to, "userEmailLoginLink", emailVerificationURL)).build();
				templateConfiguration.setClassForTemplateLoading(getClass(), basePackagePath);
				template = templateConfiguration.getTemplate("email-approval.ftl");
				mailContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, mail.getModel());
				mail.setContent(mailContent);
				this.send(mail);
				break;
			case USER_DENY_EMAIL_LINK:
				mail = Mail.builder().subject("Email For Deny").to(to).from(mailFrom)
						.model(Map.of("userName", to, "userDeny", emailVerificationURL)).build();
				templateConfiguration.setClassForTemplateLoading(getClass(), basePackagePath);
				template = templateConfiguration.getTemplate("email-deny.ftl");
				mailContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, mail.getModel());
				mail.setContent(mailContent);
				this.send(mail);
				break;
			default:
				break;
			}
		} else

		{
			LOGGER.error("Fatal error - emailType not defined");
		}
	}

	public void sendEmailVerification(String to, EmailType emailType, String Password) throws TemplateNotFoundException,
			MalformedTemplateNameException, ParseException, IOException, TemplateException, MessagingException {
		Mail mail = null;
		Template template = null;
		String mailContent = null;
		mail = Mail.builder().subject("Email Password").to(to).from(mailFrom)
				.model(Map.of("userName", to, "password", Password)).build();
		templateConfiguration.setClassForTemplateLoading(getClass(), basePackagePath);
		template = templateConfiguration.getTemplate("email-password.ftl");
		mailContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, mail.getModel());
		mail.setContent(mailContent);
		this.send(mail);
	}

	public void sendEmailVerification(String to, EmailType emailType, String firstName, String lastName,
			String jobTittle, String workEmail, String phoneNo, String companyName, String companySize, String comments)
			throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException,
			TemplateException, MessagingException {
		Mail mail = null;
		Template template = null;
		String mailContent = null;
		mail = Mail.builder().subject("Contact Email").to(to).from(mailFrom)
				.model(Map.of("userName", to, "firstName", firstName, "lastName", lastName, "jobTittle", jobTittle,
						"workEmail", workEmail, "phoneNo", phoneNo, "companyName", companyName, "companySize",
						companySize, "comments", comments))
				.build();
		templateConfiguration.setClassForTemplateLoading(getClass(), basePackagePath);
		template = templateConfiguration.getTemplate("email-contact.ftl");
		mailContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, mail.getModel());
		mail.setContent(mailContent);
		this.send(mail);
	}

	public void send(Mail mail) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());

		helper.setTo(mail.getTo());
		helper.setText(mail.getContent(), true);
		helper.setSubject(mail.getSubject());
		helper.setFrom(mail.getFrom());

		mailSender.send(message);
	}

}
