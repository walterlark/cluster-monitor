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
			long duration, Action action) {
		_cluster = cluster;
		_metric = metric;
		_comp = comp;
		_value = value;
		_duration = duration;
		_action = action;
		_wasTrue = false;

	}

	/**
	 * Return true if adding this new data has made the rule true, false
	 * otherwise.
	 * 
	 * @param pm
	 */
	boolean evaluateRule(PerformanceMetrics pm) {

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

		// if it's true, and we haven't evaluated it within the adjustment time,
		// go for it!
		if (isTrue && (now - _lastActionAt) > ClusterMonitorConstants.ADJUSTMENT_TIME) {
			if (now - _trueSince > _duration) {
				_lastActionAt = now;
				_wasTrue = false;
				_cluster.processAction(_action);
				return true;
			}
		}

		return false;

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
		String s = "Evaluates to true when " + _metric + " is " + _comp + " "
				+ _value + " for " + _duration + "ms";
		return s;
	}

	/**
	 * If another rule gets set to true, by calling this function, we can ensure
	 * that this rule will not perform its action until the
	 * ClusterMonitor.ADJUST_TIME has expired.
	 */
	public void setActionOccurred() {
		_lastActionAt = Calendar.getInstance().getTimeInMillis();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_action == null) ? 0 : _action.hashCode());
		result = prime * result
				+ ((_cluster == null) ? 0 : _cluster.hashCode());
		result = prime * result + ((_comp == null) ? 0 : _comp.hashCode());
		result = prime * result + (int) (_duration ^ (_duration >>> 32));
		result = prime * result + ((_metric == null) ? 0 : _metric.hashCode());
		long temp;
		temp = Double.doubleToLongBits(_value);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Rule other = (Rule) obj;
		if (_action != other._action)
			return false;
		if (_cluster == null) {
			if (other._cluster != null)
				return false;
		} else if (!_cluster.equals(other._cluster))
			return false;
		if (_comp != other._comp)
			return false;
		if (_duration != other._duration)
			return false;
		if (_metric == null) {
			if (other._metric != null)
				return false;
		} else if (!_metric.equals(other._metric))
			return false;
		if (Double.doubleToLongBits(_value) != Double
				.doubleToLongBits(other._value))
			return false;
		return true;
	}
}