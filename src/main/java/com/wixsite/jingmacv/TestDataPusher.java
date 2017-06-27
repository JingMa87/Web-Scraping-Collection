package com.wixsite.jingmacv;

import java.io.FileWriter;
import java.io.PrintWriter;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.Topic;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestDataPusher {

	// --- Start of connection details
	private static final String URL = "ssl://api.bmreports.com:61616"; // This is the connection string to the ELEXON servers
	private static final String APIKEY = "<YOUR API KEY GOES HERE>"; // This is your API key from the portal
	private static final String CLIENTID = "<YOUR CLIENT ID GOES HERE>"; // This is a client name that needs to be unique (this you create)
	private static final String TOPICNAME = "bmrsTopic"; // This is the topic name
	private static final String SUBSCRIPTIONID = "<YOUR SUBSCRIPTION ID GOES HERE>"; // Each durable subscription needs an ID that is unique (this you create)
	// --- End of connection details
	private static final Logger LOGGER = LoggerFactory.getLogger(TestDataPusher.class);
	private Connection connection;
	private Session session;
	private MessageConsumer messageConsumer;
	private static TestDataPusher subscriberPublishSubscribe;
	
	/**
	* Generic start point.
	*
	* @param args the command line arguments
	* @throws java.lang.Exception
	*/
	public static void main(String[] args) throws Exception {
		try {
			// Setup and connect to the queue
			subscriberPublishSubscribe = new TestDataPusher();
			subscriberPublishSubscribe.create(URL, APIKEY, CLIENTID, TOPICNAME, SUBSCRIPTIONID);
		} catch (Exception ex) {
			LOGGER.error(ex.getLocalizedMessage());
			if (subscriberPublishSubscribe != null) {
				subscriberPublishSubscribe.closeConnection();
			}
		}
	}
	
	/**
	* This is the method that initiates the connection and sets up the
	* JMSListener
	*
	* @param url - The server and connection protocol
	* @param apikey - the api key to connect with
	* @param clientId - Unique id for this client
	* @param topicName - The topic to listen to
	* @throws JMSException
	*/
	public void create(String url, String apikey, String clientId, String topicName, String subId) throws JMSException {
		// create a Connection Factory
		ConnectionFactory factory = new ActiveMQConnectionFactory(apikey, apikey, url);
		try {
			// create a Connection
			LOGGER.debug("Creating a connection");
			connection = factory.createConnection();
			connection.setClientID(clientId);
			// create a Session
			LOGGER.debug("Creating a session");
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			// create the Topic from which messages will be received
			LOGGER.debug("Creating the topic connection: " + topicName);
			Topic topic = session.createTopic(topicName);
			// Set up the message consumer
			LOGGER.debug("Creating the consumer for: " + topicName);
			//messageConsumer = session.createConsumer(topic);
			messageConsumer = session.createDurableSubscriber(topic, subId);
			// Create the listener.
			LOGGER.debug("Setting up the listener");
			JMSMessageListener listener = new JMSMessageListener();
			messageConsumer.setMessageListener(listener);
			// start the connection in order to receive messages
			LOGGER.debug("Starting the connection");
			connection.start();
		} catch (JMSException exp) {
			throw exp;
		}
	}
	
	public void closeConnection() throws JMSException {
		LOGGER.debug("Closing the connection");
		connection.close();
	}
	
	/**
	 * This class implements a message listener for the ActiveMQ
	 */
	class JMSMessageListener implements MessageListener {
		
		@Override
		public void onMessage(javax.jms.Message msg) {
			try {
				LOGGER.info(msg.toString());
				ActiveMQTextMessage txtMessage = (ActiveMQTextMessage) msg;
				LOGGER.info(txtMessage.getText());
				try (PrintWriter out = new PrintWriter(new FileWriter(txtMessage.getJMSMessageID()))) {
					out.print(txtMessage.getText());
				}
			} catch (Exception ex) {
				LOGGER.error(ex.getLocalizedMessage());
			}
		}
	}
}