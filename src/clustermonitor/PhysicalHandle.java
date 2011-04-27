package clustermonitor;

/**
 * Allows the {@link ClusterMonitor} to be platform independent by requiring the
 * implementing application to handle interactions with the servers / server
 * provider.
 * 
 * @author Walter
 * 
 */
public interface PhysicalHandle {

	/**
	 * Get a string representation of the server name.
	 * 
	 * @return
	 */
	public String getServerName();

	/**
	 * Get a string representation of the name of the cluster to which this
	 * server belongs.
	 * 
	 * @return
	 */
	public String getClusterName();

	/**
	 * Value used when no load can be obtained.
	 */
	public static final double NO_LOAD = -1.0;

	/**
	 * Start the server up. This method may (and probably should) block.
	 * 
	 * @return return true once the server has booted, or if the server is
	 *         already online. return false if the server cannot be booted at
	 *         this time.
	 * 
	 */
	public boolean startServer();

	/**
	 * Stop the server. Use of this server will cease regardless, so it is left
	 * up the implementing application to ensure that the server does indeed
	 * shutdown.
	 * 
	 * If server is already stopped, simply ignore request.
	 * 
	 */
	public void stopServer();

	/**
	 * Tell the load balancer to send requests to this server. If server is not
	 * on, it is acceptable for this function to simply return false. This
	 * method can (and probably should) block.
	 * 
	 * @return true once the server is able to accept requests, or if it already
	 *         can at time of calling. false if this is not possible at this
	 *         time.
	 */
	public boolean enableServer();

	/**
	 * Tell the load balancer to stop sending requests to this server. It is not
	 * important to {@link ClusterMonitor} whether or not this action succeeds,
	 * and as such, it is left up to the implementing application to ensure that
	 * use of this server stops.
	 * 
	 * If server is already disabled, simply ignore this request.
	 * 
	 */
	public void disableServer();

	/**
	 * Return the current load for this server, or NO_LOAD if the load cannot be
	 * acquired at this time. Though {@link ClusterMonitor} will be smoothing
	 * and averaging of load internally, it is probably best to not use
	 * instantaneous load, but rather the 1 minute average value returned by the
	 * standard program "top."
	 * 
	 * @return the load of the server normalized by the number of CPUs in the
	 *         machine (so 1 should be max sustainable load, even on a 4 CPU
	 *         machine) or NO_LOAD if load is not obtainable
	 */
	public double getLoadForServer();
}
