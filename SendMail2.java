package com.gmail.gmail;

import java.util.Properties;    
import javax.mail.*;    
import javax.mail.internet.*; 
public class SendMail2 {
public void Send(String fromEmailAddress, String toEmailAddress)
	
{
	 
	// Recipient's email ID needs to be mentioned.
    String to = toEmailAddress;

    // Sender's email ID needs to be mentioned
    String from = fromEmailAddress;

    // Assuming you are sending email from through gmails smtp
    String host = "smtp.gmail.com";

    // Get system properties
    Properties props = System.getProperties();

    // Setup mail server
    
    
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.socketFactory.port", "465");
    props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.port", "465");
    props.put("mail.smtp.ssl.trust", host);

    // Get the Session object.// and pass username and password
    Session session = Session.getInstance(props, new javax.mail.Authenticator() {

        protected PasswordAuthentication getPasswordAuthentication() {

            return new PasswordAuthentication(fromEmailAddress, "wgkntckdfxopxhqd");//app password

        }

    });

    // Used to debug SMTP issues
    session.setDebug(true);

    try {
        // Create a default MimeMessage object.
        MimeMessage message = new MimeMessage(session);

        // Set From: header field of the header.
        message.setFrom(new InternetAddress(from));

        // Set To: header field of the header.
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

        // Set Subject: header field
        message.setSubject("This is the Subject Line!");

        // Now set the actual message
        message.setText("This is actual message");

        System.out.println("sending...");
        // Send message
        Transport.send(message);
        
        System.out.println("Sent message successfully....");
    } catch (MessagingException mex) {
        mex.printStackTrace();
    }
	
}
	

}
