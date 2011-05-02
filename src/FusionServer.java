import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

			if (rv != 0) {
				System.err.println("Unable to start server!");
			} else {
				System.out.println("Server started successfully.");
			}

		} catch (IOException e) {
			e.printStackTrace();
			rv = 1;
		} catch (InterruptedException e) {
			e.printStackTrace();
			rv = 1;
		}

		return (rv == 0);
	}

	@Override
	public void stopServer() {
		try {
			System.out.println("Stopping server!!!!!");
			Process p = Runtime.getRuntime().exec(
					"scripts/stop_server.sh " + _serverName);
			int rv = p.waitFor();

			if (rv != 0) {
				System.err.println("Unable to stop server!");
			} else {
				System.out.println("Server stopped successfully.");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean enableServer() {
		int rv = 1;
		try {
			System.out.println("Enabling server!!!!!");
			Process p = Runtime.getRuntime().exec(
					"scripts/enable_server.sh " + _serverName);
			rv = p.waitFor();

			if (rv != 0) {
				System.err.println("Unable to enable server!");
			} else {
				System.out.println("Server enabled successfully.");
			}

		} catch (IOException e) {
			e.printStackTrace();
			rv = 1;
		} catch (InterruptedException e) {
			e.printStackTrace();
			rv = 1;
		}

		return (rv == 0);
	}

	@Override
	public void disableServer() {

		try {
			System.out.println("Disabling server!!!!!");
			Process p = Runtime.getRuntime().exec(
					"scripts/disable_server.sh " + _serverName);
			int rv = p.waitFor();

			if (rv != 0) {
				System.err.println("Unable to disable server!");
			} else {
				System.out.println("Server disabled successfully.");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

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

	@Override
	public boolean isRunning() {

		try {
			Process p = Runtime.getRuntime().exec(
					"scripts/server_status.py " + _serverName);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line = br.readLine();
			if (line == null) {
				System.err.println("Unable to obtain status of server.");
				return false;
			} else {
				if (line.split(",")[0].compareToIgnoreCase("yes") == 0) {
					return true;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean isEnabled() {
		try {
			Process p = Runtime.getRuntime().exec(
					"scripts/server_status.py " + _serverName);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line = br.readLine();
			if (line == null) {
				System.err.println("Unable to obtain status of server.");
				return false;
			} else {
				if (line.split(",")[1].compareToIgnoreCase("yes") == 0) {
					return true;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

}
