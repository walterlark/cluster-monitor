package clustermonitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * This class represents one time-slice of metrics. For example, it could store
 * values for load and memory usage. Only assumption (for now) is that metrics
 * are representable by a timestamp and a double. Boolean values can be
 * represented as a 0/1.
 * 
 * @author Walter
 * 
 */
public class PerformanceMetrics {

	private HashMap<String, MetricCountPair> _metrics;
	public static final double NO_DATA = Double.NEGATIVE_INFINITY;

	public PerformanceMetrics(String... metrics) {

		_metrics = new HashMap<String, MetricCountPair>();

		for (String s : metrics) {
			_metrics.put(s, new MetricCountPair());
		}
	}

	/**
	 * Create a data-less copy of the passed-in instance.
	 * 
	 * @param pm
	 */
	public PerformanceMetrics(PerformanceMetrics pm) {
		this(pm._metrics.keySet().toArray(new String[pm._metrics.size()]));
	}

	/**
	 * Sets the value for the given metric to that passed in. If there is
	 * already a value present for this metric, the new metric will simply
	 * overwrite it. If the metric is not enabled for this instance of
	 * PerformanceMetrics, false will be returned.
	 * 
	 * @param metric
	 * @param value
	 * @return
	 */
	public boolean setMetricValue(String metric, double value) {
		if (_metrics.containsKey(metric)) {
			_metrics.get(metric).clearValues();
			_metrics.get(metric).addValue(value);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Add a value to this metric (average is computed automatically). This will
	 * *NOT* clear old values (obviously) as opposed to setMetricValue, which
	 * will clear all previous values.
	 * 
	 * @param metric
	 * @param value
	 * @return
	 */
	public boolean addMetricValue(String metric, double value) {
		if (_metrics.containsKey(metric)) {
			_metrics.get(metric).addValue(value);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Add all values from the passed-in {@link PerformanceMetric}s to this
	 * {@link PerformanceMetrics} instance. Useful when aggregating a number of
	 * {@link PerformanceMetric} values from a number of different sources (e.g.
	 * all servers in a cluster)
	 * 
	 * @param pm
	 */
	public void addMetricValues(PerformanceMetrics pm) {

		String[] metrics = pm.getAvailableMetrics();

		for (String metric : metrics) {
			this.addMetricValue(metric, pm.getValueForMetric(metric));
		}

	}

	/**
	 * Return the list of metrics available that have data.
	 * 
	 * @return
	 */
	public String[] getAvailableMetrics() {

		ArrayList<String> metrics = new ArrayList<String>();

		for (String s : _metrics.keySet()) {
			if (_metrics.get(s).getValue() != NO_DATA) {
				metrics.add(s);
			}
		}

		return metrics.toArray(new String[metrics.size()]);
	}

	/**
	 * Return an array of all metrics in this instance of PerformanceMetric
	 * regardless of whether or not they have data.
	 * 
	 * @return
	 */
	public String[] getMetrics() {
		return _metrics.keySet().toArray(new String[_metrics.size()]);
	}

	/**
	 * Returns the value associated with this metric, or NO_DATA is no data is
	 * present.
	 * 
	 * @return
	 */
	public double getValueForMetric(String metric) {
		return _metrics.get(metric).getValue();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Performance Metrics:\n\n");

		for (Entry<String, MetricCountPair> e : _metrics.entrySet()) {
			if (e.getValue().getValue() != NO_DATA) {
				builder.append(e.getKey() + ": " + e.getValue().getValue() + " (" + e.getValue().getCount() + ")\n");
			} else {
				builder.append(e.getKey() + ": -\n");
			}
		}

		builder.append("\n");

		return builder.toString();
	}

	/**
	 * This class is used so that multiple values can be added to this instance
	 * of PerformanceMetrics and an average returned.
	 * 
	 * @author Walter
	 * 
	 */
	private class MetricCountPair {

		private double _totalValue;
		private int _count;

		MetricCountPair() {
			clearValues();
		}

		/**
		 * Reset values to 0.
		 */
		void clearValues() {
			_totalValue = 0;
			_count = 0;
		}

		/**
		 * Add a value on to this metric.
		 * 
		 * @param value
		 */
		void addValue(double value) {
			_totalValue += value;
			_count++;
		}

		/**
		 * Computes the average value based on the number of metrics passed in.
		 * If no values are contained within, a value of NO_DATA will be
		 * returned.
		 * 
		 * @return
		 */
		double getValue() {

			if (_totalValue == 0) {
				return NO_DATA;
			} else {
				return _totalValue / ((double) _count);
			}
		}
		
		/**
		 * Return number of values associated with this metric.
		 * 
		 * @return
		 */
		int getCount() {
			return _count;
		}

	}

}
