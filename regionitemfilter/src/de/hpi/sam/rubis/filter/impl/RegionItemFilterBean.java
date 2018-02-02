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
import de.hpi.sam.rubis.entity.Region;
import de.hpi.sam.rubis.entity.User;
import de.hpi.sam.rubis.filter.AbstractFilterBean;
import de.hpi.sam.rubis.filter.ItemFilter;
import de.hpi.sam.rubis.filter.ItemFilterException;
import de.hpi.sam.rubis.filter.ItemFilterMonitor;
import de.hpi.sam.rubis.filter.MonitoredPropertyNotification;

/**
 * Implementation of an {@link ItemFilter} that filters items based on the
 * region, in which they are sold.
 * 
 * @author thomas vogel
 * 
 */
@Stateless(mappedName = ItemFilter.REGION_ITEM_FILTER_MAPPED_NAME)
public class RegionItemFilterBean extends AbstractFilterBean implements
		ItemFilter {

	/**
	 * The next filter to invoke.
	 */
	@EJB(mappedName = ItemFilter.SELLER_REPUTATION_ITEM_FILTER_MAPPED_NAME)
	private ItemFilter nextFilter;

	/**
	 * Application-specific monitoring of this filter.
	 */
	private static ItemFilterMonitor monitor = new ItemFilterMonitor(
			RegionItemFilterBean.class.getCanonicalName());

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

			// let's implement it less efficient
			boolean isUserFromEurope = this.isEurope(user.getRegion());
			if (isUserFromEurope) {
				boolean isItemFromAmerica = this.isAmerica(next.getSeller()
						.getRegion());
				if (isItemFromAmerica) {
					// user (buyer) is from Europe, and item is sold in America
					iterator.remove();
				}
			}

			boolean isUserFromAmerica = this.isAmerica(user.getRegion());
			if (isUserFromAmerica) {
				boolean isItemFromEurope = this.isEurope(next.getSeller()
						.getRegion());
				if (isItemFromEurope) {
					// user (buyer) is from America, and item is sold in Europe
					iterator.remove();
				}
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

	private boolean isEurope(Region region) {
		String name = region.getName();
		return name.equals("Austria") || name.equals("Belgium")
				|| name.equals("Cyprus") || name.equals("Estonia")
				|| name.equals("Finland") || name.equals("France")
				|| name.equals("Germany") || name.equals("Greece")
				|| name.equals("Ireland") || name.equals("Italy")
				|| name.equals("Latvia") || name.equals("Luxembourg")
				|| name.equals("Malta") || name.equals("Netherlands")
				|| name.equals("Portugal") || name.equals("Slovakia")
				|| name.equals("Slovenia") || name.equals("Spain");
	}

	private boolean isAmerica(Region region) {
		String name = region.getName();
		return name.equals("USA") || name.equals("Canada");
	}

	@PostConstruct
	public void init() {
		super.setFilterId(RegionItemFilterBean.class.getCanonicalName());
		super.initConnectionToQueue();
	}
}
