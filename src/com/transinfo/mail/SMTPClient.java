/**
* Copyright (c) 2007-2008 Trans-Info Pte Ltd. Singapore. All Rights Reserved.
* This work contains trade secrets and confidential material of
* Trans-Info Pte Ltd. Singapore and its use of disclosure in whole
* or in part without express written permission of
* Trans-Info Pte Ltd. Singapore. is prohibited.
* Date of Creation   : Feb 25, 2008
* Version Number     : 1.0
*                   Modification History:
* Date          Version No.         Modified By           Modification Details.
*/

package com.transinfo.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import com.transinfo.tplus.TPlusConfig;
import com.transinfo.tplus.TPlusException;

public class SMTPClient implements ISMTP
{
	public static void main(String[] args) throws TPlusException {
		TPlusConfig.loadProperties();
		new SMTPClient().sendMail("The Subject","Mail Testing");
	}

	public boolean sendMail(String strSubject, String strMsg) throws TPlusException {

		try
		{
			System.out.println(TPlusConfig.getSMTPTo()+" "+TPlusConfig.getSMTPFrom()+" "+TPlusConfig.getSMTPServer()+" "+TPlusConfig.getSMTPPort()+" "+TPlusConfig.getSMTPUserName()+" "+TPlusConfig.getSMTPPassword());			
			Message message = new MimeMessage(getSession());

			message.addRecipient(RecipientType.TO, new InternetAddress(TPlusConfig.getSMTPTo()));
			message.addFrom(new InternetAddress[] { new InternetAddress(TPlusConfig.getSMTPFrom())});

			message.setSubject(strSubject);
			message.setContent(strMsg, "text/plain");

			Transport.send(message);
		}
		catch(MessagingException msgExp)
		{
			throw new TPlusException("1001",msgExp.toString());
		}
		
		return true;
	}

	private Session getSession() {
		Authenticator authenticator = new Authenticator();

		Properties properties = new Properties();
		properties.setProperty("mail.smtp.submitter", authenticator.getPasswordAuthentication().getUserName());
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.host", TPlusConfig.getSMTPServer());
		properties.setProperty("mail.smtp.port", TPlusConfig.getSMTPPort());

		return Session.getInstance(properties, authenticator);
	}

	private class Authenticator extends javax.mail.Authenticator {
		private PasswordAuthentication authentication;

		public Authenticator()
		{
			String username = TPlusConfig.getSMTPUserName();
			String password = TPlusConfig.getSMTPPassword();
			authentication = new PasswordAuthentication(username, password);
		}

		protected PasswordAuthentication getPasswordAuthentication() {
			return authentication;
		}
	}
}
