package com.gmail.gmail;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.Base64;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;
import com.google.api.services.gmail.model.Message;

public class SendMail {
	  private static final String APPLICATION_NAME = "OnlineExamSystem";
	  /**
	   * Global instance of the JSON factory.
	   */
	  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
	  /**
	   * Directory to store authorization tokens for this application.
	   */
	  private static final String TOKENS_DIRECTORY_PATH = "ya29.a0AeTM1idYqxsfZ4PvLDJQl4zrWFm5KJiRRvRWuQ9W-bJUMqz1vEXWfbb6Rxz1ClSaACbgfp0U37ysYsFEmwQ4ej7gCcHK3hnvoANIX232h5IR4zbvSAp_jBSGsIaW-O5IxikEHQzVsKXI2zDXr-kEMisYxXZkeQaCgYKAfoSARMSFQHWtWOmtAJuxxtatDlbcNmJg4DgCA0165";

	  /**
	   * Global instance of the scopes required by this quickstart.
	   * If modifying these scopes, delete your previously saved tokens/ folder.
	   */
	  private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_SEND);
	  private static final String CREDENTIALS_FILE_PATH = "H:\\SpringBoot\\project forder\\OnlineExamSystem\\src\\main\\resources\\static\\gmail/credential.json";

	  /**
	   * Creates an authorized Credential object.
	   *
	   * @param HTTP_TRANSPORT The network HTTP Transport.
	   * @return An authorized Credential object.
	   * @throws IOException If the credentials.json file cannot be found.
	   */
	  private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
	    // Load client secrets.
	   // InputStream in = GmailApplication.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
	    InputStream in = new FileInputStream("H:\\\\SpringBoot\\\\project forder\\\\OnlineExamSystem\\\\src\\\\main\\\\resources\\\\static\\\\gmail/credential.json" ); 
	    if (in == null) {
	      throw new FileNotFoundException("Resource not found: " + in);
	    }
	    GoogleClientSecrets clientSecrets =
	        GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

	    // Build flow and trigger user authorization request.
	    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
	        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets,SCOPES)
	        .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
	        .setAccessType("offline")
	        .build();
	    LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8084).build();
	    Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
	    //returns an authorized Credential object.
	    return credential;
	  }

	
	public Message Send(String fromEmailAddress, String toEmailAddress) throws GeneralSecurityException, IOException, AddressException, MessagingException
	
	{
		 final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		    Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
		        .setApplicationName(APPLICATION_NAME)
		        .build();	
		    
		    
		    String messageSubject = "Saddam Hossen";
		    String bodyText = "Can You Help Me For Study About Exam?";

		    // Encode as MIME message
		    Properties props = new Properties();
		    Session session = Session.getDefaultInstance(props, null);
		    MimeMessage email = new MimeMessage(session);
		    email.setFrom(new InternetAddress(fromEmailAddress));
		    email.addRecipient(javax.mail.Message.RecipientType.TO,
		        new InternetAddress(toEmailAddress));
		    email.setSubject(messageSubject);
		    email.setText(bodyText);

		    // Encode and wrap the MIME message into a gmail message
		    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		    email.writeTo(buffer);
		    byte[] rawMessageBytes = buffer.toByteArray();
		    String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
		    Message message = new Message();
		    message.setRaw(encodedEmail);

		    try {
		      // Create send message
		      message = service.users().messages().send("me", message).execute();
		      System.out.println("Message id: " + message.getId());
		      System.out.println(message.toPrettyString());
		      return message;
		    } catch (GoogleJsonResponseException e) {
		      // TODO(developer) - handle error appropriately
		      GoogleJsonError error = e.getDetails();
		      if (error.getCode() == 403) {
		        System.err.println("Unable to send message: " + e.getDetails());
		      } else {
		        throw e;
		      }
		    }
		    
		 // Print the labels in the user's account.
		    String user = "me";
		    ListLabelsResponse listResponse = service.users().labels().list(user).execute();
		    List<Label> labels = listResponse.getLabels();
		    if (labels.isEmpty()) {
		      System.out.println("No labels found.");
		    } else {
		      System.out.println("Labels:");
		      for (Label label : labels) {
		        System.out.printf("- %s\n", label.getName());
		      }
		    }
			return message;
	}
	

}
