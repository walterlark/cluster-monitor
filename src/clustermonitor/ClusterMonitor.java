package clustermonitor;

import java.util.HashMap;

/**
 * Despite the lack of a plural name, this class will manage as many clusters of
 * computers as is desired. When adding servers, both the cluster name and
 * server name must be specified; these values will be used to monitor the
 * clusters individually.
 * 
 * @author Walter
 * 
 */
public class ClusterMonitor {

	/**
	 * Hold the clusters active in the system.
	 */
	private HashMap<String, Cluster> _clusters;

	public ClusterMonitor() {
		_clusters = new HashMap<String, Cluster>();
	}

	/**
	 * Start the manager.
	 */
	public void startManager() {
		System.out.println("Starting the manager.");
	}

	/**
	 * Stop the manager.
	 */
	public void stopManager() {
		System.out.println("Stopping the manager.");
	}

	/**
	 * Add this server handle to the manager.
	 * 
	 * @param handle
	 * @param isRunning
	 */
	public void addServer(PhysicalHandle handle, boolean isRunning) {

		// make sure the cluster is instantiated
		Cluster cluster = this.addCluster(handle.getClusterName());
		Server server = new Server(cluster, handle, isRunning);
		cluster.addServer(server);

	}

	private Cluster addCluster(String clusterName) {
		if (!_clusters.containsKey(clusterName)) {
			return _clusters.put(clusterName, new Cluster(clusterName));
		} else {
			return _clusters.get(clusterName);
		}
	}

}