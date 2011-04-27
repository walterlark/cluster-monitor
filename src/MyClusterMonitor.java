import clustermonitor.ClusterMonitor;

public class MyClusterMonitor {

	public static final String APACHE_CLUSTER = "apache";
	
	public static void main(String[] args) {

		// create a new cluster monitor
		ClusterMonitor monitor = new ClusterMonitor();
		
		// create potential servers
		FusionServer apache0 = new FusionServer("apache0", APACHE_CLUSTER);
		FusionServer apache1 = new FusionServer("apache1", APACHE_CLUSTER);
		
		// add servers to monitor
		monitor.addServer(apache0, true);
		monitor.addServer(apache1, false);
		
		// finally, start the monitor
		monitor.startManager();

	}

}
