package tdg.teide.filters.overlap;

import tdg.teide.filters.AbstractTeideFilterManager;

public class Overlapper extends AbstractTeideFilterManager{

	public Overlapper(Double threshold) {
		super("overlap", threshold, "NEIGHBORS_LINKED");
		
		this.metric = new OverlapMetric( this.inputTable, this.metricTable, this.filterName);
		this.filter = new OverlapFilter(threshold, this.metricTable, this.filterTable, this.filterName);
		
	}

	
	
}
