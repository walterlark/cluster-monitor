package clustermonitor;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

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
	private AtomicBoolean _running;
	private Object _runLock;

	public ClusterMonitor() {
		_clusters = new HashMap<String, Cluster>();
		_running = new AtomicBoolean(false);
		_runLock = new Object();
	}

	/**
	 * Start the manager. This function will block forever. Call
	 * stopManager to stop it from executing.
	 */
	public void startManager() {

		System.out.println("Starting the manager.");

		// ensure stop manager wont return until it grabs this lock
		synchronized(_runLock) {

			_running.set(true);

			while (_running.get()) {

				System.out.println("Checking on all clusters...");
				
				for (Cluster c : _clusters.values()) {
					PerformanceMetrics pm = new PerformanceMetrics("load-1", "free-memory");
					c.getPerformanceMetrics(pm);
					System.out.println(pm);
				}
				
				try {
					Thread.sleep(ClusterMonitorConstants.MONITOR_INTERVAL);
				} catch (InterruptedException e) {
					// not a huge deal...as long as it rarely happens
				}
			}

		}
	}

	/**
	 * This function will stop the manager, blocking until it
	 * is sure that it has finished running.
	 */
	public void stopManager() {

		System.out.println("Stopping manager...");
		
		_running.set(false);
		
		// ensure we don't return until finished running
		synchronized(_runLock) {
			return;
		}
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
			Cluster c = new Cluster(clusterName);
			_clusters.put(clusterName, c);
			return c;
		} else {
			return _clusters.get(clusterName);
		}
	}

}