package de.hpi.sam.rubis.filter.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import de.hpi.sam.rubis.entity.Item;
import de.hpi.sam.rubis.entity.User;
import de.hpi.sam.rubis.filter.AbstractFilterBean;
import de.hpi.sam.rubis.filter.ItemFilter;
import de.hpi.sam.rubis.filter.ItemFilterException;
import de.hpi.sam.rubis.filter.ItemFilterMonitor;
import de.hpi.sam.rubis.filter.MonitoredPropertyNotification;

/**
 * Implementation of an {@link ItemFilter} that filters items of sales finishing
 * too early to make a bid.
 * 
 * @author thomas vogel
 * 
 */
@Stateless(mappedName = ItemFilter.LAST_SECOND_SALES_ITEM_FILTER_MAPPED_NAME)
public class LastSecondSaleItemFilterBean extends AbstractFilterBean implements
		ItemFilter {

	/**
	 * The next filter to invoke.
	 */
	@EJB(mappedName = ItemFilter.PAST_SALES_ITEM_FILTER_MAPPED_NAME)
	private ItemFilter nextFilter;

	/**
	 * Application-specific monitoring of this filter.
	 */
	private static ItemFilterMonitor monitor = new ItemFilterMonitor(
			LastSecondSaleItemFilterBean.class.getCanonicalName());

	/**
	 * +/- threshold for altering changes of the filter's selection rate.
	 */
	@Resource
	private double selectionRateThreshold;

	/**
	 * +/- threshold for altering changes of the filter's computation time.
	 */
	@Resource
	private double computationTimeThreshold;

	/**
	 * CPU time (in the sense of load) imposed on the filter bean in ms.
	 */
	@Resource
	private long cpuTime;

	/**
	 * If an item is sold for less than "last seconds" before the sale ends, the
	 * item is filtered.
	 */
	@Resource
	private int lastSeconds;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Item> filter(List<Item> items, User user)
			throws ItemFilterException {

		// Monitoring START =========================================
		int initialItemListSize = items.size();
		long startComputationTime = System.currentTimeMillis();
		// Monitoring END ===========================================

		// look up last seconds =====================================
		try {
			InitialContext ctx = new InitialContext();
			this.lastSeconds = (Integer) ctx
					.lookup("java:comp/env/lastSeconds");
		} catch (NamingException e) {
			e.printStackTrace();
			this.lastSeconds = 360;
		}
		// end of look up last seconds ====================================

		long lastMillies = this.lastSeconds * 1000;
		long currentTimeMillis = System.currentTimeMillis();
		Date currentTime = new Date(currentTimeMillis);
		Date deadline = new Date(currentTimeMillis + lastMillies);

		Iterator<Item> iterator = items.iterator();
		while (iterator.hasNext()) {
			Item next = iterator.next();
			Date endDate = next.getEndDate();
			if (endDate.after(currentTime) && endDate.before(deadline)) {
				// sales is still running, but it ends before the deadline
				iterator.remove();
			}
		}

		// cause synthetic load =====================================
		try {
			InitialContext ctx = new InitialContext();
			this.cpuTime = (Long) ctx.lookup("java:comp/env/cpuTime");
			super.useCPU(this.cpuTime);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		// end of synthetic load ====================================

		// MONITORING START =========================================
		try {
			InitialContext ctx = new InitialContext();
			this.selectionRateThreshold = (Double) ctx
					.lookup("java:comp/env/selectionRateThreshold");
			this.computationTimeThreshold = (Double) ctx
					.lookup("java:comp/env/computationTimeThreshold");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		int processedItemListSize = items.size();
		long endComputationTime = System.currentTimeMillis();
		List<MonitoredPropertyNotification> msgs = monitor.addData(
				initialItemListSize, processedItemListSize,
				this.selectionRateThreshold, startComputationTime,
				endComputationTime, this.computationTimeThreshold);
		super.notifyChange(msgs);
		// MONITORING END ===========================================

		// invoke next filter
		if (this.nextFilter != null) {
			return this.nextFilter.filter(items, user);
		} else {
			return items;
		}
	}

	@PostConstruct
	public void init() {
		super.setFilterId(LastSecondSaleItemFilterBean.class.getCanonicalName());
		super.initConnectionToQueue();
	}
}
