package clustermonitor;

import java.util.ArrayList;

/**
 * Represents the rule manager for one cluster. Can be used to store rules as
 * well as evaluate when rule conditions are met and the appropriate action
 * should be taken.
 * 
 * @author Walter
 * 
 */
class RuleManager {

	private ArrayList<Rule> _rules;
	private long _metricsTTL;

	RuleManager() {
		_rules = new ArrayList<Rule>();
		_metricsTTL = 0;
	}

	/**
	 * Add a rule to the rule manager.
	 * 
	 * @param r
	 */
	void addRule(Rule r) {
		_rules.add(r);
		if (r.get_duration() > _metricsTTL) {
			_metricsTTL = r.get_duration();
		}
	}

	void processMetrics(PerformanceMetrics pm) {
		
		for (Rule r : _rules) {
			boolean actionOccurred = r.evaluateRule(pm);
			
			if (actionOccurred) {
				for (Rule rr : _rules) {
					if (rr != r) {
						rr.setActionOccurred();
					}
				}
			}
			
		}

	}

}
