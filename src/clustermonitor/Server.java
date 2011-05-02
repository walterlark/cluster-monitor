package clustermonitor;

/**
 * Represents a server in the system. Among other things, this class stores a
 * reference to the {@link PhysicalHandle} so that ClusterMonitor can take
 * actions on the physical machine that this class represents.
 * 
 * @author Walter
 * 
 */
public class Server implements PhysicalHandle {

	/**
	 * String name of this server.
	 */
	String _serverName;

	/**
	 * Cluster to which this server belongs.
	 */
	Cluster _cluster;

	/**
	 * Store the {@link PhysicalHandle} so that we have an easy way to request
	 * the client take certain actions.
	 */
	PhysicalHandle _handle;

	/**
	 * Create a new {@link Server}. Pass in whether or not the server is already
	 * running.
	 * 
	 * @param running
	 */
	Server(Cluster cluster, PhysicalHandle handle) {
		_cluster = cluster;
		_serverName = handle.getServerName();
		_handle = handle;
		System.out.println(this);
	}
	
	boolean isAvailable() {
		return !isRunning();
	}
	
	boolean isActive() {
		return (isRunning() && isEnabled());
	}
	
	boolean isDisabled() {
		return !isEnabled();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((_cluster == null) ? 0 : _cluster.hashCode());
		result = prime * result
				+ ((_serverName == null) ? 0 : _serverName.hashCode());
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
		Server other = (Server) obj;
		if (_cluster == null) {
			if (other._cluster != null)
				return false;
		} else if (!_cluster.equals(other._cluster))
			return false;
		if (_serverName == null) {
			if (other._serverName != null)
				return false;
		} else if (!_serverName.equals(other._serverName))
			return false;
		return true;
	}

	@Override
	public void getPerformanceMetrics(PerformanceMetrics performanceMetrics) {
		// TODO Auto-generated method stub
		_handle.getPerformanceMetrics(performanceMetrics);
	}

	@Override
	public String getServerName() {
		return _serverName;
	}

	@Override
	public String getClusterName() {
		return _cluster.getName();
	}

	@Override
	public boolean startServer() {
		if (_handle.startServer()) {
			return true;
		}
		return false;
	}

	@Override
	public void stopServer() {
		_handle.stopServer();
	}

	@Override
	public boolean enableServer() {
		
		// ensure server is running
		if (!_handle.startServer()) {
			return false;
		}
		
		// ensure server is enabled
		if (_handle.enableServer()) {
			return true;
		}
		return false;
	}

	@Override
	public void disableServer() {
		_handle.disableServer();
	}
	
	@Override
	public String toString() {
		return _cluster.getName() + "." + getServerName() + " is " + (isRunning() ? "" : "not ") + "running and is " + (isEnabled() ? "" : "not ") + "enabled.";
	}

	@Override
	public boolean isRunning() {
		return _handle.isRunning();
	}

	@Override
	public boolean isEnabled() {
		return _handle.isEnabled();
	}

}
