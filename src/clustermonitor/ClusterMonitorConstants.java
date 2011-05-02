package clustermonitor;

public interface ClusterMonitorConstants {

	/**
	 * Interval at which ClusterMonitor will poll for cluster/server performance
	 * metrics.
	 */
	public static final long MONITOR_INTERVAL = 10 * 1000;
	
	/**
	 * Time in milliseconds between two successive actions on this cluster.
	 */
	public static final long ADJUSTMENT_TIME = 60 * 1000;

}
