package com.javatechie.spring.drools.api.model.facts;


import java.util.Date;

public interface JobFact extends Fact {

	public void removePartByCodePrimary( String codePrimary );
	
	public void removeAllParts();
	
	public void removeLabourByCode( String labourCode );
	
	public void removeAllLabours();
	
	public void removeGenericPartByCode( String code );
	
	public void removeAllGenericParts();

	public void addPartByCodePrimary(String codePrimary, Double quantity );
	
	/**
	 * @author frank
	 * @since Jul 9, 2008
	 *
	 * @param labourCode
	 * @param quantity in minutes
	 */
	public void addLabourByLabourCode(String labourCode, Double quantity );
	
	/**
	 * 
	 * @author frankschneider
	 * @since 20.10.2010
	 *
	 * @param codePrimary
	 * @param primaryPartCodesReplacement - in case multiple codes should serve as replacement, they must be separated by comma
	 */
	public void replacePartByCodePrimary(String codePrimary, String primaryPartCodesReplacement, String quantity );
	
	/**
	 * replaces a genericPart by physical parts
	 * 
	 * @author frankschneider
	 * @since 29.10.2010
	 *
	 * @param genericPartCode
	 * @param primaryPartCodesReplacement - in case multiple codes should serve as replacement, they must be separated by comma
	 */
	public void replaceGenericPartByCode(String genericPartCode, String primaryPartCodesReplacement, String quantity );
	
	/**
	 * 
	 * @author frankschneider
	 * @since 20.10.2010
	 *
	 * @param labourCode
	 * @param labourCodesReplacement - in case multiple codes should serve as replacement, they must be separated by comma
	 */
	public void replaceLabourByLabourCode(String labourCode, String labourCodesReplacement, String quantity );

	
	public void setPromotionCode(String promotionCode);
	
	public void setPriceExclTax( String value );

    public void setPriceInclTax( String value );

    public void setFixedLabourPriceExclTax( String value );
    
    public void setFixedLabourPriceInclTax( String value );
    
    public void setFixedPartPriceExclTax( String value );
    
    public void setFixedPartPriceInclTax( String value );
    
    public void setFixedPartPriceByPercentageAdjustmentExclTax(String value, String basedOnPrice );
    public void setFixedPartPriceByPercentageAdjustmentInclTax(String value, String basedOnPrice );
    public void setFixedLabourPriceByPercentageAdjustmentExclTax(String value, String basedOnPrice );
    public void setFixedLabourPriceByPercentageAdjustmentInclTax(String value, String basedOnPrice );

    public void setRoundingDetails(String roundingMode, int scale );
    
    public void adjustAnyLabourTimeByPercentage( String value );
    
    public void setAnyLabourTimeToValue( String quantityString );
	
	public void renameJob( String jobDescription );
	
	public void addAddOnJob( int operationId );

	public void addAutoAddJob( int operationId, String percentageOfParentTotalExclTax );

	public void addAddOnCustomJob( int customJobId );
	
	public void addAutoAddCustomJob( int customJobId, String percentageOfParentTotalExclTax );
	
	/**
	 * has been removed from the requirement - but I will leave it in the code and just remove the 
	 * data line in the database so we can reenable it, in case it will come back
	 * 
	 * The data line to remove the effect send message from the database is in table MenuRuleComponent. If it must be reenabled
	 * just add the following data into MenuRuleComponent table
	 * 
	 *  oid                  version_    entity__id  translationKey                                   factoryBeanName              mandatory displaySequence applicableForUserGroupType availableLogicConnectors 
	 *	-------------------- ----------- ----------- --------------------------------------------------------------------------------------- --------------- -------------------------- ------------------------ 
	 *	34                   0           51          localisedMenus.ruleCreation.effect.sendMessage   EffectComponentSendMessage   0         22              NULL                       NULL
	 *
	 * 
	 * @author frank
	 * @since Nov 11, 2008
	 *
	 * @param infoMessge
	 */
	public void showInfoMessage( String infoMessge );
	
	public void setCustomerTypeFreeText( String customerType );

	public Object clone() throws CloneNotSupportedException;

	public String getJobCode();
	
	public Date getJobLastModified();
	
	public boolean validateJobLastModified( boolean ignoreAffectedMenusChanged, long ruleCreationDateTime );
	
	public String getOperationCode();
	
	public String getPromotionCode();
	
	public String getJobDescription();
	
	public Object getJobObject();
	
	public <T> T containsPartWithCode( String primaryPartCode );
	
