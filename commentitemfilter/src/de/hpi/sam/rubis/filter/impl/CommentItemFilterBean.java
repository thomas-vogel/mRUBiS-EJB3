package de.hpi.sam.rubis.filter.impl;

import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import de.hpi.sam.rubis.entity.Comment;
import de.hpi.sam.rubis.entity.Item;
import de.hpi.sam.rubis.entity.User;
import de.hpi.sam.rubis.filter.AbstractFilterBean;
import de.hpi.sam.rubis.filter.ItemFilter;
import de.hpi.sam.rubis.filter.ItemFilterException;
import de.hpi.sam.rubis.filter.ItemFilterMonitor;
import de.hpi.sam.rubis.filter.MonitoredPropertyNotification;

/**
 * Implementation of an {@link ItemFilter} that filters items based on the
 * comments for individual items. Items are rated on a scale from 0 to 10 with
 * 10 being best.
 * 
 * @author thomas vogel
 * 
 */
@Stateless(mappedName = ItemFilter.COMMENT_ITEM_FILTER_MAPPED_NAME)
public class CommentItemFilterBean extends AbstractFilterBean implements
		ItemFilter {

	/**
	 * The next filter to invoke.
	 */
	@EJB(mappedName = ItemFilter.CATEGORY_ITEM_FILTER_MAPPED_NAME)
	private ItemFilter nextFilter;

	/**
	 * Application-specific monitoring of this filter.
	 */
	private static ItemFilterMonitor monitor = new ItemFilterMonitor(
			CommentItemFilterBean.class.getCanonicalName());

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
	 * Items with at least one comment giving a rating below
	 * <code>ratingThreshold</code> are filtered.
	 */
	@Resource
	private int ratingThreshold;

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

		// lookup of rating threshold
		try {
			InitialContext ctx = new InitialContext();
			this.ratingThreshold = (Integer) ctx
					.lookup("java:comp/env/ratingThreshold");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		// end of lookup of rating threshold

		Iterator<Item> iterator = items.iterator();
		while (iterator.hasNext()) {
			Item next = iterator.next();
			boolean lowRating = false;
			for (Comment comment : next.getComments()) {
				if (comment.getRating() < this.ratingThreshold) {
					lowRating = true;
				}
			}
			if (lowRating) {
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
		super.setFilterId(CommentItemFilterBean.class.getCanonicalName());
		super.initConnectionToQueue();
	}
}
