package de.hpi.sam.rubis.filter.impl;

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
 * Implementation of an {@link ItemFilter} that filters items that are only sold
 * by buy-nows.
 * 
 * @author thomas vogel
 * 
 */
@Stateless(mappedName = ItemFilter.BUY_NOW_ITEM_FILTER_MAPPED_NAME)
public class BuyNowItemsFilterBean extends AbstractFilterBean implements
		ItemFilter {

	/**
	 * The next filter to invoke.
	 */
	@EJB(mappedName = ItemFilter.AVAILABILITY_ITEM_FILTER_MAPPED_NAME)
	private ItemFilter nextFilter;

	/**
	 * Application-specific monitoring of this filter.
	 */
	private static ItemFilterMonitor monitor = new ItemFilterMonitor(
			BuyNowItemsFilterBean.class.getCanonicalName());

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
	 * {@inheritDoc}
	 */
	@Override
	public List<Item> filter(List<Item> items, User user)
			throws ItemFilterException {

		// Monitoring START =========================================
		int initialItemListSize = items.size();
		long startComputationTime = System.currentTimeMillis();
		// Monitoring END ===========================================

		Iterator<Item> iterator = items.iterator();
		while (iterator.hasNext()) {
			Item next = iterator.next();
			if (next.isOnlyBuyNow()) {
				// item is only sold by buy-nows
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
		super.setFilterId(BuyNowItemsFilterBean.class.getCanonicalName());
		super.initConnectionToQueue();
	}
}
