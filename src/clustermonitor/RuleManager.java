package clustermonitor;

import java.util.ArrayList;

public class RuleManager {

	private ArrayList<Rule> _rules;
	
	public RuleManager() {
		_rules = new ArrayList<Rule>();
	}
	
	public void addRule(Rule r) {
		_rules.add(r);
	}
}
