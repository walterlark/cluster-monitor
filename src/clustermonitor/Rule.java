package clustermonitor;

import java.util.Calendar;

public class Rule {

	public enum Comparison {
		LESS_THAN, EQUAL, GREATER_THAN
	}

	public enum Action {
		ADD_SERVER, REMOVE_SERVER
	}

	private Cluster _cluster;
	private String _metric;
	private Comparison _comp;
	private double _value;
	private long _duration;
	private long _adjustmentTime;
	private Action _action;

	/**
	 * Stores the first time this became true. This value is updated to false
	 * whenever the rule is not true. By doing this, we can keep track of how
	 * long this rule has been true and therefore evaluate the rule to true only
	 * when it meets the duration requirement.
	 */
	private long _trueSince;
	private boolean _wasTrue;
	private long _lastActionAt;

	Rule(Cluster cluster, String metric, Comparison comp, double value,
			long duration, long adjustmentTime, Action action) {
		_cluster = cluster;
		_metric = metric;
		_comp = comp;
		_value = value;
		_duration = duration;
		_adjustmentTime = adjustmentTime;
		_action = action;
		_wasTrue = false;

	}

	/**
	 * Return true if adding this new data has made the rule true, false
	 * otherwise.
	 * 
	 * @param pm
	 * @return
	 */
	boolean evaluateRule(PerformanceMetrics pm) {

		boolean returnValue = false;
		long now = Calendar.getInstance().getTimeInMillis();
		boolean isTrue = false;

		// if the metric is present
		if (pm.hasMetric(_metric)) {
			double current = pm.getValueForMetric(_metric);
			isTrue = compare(current, _comp, _value);
		}

		// set _trueSince if this became true right now
		if (!_wasTrue && isTrue) {
			_trueSince = now;
		}
		
		// update value for next time
		_wasTrue = isTrue;
		
		// if it's true, and we haven't evaluated it within the adjustment time, go for it!
		if (isTrue && (now - _lastActionAt) > _adjustmentTime) {
			if (now - _trueSince > _duration) {
				returnValue = true;
				_lastActionAt = now;
				_wasTrue = false; // make sure it doesn't happen immediately again
			}
		}

		return returnValue;
	}

	public long get_duration() {
		return _duration;
	}

	public void set_duration(long _duration) {
		this._duration = _duration;
	}

	private boolean compare(double one, Comparison comp, double two) {

		switch (comp) {
		case LESS_THAN:
			return one < two;
		case EQUAL:
			return one == two;
		case GREATER_THAN:
			return one > two;
		default:
			return false;
		}

	}
	
	@Override
	public String toString() {
		String s = "Evaluates to true when " + _metric + " is " + _comp + " " + _value + " for " + _duration + "ms";
		return s;
	}

}
