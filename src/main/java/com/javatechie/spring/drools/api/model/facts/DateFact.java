package com.javatechie.spring.drools.api.model.facts;

import java.sql.Timestamp;

public interface DateFact extends Fact {

	public long currentTimeInMillis();
	
	public Timestamp currentTime();
	
	public boolean validateRuleFromTo( long ruleDateFromInMillis, long ruleDateToInMillis );

}