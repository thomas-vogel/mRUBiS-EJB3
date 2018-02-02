package de.hpi.sam.rubis.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Maintains the last {@code ItemFilterMonitor#sampleLength} measurements of the
 * selection rate and computation time of all (bean) instances of a specific
 * filter (bean). Hence, instances of the same filter share this class.
 * 
 * The class provides method to obtain the moving average over the last
 * {@code ItemFilterMonitor#sampleLength} measurements of the selection rate and
 * computation time for all (bean) instances of the filter (bean) since these
 * measurements are relevant at the deployment level and not at the instance
 * level.
 * 
 * -Dimq.autocreate.destination.maxNumProducers=-1
 * 
 * @author thomas vogel
 *
 */
public class ItemFilterMonitor {

	private boolean monitoredActivated = false;

	private String filterId;
	private long invocationCount;
	private int sampleLength = 10;

	private double selectionRateCount;
	private ArrayList<Double> selectionRateRingBuffer;

	private long computationTimeCount;
	private ArrayList<Long> computationTimeRingBuffer;

	private boolean sysout = false;

	// Properties
	private final String computationTimePropertyName = "local-computation-time";
	private final String computationTimePropertyType = "java.lang.Long";
	private final String computationTimePropertyUnit = "ms";
	private final String computationTimePropertyDescription = "Average computation time of a filter bean.";

	private final String selectionRatePropertyName = "selection-rate";
	private final String selectionRatePropertyType = "java.lang.Double";
	private final String selectionRatePropertyUnit = "";
	private final String selectionRatePropertyDescription = "Average selection rate of a filter bean.";

	// Connection to the message queue
	private boolean init = false;
	private boolean sendMsgs = true; // to deactivate messaging for debugging
	private Context jndiContext = null;
	private TopicConnectionFactory topicConnectionFactory = null;
	private TopicConnection topicConnection = null;
	private TopicSession topicSession = null;
	private Topic topic = null;
	private TopicPublisher topicPublisher = null;
	// TODO Define topic properties that are also configured in the GlassFish
	// server.
	private final String topicFactoryName = "set topic factory name";
	private final String topicName = "set topic name";

	// End of Connection to the message queue

	/**
	 * Creates a monitor for the filter with the given name.
	 * 
	 * @param filterId
	 *            the filter's fully qualified class name
	 */
	public ItemFilterMonitor(String filterId) {
		this.filterId = filterId;
		this.invocationCount = 0;

		this.selectionRateCount = 0;
		this.selectionRateRingBuffer = new ArrayList<Double>(this.sampleLength);

		this.computationTimeCount = 0;
		this.computationTimeRingBuffer = new ArrayList<Long>(this.sampleLength);
	}

	private boolean isConnectionOpen() {
		return this.topicConnection != null;
	}

	private void closeConnection() {
		if (this.isConnectionOpen()) {
			try {
				this.topicPublisher.close();
				this.topicSession.close();
				this.topicConnection.close();

				this.topicPublisher = null;
				this.topicSession = null;
				this.topicConnection = null;

			} catch (JMSException ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	private void createConnection() {
		try {
			this.topicConnection = this.topicConnectionFactory.createTopicConnection();
			this.topicSession = this.topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			this.topicPublisher = this.topicSession.createPublisher(this.topic);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	private void initJMS() {
		if (this.sendMsgs) {

			/*
			 * Create a JNDI API InitialContext object.
			 */
			try {
				this.jndiContext = new InitialContext();
			} catch (NamingException e) {
				String msg = "Could not create JNDI " + "context: " + e.toString();
				System.out.println(msg);
				throw new RuntimeException(msg, e);
			}

			/*
			 * Look up connection factory and topic. If either does not exist, exit.
			 */
			try {
				this.topicConnectionFactory = (TopicConnectionFactory) this.jndiContext.lookup(this.topicFactoryName);
			} catch (NamingException e) {
				String msg = "JNDI lookup failed: " + e.toString();
				System.out.println(msg);
				throw new RuntimeException(msg, e);
			}

			try {
				// this.topicConnection = this.topicConnectionFactory
				// .createTopicConnection();
				// NOT SUPPORTED IN THIS CONTEXT:
				// this.topicConnection.setClientID(this.filterId);
				// this.topicSession = topicConnection.createTopicSession(false,
				// Session.AUTO_ACKNOWLEDGE);
				this.topic = (Topic) this.jndiContext.lookup(this.topicName);
			} catch (Exception e) {
				String msg = "Connection problem: " + e.toString();
				System.out.println(msg);
				if (this.topicConnection != null) {
					try {
						this.topicConnection.close();
					} catch (JMSException ee) {
					}
				}
				throw new RuntimeException(msg, e);
			}

			// try {
			// this.topicPublisher = this.topicSession
			// .createPublisher(this.topic);
			// } catch (JMSException e) {
			// String msg = "Filter: " + this.filterId
			// + " -- Connection problem: " + e.toString();
			// System.out.println(msg);
			// throw new RuntimeException(msg, e);
			// }
		}
	}

	// is it deployable having a synchronized method? YES!
	public synchronized List<MonitoredPropertyNotification> addData(int initialItemListSize, int processedItemListSize,
			double selectionRateThreshold, long startComputationTime, long endComputationTime,
			double computationTimeThreshold) {

		if (this.monitoredActivated) {

			if (!this.init) {
				this.initJMS();
				this.init = true;
			}

			List<MonitoredPropertyNotification> messagesToSend = new ArrayList<MonitoredPropertyNotification>(2);

			if (this.invocationCount < this.sampleLength) {
				// warm up

				// increment the invocation count
				this.invocationCount++;

				// Update selection rate
				double selectionRate = 1.00 - new Double(processedItemListSize) / new Double(initialItemListSize);
				this.updateSelectionRate(selectionRate);

				// Update computation time
				long computationTime = endComputationTime - startComputationTime;
				this.updateComputationTime(computationTime);

				if (this.sysout) {
					System.out.println(this.invocationCount + ". execution of the filter " + this.filterId
							+ ". Latest selection rate: " + selectionRate + ". Latest computation time: "
							+ computationTime);
				}

			} else if (this.invocationCount == this.sampleLength) {
				// increment the invocation count
				this.invocationCount++;

				// Update selection rate
				double selectionRate = 1.00 - new Double(processedItemListSize) / new Double(initialItemListSize);
				this.updateSelectionRate(selectionRate);

				double newSelectionRateAvg = this.getMovingAvgOfSelectionRates();
				if (this.sysout) {
					System.out.println("Selection rate of the filter " + this.filterId + " is initially set to "
							+ newSelectionRateAvg);
				}
				MonitoredPropertyNotification notification = new MonitoredPropertyNotification(this.filterId,
						this.selectionRatePropertyName, new Double(newSelectionRateAvg).toString(),
						new Double(0).toString(), this.selectionRatePropertyType, this.selectionRatePropertyUnit,
						this.selectionRatePropertyDescription);
				messagesToSend.add(notification);

				// Update computation time
				long computationTime = endComputationTime - startComputationTime;
				this.updateComputationTime(computationTime);

				long newComputationTimeAvg = this.getMovingAvgOfComputationTimes();
				if (this.sysout) {
					System.out.println("Computation time of the filter " + this.filterId + " is initially set to "
							+ newComputationTimeAvg);
				}
				MonitoredPropertyNotification notification2 = new MonitoredPropertyNotification(this.filterId,
						this.computationTimePropertyName, new Long(newComputationTimeAvg).toString(),
						new Long(0).toString(), this.computationTimePropertyType, this.computationTimePropertyUnit,
						this.computationTimePropertyDescription);
				messagesToSend.add(notification2);
			} else {
				// end of warm up phase

				// calculate old moving averages
				double oldSelectionRateAvg = this.getMovingAvgOfSelectionRates();
				long oldComputationTimeAvg = this.getMovingAvgOfComputationTimes();

				// increment the invocation count
				this.invocationCount++;

				// Update selection rate
				double selectionRate = 1.00 - new Double(processedItemListSize) / new Double(initialItemListSize);
				this.updateSelectionRate(selectionRate);

				double newSelectionRateAvg = this.getMovingAvgOfSelectionRates();
				double selectionRateAvgChange = Math.abs(newSelectionRateAvg - oldSelectionRateAvg)
						/ oldSelectionRateAvg;

				if (selectionRateAvgChange > selectionRateThreshold) {
					// send a message to inform about a significant change of
					// the filter's selection rate
					if (this.sysout) {
						System.out.println("Selection rate of the filter " + this.filterId + " changed from "
								+ oldSelectionRateAvg + " to " + newSelectionRateAvg);
					}

					MonitoredPropertyNotification notification = new MonitoredPropertyNotification(this.filterId,
							this.selectionRatePropertyName, new Double(newSelectionRateAvg).toString(),
							new Double(oldSelectionRateAvg).toString(), this.selectionRatePropertyType,
							this.selectionRatePropertyUnit, this.selectionRatePropertyDescription);
					messagesToSend.add(notification);
				}

				// Update computation time
				long computationTime = endComputationTime - startComputationTime;
				this.updateComputationTime(computationTime);

				long newComputationTimeAvg = this.getMovingAvgOfComputationTimes();
				long computationTimeAvgChange = Math.abs(newComputationTimeAvg - oldComputationTimeAvg)
						/ oldComputationTimeAvg;

				if (computationTimeAvgChange > computationTimeThreshold) {
					// send a message to inform about a significant change of
					// the filter's computation time
					if (this.sysout) {
						System.out.println("Computation time of the filter " + this.filterId + " changed from "
								+ oldComputationTimeAvg + " to " + newComputationTimeAvg);
					}

					MonitoredPropertyNotification notification2 = new MonitoredPropertyNotification(this.filterId,
							this.computationTimePropertyName, new Long(newComputationTimeAvg).toString(),
							new Long(oldComputationTimeAvg).toString(), this.computationTimePropertyType,
							this.computationTimePropertyUnit, this.computationTimePropertyDescription);
					messagesToSend.add(notification2);
				}

				if (this.sysout) {
					System.out.println(this.invocationCount + ". execution of the filter " + this.filterId
							+ ". Latest selection rate: " + selectionRate + " (Moving average: " + oldSelectionRateAvg
							+ " +/- " + selectionRateAvgChange * 100 + " % = " + newSelectionRateAvg + ")"
							+ ". Latest computation time: " + computationTime + " (Moving average: "
							+ oldComputationTimeAvg + " +/- " + computationTimeAvgChange * 100 + " % = "
							+ newComputationTimeAvg + ")");
				}
			}

			this.createConnection();
			this.notifyChange(messagesToSend);
			this.closeConnection();
			return messagesToSend;
		} else {
			return Collections.EMPTY_LIST;
		}
	}

	private double getMovingAvgOfSelectionRates() {
		double sum = 0;
		for (Double datapoint : this.selectionRateRingBuffer) {
			sum = sum + datapoint;
		}
		double avg = sum / this.selectionRateRingBuffer.size();
		return avg;
	}

	private long getMovingAvgOfComputationTimes() {
		long sum = 0;
		for (Long datapoint : this.computationTimeRingBuffer) {
			sum = sum + datapoint;
		}
		double avg = new Double(sum) / this.computationTimeRingBuffer.size();
		long result = Math.round(avg);
		return result;
	}

	private void updateSelectionRate(double newSelectionRate) {
		this.selectionRateCount = this.selectionRateCount + newSelectionRate;
		if (this.selectionRateRingBuffer.size() >= this.sampleLength) {
			this.selectionRateRingBuffer.remove(0);
		}
		this.selectionRateRingBuffer.add(newSelectionRate);
	}

	private void updateComputationTime(long newComputationTime) {
		this.computationTimeCount = this.computationTimeCount + newComputationTime;
		if (this.computationTimeRingBuffer.size() >= this.sampleLength) {
			this.computationTimeRingBuffer.remove(0);
		}
		this.computationTimeRingBuffer.add(newComputationTime);
	}

	private void notifyChange(List<MonitoredPropertyNotification> notifications) {
		if (this.sendMsgs) {
			for (MonitoredPropertyNotification notification : notifications) {
				this.sendMessage(notification);
			}
		}
	}

	private void sendMessage(MonitoredPropertyNotification notification) {
		try {
			ObjectMessage msg = this.topicSession.createObjectMessage();
			msg.setObject(notification);

			msg.setStringProperty(MonitoredPropertyNotification.IDENTIFIER_PROPERTY, notification.getNotificationId());
			msg.setStringProperty(MonitoredPropertyNotification.LEVEL_PROPERTY,
					MonitoredPropertyNotification.APP_SPECIFIC_LEVEL);

			this.topicPublisher.publish(msg);

		} catch (JMSException e) {
			String msg = "Message sending problem: " + e.toString();
			System.out.println(msg);
			throw new RuntimeException(msg, e);
		}
	}
}
