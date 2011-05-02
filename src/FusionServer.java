import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import clustermonitor.PerformanceMetrics;
import clustermonitor.PhysicalHandle;

/**
 * This class represents a server running on VMWare Fusion.
 * 
 * @author Walter
 * 
 */
public class FusionServer implements PhysicalHandle {

	private String _serverName, _clusterName;
	private long temp;

	public FusionServer(String serverName, String clusterName) {
		_serverName = serverName;
		_clusterName = clusterName;
		temp = 0;
	}

	@Override
	public String getServerName() {
		return _serverName;
	}

	@Override
	public String getClusterName() {
		return _clusterName;
	}

	@Override
	public boolean startServer() {
		int rv = 1;
		try {
			System.out.println("Starting server!!!!!");
			Process p = Runtime.getRuntime().exec(
					"scripts/start_server.sh " + _serverName);
			rv = p.waitFor();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return (rv == 0);
	}

	@Override
	public void stopServer() {
		try {
			System.out.println("Stopping server!!!!!");
			Process p = Runtime.getRuntime().exec(
					"scripts/stop_server.sh " + _serverName);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public boolean enableServer() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void disableServer() {
		// TODO Auto-generated method stub

	}

	@Override
	public void getPerformanceMetrics(PerformanceMetrics performanceMetrics) {
		// TODO Auto-generated method stub
		// System.out.println("Getting performance metrics for server: " +
		// _clusterName + "." + _serverName);

		temp += 1;

		if (temp < 20) {

			performanceMetrics.setMetricValue("load", 2);
		}
		if (temp > 20 && temp < 60) {
			performanceMetrics.setMetricValue("load", 0.4);

		} else {
			performanceMetrics.setMetricValue("load", 2);
		}
	}

}
