package de.hpi.sam.rubis.filter;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.List;

/**
 * Abstract class for filter beans.
 * 
 * @author thomas vogel
 *
 */
public class AbstractFilterBean {

	private boolean sysout = false;

	/**
	 * Identifier of the filter.
	 */
	protected String filterId;

	/**
	 * Set the filter id.
	 */
	protected void setFilterId(String filterName) {
		this.filterId = filterName;
	}

	/**
	 * Busy waiting of the current thread until <code>timeInMs</code> has elapsed.
	 * 
	 * @param timeInMs
	 *            time in ms of busy waiting
	 */
	protected void useCPU(long timeInMs) {

		long before = System.nanoTime();

		long timeInNS = timeInMs * 1000000;
		long currentThreadId = Thread.currentThread().getId();
		ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
		long nanoNow = mxBean.getThreadCpuTime(currentThreadId);
		while (mxBean.getThreadCpuTime(currentThreadId) - nanoNow < timeInNS) {
			// busy waiting
		}

		if (this.sysout) {
			long after = System.nanoTime();
			System.out.println(this.filterId + ": busy waiting done. Spent " + ((after - before) / 1000000) + " ms.");
		}
	}

	public void initConnectionToQueue() {
		// Do nothing. Functionality shifted to the ItemFilterMonitor
	}

	public void notifyChange(List<MonitoredPropertyNotification> msgs) {
		// Do nothing. Functionality shifted to the ItemFilterMonitor
	}
}
