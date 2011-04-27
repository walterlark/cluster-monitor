package clustermonitor;

import java.util.ArrayList;

public class Cluster implements Monitorable {

	private ArrayList<Server> _servers;
	private String _clusterName;

	Cluster(String clusterName) {

		_clusterName = clusterName;

		// instantiate servers
		_servers = new ArrayList<Server>();
	}

	void addServer(Server server) {

		if (!_servers.contains(server)) {
			_servers.add(server);
		}

	}

	String getName() {
		return this._clusterName;
	}
	
	int getServerCount() {
		return _servers.size();
	}
	
	int getActiveServerCount() {
		int count = 0;
		for (Server s : _servers) {
			if (s.isActive()) {
				count ++;
			}
		}
		return count;
	}
	
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((_clusterName == null) ? 0 : _clusterName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cluster other = (Cluster) obj;
		if (_clusterName == null) {
			if (other._clusterName != null)
				return false;
		} else if (!_clusterName.equals(other._clusterName))
			return false;
		return true;
	}

	@Override
	public void getPerformanceMetrics(PerformanceMetrics performanceMetrics) {
		
		// get performance metrics for all servers in cluster
		for (Server s : _servers) {
			
			// TODO: average the metrics coming back into one cluster-wide data set
			s.getPerformanceMetrics(performanceMetrics);
		}
		
	}
}
