package seslambda;

import javax.print.attribute.standard.Destination;
import javax.swing.plaf.synth.Region;

import java.util.List;
import java.util.Map.Entry;
import java.sql.*;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;


public class LambdaFunctionHandler implements RequestHandler<Object, Object> {
	 static final String FROM = "Your Email";  // Replace with your "From" address. This address must be verified.
	    static final String TO = "Where do you want to send"; // Replace with a "To" address. If you have not yet requested
	                                                      // production access, this address must be verified.
	    static final String BODY = "Congrats! Your order has been placed!";
	    static final String SUBJECT = "Thankyou from Edureka Live!";
	    private com.amazonaws.services.simpleemail.model.Destination x;
		private com.amazonaws.services.simpleemail.model.Message y;

	 public Object handleRequest(Object input, Context context) {
		
		
		    
		    
	        /*
	         * The ProfileCredentialsProvider will return your [default]
	         * credential profile by reading from the credentials file located at
	         * (C:\\Users\\Hemanth\\.aws\\credentials).
	         */
	        AWSCredentials credentials = null;
	        try {
	        	credentials = new BasicAWSCredentials("Your key", "Your Secret Key");
	        } catch (Exception e) {
	            throw new AmazonClientException("error",
	                    e);
	        }

	        AmazonS3 s3 = new AmazonS3Client(credentials);
	        AmazonSQS sqs = new AmazonSQSClient(credentials);
	        com.amazonaws.regions.Region usWest2 = com.amazonaws.regions.Region.getRegion(Regions.US_WEST_2);
	        sqs.setRegion(usWest2);
	       
	        String rec="";
	        try{
	        	String myQueueUrl = "Queue URL";
	        	String to="email address";
	            // Receive messages
	            String B="";
	            System.out.println("Receiving messages from MyQueue.\n");
	            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(myQueueUrl);
	            List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
	            String base1="";String extension1="";
	            for (Message message : messages) {
	                rec=message.getBody();
	               
	               to = rec;
	               
	            }
	            //SES BEGIN
	            // Construct an object to contain the recipient address.
	            com.amazonaws.services.simpleemail.model.Destination destination = new com.amazonaws.services.simpleemail.model.Destination().withToAddresses(new String[]{to});

		        // Create the subject and body of the message.
		        Content subject = new Content().withData(SUBJECT);
		        Content textBody = new Content().withData(BODY);
		        Body body = new Body().withText(textBody);

		        // Create a message with the specified subject and body.
		        com.amazonaws.services.simpleemail.model.Message message = new com.amazonaws.services.simpleemail.model.Message().withSubject(subject).withBody(body);

		        // Assemble the email.
		        SendEmailRequest request = new SendEmailRequest().withSource(FROM).withDestination(destination).withMessage(message);

		        try {
		            System.out.println("Attempting to send an email through Amazon SES by using the AWS SDK for Java...");
		            AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient(credentials);
		            com.amazonaws.regions.Region REGION = com.amazonaws.regions.Region.getRegion(Regions.US_WEST_2);
		            client.setRegion(REGION);

		            // Send the email.
		            client.sendEmail(request);
		            System.out.println("Email sent!");

		        } catch (Exception ex) {
		            System.out.println("The email was not sent.");
		            System.out.println("Error message: " + ex.getMessage());
		        }
		        //END OF SES
	         // Delete the message
	            System.out.println("Receiving messages from MyQueue."+rec+"\n");
	            String messageReceiptHandle = messages.get(0).getReceiptHandle();
            	sqs.deleteMessage(new DeleteMessageRequest(myQueueUrl, messageReceiptHandle));
	        }
	        catch (AmazonServiceException ase) {
	            System.out.println("Caught an AmazonServiceException, which means your request made it " +
	                    "to Amazon SQS, but was rejected with an error response for some reason.");
	            System.out.println("Error Message:    " + ase.getMessage());
	            System.out.println("HTTP Status Code: " + ase.getStatusCode());
	            System.out.println("AWS Error Code:   " + ase.getErrorCode());
	            System.out.println("Error Type:       " + ase.getErrorType());
	            System.out.println("Request ID:       " + ase.getRequestId());
	        }
	        
	    
	return null;
	 }


}
