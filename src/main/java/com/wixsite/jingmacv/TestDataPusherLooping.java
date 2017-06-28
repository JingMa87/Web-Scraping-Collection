package com.wixsite.jingmacv;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.NamingException;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestDataPusherLooping {

	// --- Start of connection details
	private static final String URL = "ssl://api.bmreports.com:61616"; // This is the connection string to the ELEXON servers
	private static final String APIKEY = "69qnl68twxam19w"; // This is your API key from the portal
	private static final String CLIENTID = "jing"; // This is a client name that needs to be unique (this you create)
	private static final String TOPICNAME = "bmrsTopic"; // This is the topic name
	private static final String SUBSCRIPTIONID = "subid"; // Each durable subscription needs an ID that is unique (this you create)
	// --- End of connection details
	private static final Logger LOGGER = LoggerFactory.getLogger(TestDataPusherLooping.class);
	private Connection connection;
	private Session session;
	private MessageConsumer messageConsumer;
	private boolean transacted;
	private boolean isRunning = false;
	private static TestDataPusherLooping consumer;
	
	/**
	* @param args the command line arguments
	* @throws java.lang.InterruptedException
	*/
	public static void main(String[] args) throws InterruptedException {
		BasicConfigurator.configure();
		int retryCount = 20000;
		int count = 0;
		consumer = new TestDataPusherLooping();
		// This runs forever
		while (count < retryCount) {
			LOGGER.debug("Attempting connection. Count = " + count);
			try {
				consumer.run();
			} catch (NamingException | JMSException ex) {
				LOGGER.error(ex.getLocalizedMessage());
				count++;
			} finally {
				LOGGER.debug("Shutting down");
			}
			Thread.sleep(1000);
		}
	}
	
	public void run() throws NamingException, JMSException {
		isRunning = true;
		// create a Connection Factory
		ConnectionFactory factory = new ActiveMQConnectionFactory(APIKEY, APIKEY, URL);
		// create a Connection
		LOGGER.debug("Creating a connection");
		connection = factory.createConnection();
		connection.setClientID(CLIENTID);
		// create a Session
		LOGGER.debug("Creating a session");
		session = connection.createSession(transacted, Session.AUTO_ACKNOWLEDGE);
		// create the Topic from which messages will be received
		LOGGER.debug("Creating the topic connection: " + TOPICNAME);
		Topic topic = session.createTopic(TOPICNAME);
		// Set up the message consumer
		LOGGER.debug("Creating the consumer for: " + TOPICNAME);
		messageConsumer = session.createDurableSubscriber(topic, SUBSCRIPTIONID);
		// start the connection in order to receive messages
		LOGGER.debug("Starting the connection");
		connection.start();
		while (isRunning) {
			LOGGER.debug("Waiting for message...");
			Message message = messageConsumer.receive(1000);
			if (message != null && message instanceof TextMessage) {
				TextMessage txtMsg = (TextMessage) message;
				LOGGER.debug("Received: " + txtMsg.getText());
			}
		}
		LOGGER.debug("Closing connection");
		messageConsumer.close();
		session.close();
		connection.close();
	}
}