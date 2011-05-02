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
	 * Whether or not this server is currently running.
	 */
	boolean _isRunning;

	/**
	 * Whether or not the server is currently accepting new requests.
	 */
	boolean _isEnabled;

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
	Server(Cluster cluster, PhysicalHandle handle, boolean isRunning) {
		_cluster = cluster;
		_serverName = handle.getServerName();
		_isRunning = isRunning;
		_isEnabled = true; // TODO: maybe assume that this is not always the
							// case
		_handle = handle;
	}
	
	boolean isAvailable() {
		return !_isRunning;
	}
	
	boolean isActive() {
		return (_isRunning && _isEnabled);
	}
	
	boolean isDisabled() {
		return !_isEnabled;
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
			_isRunning = true;
			_isEnabled = true;
			return true;
		}
		return false;
	}

	@Override
	public void stopServer() {
		_handle.stopServer();
		_isRunning = false;
		_isEnabled = false;
	}

	@Override
	public boolean enableServer() {
		if (_handle.enableServer()) {
			_isRunning = true;
			_isEnabled = true;
			return true;
		}
		return false;
	}

	@Override
	public void disableServer() {
		_handle.disableServer();
		_isRunning = true;
		_isEnabled = false;
	}
	
	@Override
	public String toString() {
		return getServerName() + " is " + (_isRunning ? "" : "not ") + "running and is " + (_isEnabled ? "" : "not ") + "enabled.";
	}

}
