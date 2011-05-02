package clustermonitor;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import clustermonitor.Rule.Action;
import clustermonitor.Rule.Comparison;

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

	/**
	 * Time in milliseconds between two successive actions on this cluster.
	 */
	public static final long ADJUSTMENT_TIME = 30 * 1000;

	public ClusterMonitor() {
		_clusters = new HashMap<String, Cluster>();
		_running = new AtomicBoolean(false);
		_runLock = new Object();
	}

	/**
	 * Start the monitor. This function will block until stopMonitor is called.
	 */
	public void startMonitor() {

		System.out.println("Starting the monitor.");

		// ensure stop manager wont return until it grabs this lock
		synchronized (_runLock) {

			_running.set(true);

			while (_running.get()) {

				// System.out.println("Checking on all clusters...");

				for (Cluster c : _clusters.values()) {
					c.collectMetricsAndEvaluateRules();
				}

				try {
					Thread.sleep(ClusterMonitorConstants.MONITOR_INTERVAL);
				} catch (InterruptedException e) {
					// not a huge deal...as long as it rarely/never happens
				}
			}

		}
	}

	/**
	 * This function will stop the manager, blocking until it is sure that it
	 * has finished running.
	 */
	public void stopMonitor() {

		System.out.println("Stopping monitor...");

		_running.set(false);

		// ensure we don't return until finished running
		synchronized (_runLock) {
			return;
		}
	}

	/**
	 * Set the available metrics for this cluster.
	 * 
	 * @param clusterName
	 * @param pm
	 */
	public void setAvailableMetricsForCluster(String clusterName,
			PerformanceMetrics pm) {
		if (_clusters.containsKey(clusterName)) {
			_clusters.get(clusterName).setAvailableMetrics(pm);
		}

	}

	/**
	 * Add a rule for the given cluster.
	 * 
	 * Example:
	 * 
	 * addRuleForCluster("cluster", "load", GREATER_THAN, 1.25, 15*1000,
	 * ADD_SERVER) will add a new server once the load has been greater than
	 * 1.25 for 15 seconds.
	 * 
	 * @param clusterName
	 *            - name of the cluster for which this rule applies
	 * @param metric
	 *            - the metric associated with this rule
	 * @param comp
	 *            - the comparison operator to be used
	 * @param value
	 *            - the value to compare against
	 * @param duration
	 *            - the number of milliseconds for which this rule must be true
	 * @param adjustmentTime
	 *            TODO
	 * @param action
	 *            - the action to take when this rule evaluates to true
	 */
	public void addRuleForCluster(String clusterName, String metric,
			Comparison comp, double value, long duration, Action action) {

		// verify this cluster exists
		if (_clusters.containsKey(clusterName)) {
			_clusters.get(clusterName).addRule(metric, comp, value, duration,
					action);
		}
	}

	/**
	 * Add this server handle to the manager.
	 * 
	 * @param handle
	 * @param isRunning
	 */
	public void addServer(PhysicalHandle handle) {

		// make sure the cluster is instantiated
		Cluster cluster = this.addCluster(handle.getClusterName());
		Server server = new Server(cluster, handle);
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