package clustermonitor;

public class Rule {

	public enum Comparison {
		LESS_THAN, EQUAL, GREATER_THAN
	}

	public enum Action {
		ADD_SERVER, REMOVE_SERVER
	}
	
	public Rule(Cluster cluster, String metric, Comparison comp, double value, long duration, Action action) {
		
	}
}
