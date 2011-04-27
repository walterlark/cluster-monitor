package clustermonitor;

import java.util.ArrayList;

public class Cluster {

	private ArrayList<Server> _servers;
	private String _clusterName;

	public Cluster(String clusterName) {

		_clusterName = clusterName;

		// instantiate servers
		_servers = new ArrayList<Server>();
	}

	public void addServer(Server server) {

		if (!_servers.contains(server)) {
			_servers.add(server);
		}

	}

	public String getName() {
		return this._clusterName;
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
}
