package clustermonitor;

import java.util.ArrayList;
import java.util.Calendar;

import clustermonitor.Rule.Action;
import clustermonitor.Rule.Comparison;

public class Cluster {

	private ArrayList<Server> _servers;
	private String _clusterName;
	private PerformanceMetrics _availableMetrics;
	private RuleManager _ruleManager;

	Cluster(String clusterName) {

		_clusterName = clusterName;
		_ruleManager = new RuleManager();

		// instantiate servers
		_servers = new ArrayList<Server>();
	}

	void addServer(Server server) {

		if (!_servers.contains(server)) {
			_servers.add(server);
		}

	}

	void setAvailableMetrics(PerformanceMetrics pm) {
		_availableMetrics = pm;
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
				count++;
			}
		}
		return count;
	}

	/**
	 * Return the number of servers that are available to do work (e.g. not on).
	 * 
	 * @return
	 */
	int getFreeServerCount() {
		int count = 0;
		for (Server s : _servers) {
			if (s.isAvailable()) {
				count ++;
			}
		}
		return count;
	}
	
	int getDisabledServerCount() {
		int count = 0;
		for (Server s : _servers) {
			if (s.isDisabled()) {
				count ++;
			}
		}
		
		return count;
	}

	public void addRule(String metric, Comparison comp, double value,
			long duration, Action action) {
		Rule r = new Rule(this, metric, comp, value, duration, action);
		_ruleManager.addRule(r);
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

	public void collectMetricsAndEvaluateRules() {

		PerformanceMetrics latestMetrics = new PerformanceMetrics(
				_availableMetrics);

		// get performance metrics for all servers in cluster
		for (Server s : _servers) {

			// only if it is running right now
			if (s.isActive()) {

				PerformanceMetrics pmc = new PerformanceMetrics(latestMetrics);
				s.getPerformanceMetrics(pmc);
				latestMetrics.addMetricValues(pmc);

			}
		}

		// System.out.println(latestMetrics);

		_ruleManager.processMetrics(latestMetrics);

	}

	public void processAction(Action action) {

		System.out.println("Performing " + action + " at time "
				+ Calendar.getInstance().getTimeInMillis());

		// if we want to add a server and have no more
		if (action == Action.ADD_SERVER && getFreeServerCount() == 0) {
			System.out.println("No more servers to add!");
		}
		// if we want to remove a server and only have 1
		else if (action == Action.REMOVE_SERVER && getActiveServerCount() <= 1) {
			System.out.println("Can't have less than 1 server.");
		}
		// add a server
		else if (action == Action.ADD_SERVER) {
			System.out.println("Adding server.");
			Server s = getAvailableServer();
			if (s != null) {
				s.startServer();
			} else {
				System.err.println("Unable to add server.");
			}
		}
		// remove a server
		else if (action == Action.REMOVE_SERVER) {
			System.out.println("Removing server.");
			Server s = getActiveServer();
			if (s != null) {
				
				// TODO: disable
				
				s.stopServer();
				
				// TODO: set a timeout after which this server should be removed
			} else {
				System.err.println("Unable to remove server.");
			}
		}
		
		printServerStatus();
		System.out.println("Free server count " + getFreeServerCount());
		System.out.println("Active server count " + getActiveServerCount());
		System.out.println("Disabled server count " + getDisabledServerCount());

	}

	/**
	 * Return an available server (or null if not available). Useful when
	 * wanting to bring a server online.
	 * 
	 * @return
	 */
	public Server getAvailableServer() {
		for (Server s : _servers) {
			if (s.isAvailable()) {
				return s;
			}
		}
		return null;
	}

	/**
	 * Return a random active server (or null if not available...but that should
	 * not happen). Useful when wanting to bring a server down.
	 * 
	 * @return
	 */
	public Server getActiveServer() {
		for (Server s : _servers) {
			if (s.isActive()) {
				return s;
			}
		}
		return null;
	}
	
	void printServerStatus() {
		
		System.out.println("Servers in cluster " + getName());
		
		for (Server s : _servers) {
			System.out.println(s);
		}
		
		System.out.println();
	}

}
