package de.hpi.sam.rubis.client.main;

/**
 * Creates some load on the system using the client threads (see
 * {@link ClientRunnable}).
 * 
 * @author thomas vogel
 *
 */
public class LoadGenerator {

	public LoadGenerator() {
	}

	public static void main(String[] args) {

		int numberOfClients = 1;
		int numberOfRequestPerClient = 10;

		ClientRunnable[] clients = new ClientRunnable[numberOfClients];
		Thread[] threads = new Thread[numberOfClients];

		for (int i = 0; i < clients.length; i++) {
			clients[i] = new ClientRunnable(i, numberOfRequestPerClient);
			threads[i] = new Thread(clients[i]);
		}

		for (int i = 0; i < threads.length; i++) {
			threads[i].start();
		}

		for (int i = 0; i < threads.length; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}
