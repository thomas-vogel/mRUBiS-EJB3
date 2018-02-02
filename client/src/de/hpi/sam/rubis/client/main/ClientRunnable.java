package de.hpi.sam.rubis.client.main;

import java.util.List;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;

import de.hpi.sam.rubis.entity.Item;
import de.hpi.sam.rubis.itemmgmt.BrowseCategoriesService;

/**
 * A client thread.
 * 
 * @author thomas vogel
 *
 */
public class ClientRunnable implements Runnable {

	private static Logger logger = Logger.getLogger("de.hpi.sam.rubis.client.main");

	private long id;
	private int numberOfRequests;

	public ClientRunnable(long id, int numberOfRequests) {
		this.id = id;
		this.numberOfRequests = numberOfRequests;
	}

	@Override
	public void run() {
		try {
			Context ctx = new InitialContext();
			BrowseCategoriesService browseCategoriesService = (BrowseCategoriesService) ctx.lookup(
					BrowseCategoriesService.MAPPED_NAME + "#" + BrowseCategoriesService.class.getCanonicalName());

			for (int i = 0; i < this.numberOfRequests; i++) {

				List<Item> result = browseCategoriesService.getPersonalizedItems("howard", "XDRF56FSFFW");

				logger.info("Thread " + this.id + " has retrieved " + result.size() + " items.");
			}

		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
