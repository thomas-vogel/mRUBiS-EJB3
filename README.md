# mRUBiS-EJB3

_mRUBiS-EJB3_ is a real prototype of _mRUBiS_ implemented for the EJB application server GlassFish. In contrast, the [_mRUBiS exemplar simulates mRUBiS_](https://github.com/thomas-vogel/mRUBiS) to provide a light-weight environment for experiments with self-adaptation solutions.

The _mRUBiS-EJB3_ is configured to run one shop (one-tenant system), however, it can be extended to a multi-tenant setting with multiple shops since each module (component) can be deployed multiple times. In this case, the configuration must be adjusted to that two deployed modules have different names (so-called mapped names of components used for and by the naming service of the application server).  

## Technical Details
_mRUBiS-EJB3_ has been developed with Enterprise Java Beans 3 (EJB 3.0) technology as part of Java EE 5 for the GlassFish Application Server 2.1.1. The server is available for download [here](http://www.oracle.com/technetwork/java/javaee/downloads/java-archive-downloads-glassfish-419424.html).

GlassFish v2.1.1 requires Java 6. Hence, a Java 6 distribution must be used to run GlassFish and _mRUBiS-EJB3_. Therefore, the system property JAVA_HOME must be set to a Java 6 distribution. This is checked by the ant script delivered with this project.

Instructions for building, deploying, and running  are given below. Moreover, mRUBiS-EJB3 is also available as a [Docker image](https://github.com/jfloff/docker-mrubis) created by @jfloff.

## Building, Deploying, and Running mRUBiS-EJB3

_mRUBiS-EJB3_ is available as an Eclipse project. The structure of the project is as following:

Each component of _mRUBiS-EJB3_ has its individual folder with subfolders such as `res` and `src`. The folders `res` contain resources, especially configuration files such as deployment descriptors. The folders `src` contain Java source code. An architectural view of _mRUBiS-EJB3_ is provided by diagrams located in the folder `doc`.

```
├── authservice		// Authentication Service
│   ├── res
│   └── src
├── availabilityitemfilter	// Availability Item Filter
│   ├── res
│   └── src
├── bidandbuyservice	// Bid and Buy Service
│   ├── res
│   └── src
├── businessobjects		// Objects used by all component interfaces
│   ├── res
│   └── src
├── buynowitemfilter	// Buy-Now Item Filter
│   ├── res
│   └── src
├── categoryitemfilter	// Category Item Filter
│   ├── res
│   └── src
├── client			// Sample Client Application
│   └── src
├── commentitemfilter	// Comment Item Filter
│   ├── res
│   └── src
├── contracts		// All component interfaces
│   ├── res
│   └── src
├── database		// SQL code for the database and test data
├── doc				// Documentation
│   └── UML
├── entities		// Database entities
│   ├── res
│   └── src
├── futuresalesitemfilter	// Future Sales Item Filter
│   ├── res
│   └── src
├── inventorymgmt	// Inventory Service
│   ├── res
│   └── src
├── itemmgmt		// Item Management Service
│   ├── res
│   └── src
├── lastsecondsalesitemfilter	// Last Seconds Sales Item Filter
│   ├── res
│   └── src
├── pastsalesitemfilter		// Past Sales Item Filter
│   ├── res
│   └── src
├── persistenceservice		// Persistence Service
│   ├── res
│   └── src
├── queryservice			// Query Service
│   ├── res
│   └── src
├── recommendationitemfilter	// Recommendation Item Filter
│   ├── res
│   └── src
├── regionitemfilter		// Region Item Filter
│   ├── res
│   └── src
├── reputationservice		// Reputation Service
│   ├── res
│   └── src
├── sellerreputationitemfilter	// Seller Reputation Item Filter
│   ├── res
│   └── src
├── usermgmt	// User Management Service
│   ├── res
│   └── src
├── build.properties	// ANT properties
├── build.xml		// ANT script
├── LICENSE			// License file
└── README.md		// This file

```

To build and deploy _mRUBiS-EJB3_, a script for Apache Ant---tested under Linux---is provided (`build.xml`). To use this script, a property of the script has to be adjusted in the properties file (`build.properties`). Set the `glassfish.home`property to the fully qualified name of the `glassfish` folder where the application server has been installed, for instance:

	`glassfish.home=/home/username/server/glassfish`

When using the Ant script, this property is needed to resolve dependencies to libraries of the Java Enterprise Edition that are contained in the folder `${glassfish.home}/lib`.

Moreover, to enable Eclipse to compile and test the _mRUBiS-EJB3_ project, you have to manually adjust the libraries of the project's Java Build Path, such that Eclipse finds the two libraries `javaee.jar` and `appserv-rt.jar` located in the `${glassfish.home}/lib` folder.

Having set the property and resolved the dependencies to libraries, the Ant script can be used (either from the Terminal or within Eclipse).

In the following, the relevant commands are outlined when using the Terminal. Navigate to the _mRUBiS-EJB3_ project folder that directly contains the `build.xml` file to execute any of the following commands.


- `ant start-server`
  Starts the GlassFish application server
	(including the database server as part of GlassFish)

- `ant stop-server`
	Stops the GlassFish application server
	(including the database server as part of GlassFish)

- `ant setup-database`
	Creates the _mRUBiS-EJB3_ database and its schema, and inserts test data into the database

- `ant reset-database`
	Resets the data in the database to its original state
	(probably needed between multiple tests)

- `ant cleanup-database`
	Destroys the database (and thus all data contained in the database)

- `ant build`
	Compiles and packages _mRUBiS-EJB3_ to EJB modules that are located in the
	subfolder `dist` and that can be deployed to GlassFish

- `ant clean`
	Cleans up the artifacts created by `ant build`

- `ant deploy`
	Deploys all EJB modules of _mRUBiS-EJB3_ to GlassFish

- `ant undeploy`
	Undeploys all EJB modules of _mRUBiS-EJB3_ from GlassFish

EJB modules can also be manually (un)deployed to GlassFish through the Administration Console (see `http://localhost:4848` if GlassFish runs on localhost)

Having deployed _mRUBiS-EJB3_, the marketplace is now running.

To test the _mRUBiS-EJB3_ installation, a test client is provided as part of the _mRUBiS-EJB3_ project. In the project's subfolder `client/src` the main class `de.hpi.sam.rubis.client.main.ClientSession` can be executed within Eclipse to send request to the _mRUBiS-EJB3_ application deployed in GlassFish.