	public <T> T containsLabourWithCode( String labourCode );
	
	public boolean containsLocalPartWithCode( String partCode );
	
	public boolean containsPartWithSubBrand( String subBrand );
	
	/**
	 * whether the job is sourced as:
	 * 	- MAN = manufacturer
	 *  - NSC = nsc local menu
	 *  - LOC = dealer local menu 
	 *  - ESA = Electronic service assistant
	 *  - MOS = My Opel service
	 *
	 * @author frank
	 * @since May 26, 2008
	 *
	 * @param jobSource
	 */
	public void setJobSource( String jobSource );
	
	/**
	 * whether the job is sourced as:
	 * 	- MAN = manufacturer
	 *  - NSC = nsc local menu
	 *  - LOC = dealer local menu 
	 *  - ESA = Electronic service assistant
	 *  - MOS = My Opel service
	 *  - NSC_MOS = nsc My Opel service
	 *  - NSC_ESA = nsc Electronic service assistant
	 *  - NSC_MOS_ESA = nsc Electronic service assistant and My Opel service
	 *  - LOC_MOS = dealer My Opel service
	 *  - LOC_ESA = dealer Electronic service assistant
	 *  - LOC_MOS_ESA = dealer Electronic service assistant and My Opel service
	 *
	 * @author frank
	 * @since May 26, 2008
	 *
	 * @param jobSource
	 */
	public void setJobSourceType( int jobSource );
	
	/**
	 * whether the job is sourced as:
	 * 	- MAN = manufacturer
	 *  - NSC = nsc local menu
	 *  - LOC = dealer local menu
	 *  - ESA = Electronic service assistant
	 *  - MOS = My Opel service
	 *  
	 * @author frank
	 * @since 28 Dec 2008
	 *
	 * @return (MAN | NSC | LOC | ESA | MOS)
	 */
	public String getJobSourceAsString();
	
	/**
	 * set dealer MOS rule
	 * @param isDealerMOSRule
	 */
	public void setDealerMOSRule(String isDealerMOSRule);
	
	/**
	 * whether the rule is dealer MOS rule
	 * 
	 * @return
	 */
	public boolean isDealerMOSRule();
	
	/**
	 * set NSC MOS rule
	 * @param isNSCMOSRule
	 */
	public void setNSCMOSRule(boolean isNSCMOSRule);
	
	/**
	 * whether the rule is NSC MOS rule
	 * 
	 * @return
	 */
	public boolean isNSCMOSRule();
	
	/**
	 * set dealer ESA rule
	 * @param isDealerMOSRule
	 */
	public void setDealerESARule(String isDealerESARule);
	
	/**
	 * whether the rule is dealer ESA rule
	 * 
	 * @return
	 */
	public boolean isDealerESARule();
	
	/**
	 * set NSC ESA rule
	 * @param isNSCMOSRule
	 */
	public void setNSCESARule(boolean isNSCESARule);
	
	/**
	 * whether the rule is NSC ESA rule
	 * 
	 * @return
	 */
	public boolean isNSCESARule();
	
	public void setRuleAspectStrategy();
	
	public void setPartPriceByPercentageAdjustmentExclTax(String itemCode, String percentage );
	public void setLabourPriceByPercentageAdjustmentExclTax(String itemCode, String percentage );
	public void setLabourPriceExclTax(String itemCode, String price );
	public void setMaxLabUnitPriceExclTax(String itemCode, String price );
	public void setMaxPartUnitPriceExclTax(String itemCode, String price );
	public void setPartPriceExclTax(String itemCode, String price );
	
	public void addHiddenLabourCostExclTax( String hiddenCargeAsString );
	
	public void addAdditionalLabourTime(String newTime, String labourCode);
	
	public void setLabourRateExclTax( String labourRate );
	public void setLabourRateInclTax( String labourRate );
	
	public void setRuleDescriptionAndDisclaimer(String ruleDescBase64Encoded, String ruleDisclaimerBase64Encoded );
	
	public void setPartPriceByFixedDiscountIncTax( String discount );
	public void setPartPriceByFixedDiscountExclTax( String discount );
	public void setLabourPriceByFixedDiscountIncTax( String discount );
	public void setLabourPriceByFixedDiscountExclTax( String discount );
	public void setTotalPriceByFixedDiscountIncTax( String discount );
	public void setTotalPriceByFixedDiscountExclTax( String discount );
	
	public void setDisplaySequence( String displaySequence );
	
	public void setPackagePrice( String price );
	
}