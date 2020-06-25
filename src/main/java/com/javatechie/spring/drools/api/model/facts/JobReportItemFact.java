package com.javatechie.spring.drools.api.model.facts;

public interface JobReportItemFact extends JobFact {
	
	public int getJobId();

	public double getPrice();
	
	public double getPriceParts();
	
	public double getCostParts();
	
	public double getPriceLabours();
	
	public double getCostLabours();
	
}
