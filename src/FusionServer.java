import clustermonitor.PerformanceMetrics;
import clustermonitor.PhysicalHandle;

/**
 * This class represents a server running on VMWare Fusion.
 * 
 * @author Walter
 * 
 */
public class FusionServer implements PhysicalHandle {

	private  String _serverName, _clusterName;
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void stopServer() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean enableServer() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void disableServer() {
		// TODO Auto-generated method stub

	}

	@Override
	public void getPerformanceMetrics(PerformanceMetrics performanceMetrics) {
		// TODO Auto-generated method stub
		//System.out.println("Getting performance metrics for server: " + _clusterName + "." + _serverName);
		
		temp += 1;
		
		if (temp < 20) {
		
			performanceMetrics.setMetricValue("load", 2);
		}
		if (temp > 20 && temp < 120) {
			performanceMetrics.setMetricValue("load", 0.4);
			
		}
		else {
			performanceMetrics.setMetricValue("load", 2);
		}
	}

}
