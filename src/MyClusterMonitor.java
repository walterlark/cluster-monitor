import clustermonitor.ClusterMonitor;
import clustermonitor.PerformanceMetrics;
import clustermonitor.Rule.Action;
import clustermonitor.Rule.Comparison;

public class MyClusterMonitor {

	public static final String APACHE_CLUSTER = "apache";
	
	public static void main(String[] args) {

		// create a new cluster monitor
		ClusterMonitor monitor = new ClusterMonitor();
	
		// create available servers
		FusionServer apache0 = new FusionServer("apache0", APACHE_CLUSTER);
		FusionServer apache1 = new FusionServer("apache1", APACHE_CLUSTER);
		
		// add servers to monitor
		monitor.addServer(apache0);
		monitor.addServer(apache1);
		
		// set available metrics for each cluster
		monitor.setAvailableMetricsForCluster(APACHE_CLUSTER, new PerformanceMetrics("load", "free-memory"));

		// set rules
		monitor.addRuleForCluster(APACHE_CLUSTER, "load", Comparison.GREATER_THAN, 1.25, 15*1000, Action.ADD_SERVER);
		monitor.addRuleForCluster(APACHE_CLUSTER, "load", Comparison.LESS_THAN, 0.5, 15*1000, Action.REMOVE_SERVER);
		
		// finally, start the monitor
		monitor.startMonitor();

	}

}
