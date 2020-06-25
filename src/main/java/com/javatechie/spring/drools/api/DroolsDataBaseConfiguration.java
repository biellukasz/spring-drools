package com.javatechie.spring.drools.api;

import com.javatechie.spring.drools.api.service.DataService;
import org.drools.compiler.builder.impl.KnowledgeBuilderImpl;
import org.drools.compiler.compiler.DrlParser;
import org.drools.compiler.compiler.DroolsParserException;
import org.drools.compiler.lang.descr.PackageDescr;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.drools.core.util.DroolsStreamUtils;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.util.*;

@Configuration
public class DroolsDataBaseConfiguration {

    private final DataService dataService;

    String localDsl= "[condition][]the rule is valid regarding \"{validFromInMillis}\" to \"{validToInMillis}\" = theDateFact : DateFact(); eval( theDateFact.validateRuleFromTo( ({validFromInMillis}L), ({validToInMillis}L) ) )\n" +
            "[condition][]the rule is applicable accoring to job last modified for \"{ruleCreationDateInMillis}\" inCombinationWith \"{ruleIgnoreAffectedMenusChanged}\" = theJobFactForEval: JobFact(); eval( theJobFactForEval.validateJobLastModified({ruleIgnoreAffectedMenusChanged}, ({ruleCreationDateInMillis}L) ) )\n" +
            "[condition][]the job code startsWith \"{jobTypeCodeSelected}\" = theJobFact: JobFact(jobCode matches \"{jobTypeCodeSelected}[a-zA-Z_0-9 ]*\"); \n" +
            "[condition][]identify the vehicleFact = theVehicleFact: VehicleFact();\n" +
            "[condition][]the vehicle make characteristic is \"{makeCharacteristicId}\" \"{connector}\" = {connector} ( eval(theVehicleFact.containsCharacteristicId((Integer) {makeCharacteristicId}))  ); \n" +
            "[condition][]the vehicle model characteristic is \"{modelCharacteristicId}\" \"{connector}\" = {connector} ( eval(theVehicleFact.containsCharacteristicId((Integer) {modelCharacteristicId})) );\n" +
            "[condition][]the vehicle modelyear isGreaterOrEqual to \"{modelYear}\" = and ( eval( theVehicleFact.isModelYearInFromRange( (Integer) {modelYear} ) ) );\n" +
            "[condition][]the vehicle modelyear isLowerOrEqual to \"{modelYear}\" = and ( eval( theVehicleFact.isModelYearInToRange( (Integer) {modelYear} ) ) );\n" +
            "[condition][]the vehicle engine code characteristic is \"{engineCodeCharacteristicId}\" \"{connector}\" = {connector} ( eval(theVehicleFact.containsCharacteristicId((Integer) {engineCodeCharacteristicId})) );\n" +
            "[condition][]the vehicle fuel type characteristic is \"{fuelTypeCharacteristicId}\" \"{connector}\" = {connector} ( eval(theVehicleFact.containsCharacteristicId((Integer) {fuelTypeCharacteristicId})) );\n" +
            "[condition][]the vehicle age isGreaterOrEqual to \"{vehicleAge}\" \"{vehicleMonth}\" = and ( eval( theVehicleFact.isVehicleAgeOlderOrSame ( (Integer) {vehicleAge}, (Integer) {vehicleMonth} ) ) );\n" +
            "[condition][]the vehicle age isLowerOrEqual to \"{vehicleAge}\" \"{vehicleMonth}\" = and ( eval( theVehicleFact.isVehicleAgeYoungerOrSame ( (Integer) {vehicleAge}, (Integer) {vehicleMonth} ) ) );\n" +
            "[condition][]the job contains a part with partCode \"{primaryPartCode}\" \"{connector}\" = {connector} ( eval( theJobFact.containsPartWithCode( \"{primaryPartCode}\" ) != null ) );\n" +
            "[condition][]the job contains a localpart with partCode \"{partCode}\" \"{connector}\" = {connector} ( eval( theJobFact.containsLocalPartWithCode( \"{partCode}\" ) ) );\n" +
            "[condition][]the job contains a labour with labourCode \"{labourCode}\" \"{connector}\" = {connector} ( eval( theJobFact.containsLabourWithCode( \"{labourCode}\" ) != null ) );\n" +
            "[condition][]the job contains a part and the sub brand \"{subBrandId}\" \"{connector}\" = {connector} ( eval( theJobFact.containsPartWithSubBrand( \"{subBrandId}\" ) ) );\n" +
            "[condition][]bindStart = (\n" +
            "[condition][]bindEnd = )\n" +
            "\n" +
            "[consequence][]keep manufacturer menu = generatedMenus.add( theJobFact ); \n" +
            "\n" +
            "[consequence][]clone base menu = JobFact theClonedJobFact = (JobFact) theJobFact.clone(); generatedMenus.add( theClonedJobFact );\n" +
            "[consequence][]set menu source \"{menuSourceString}\" with type \"{menuSourceType}\" = theClonedJobFact.setJobSource( \"{menuSourceString}\" ); theClonedJobFact.setJobSourceType( (int){menuSourceType} ) ;\n" +
            "[consequence][]set whether the rule is dealer MOS rule \"{isDealerMOSRule}\" = theClonedJobFact.setDealerMOSRule( \"{isDealerMOSRule}\" );\n" +
            "[consequence][]set whether the rule is dealer ESA rule \"{isDealerESARule}\" = theClonedJobFact.setDealerESARule( \"{isDealerESARule}\" );\n" +
            "[consequence][]set menu rule descriptions \"{menuRuleDesc}\" and disclaimer \"{menuRuleDisclaimer}\" = theClonedJobFact.setRuleDescriptionAndDisclaimer( \"{menuRuleDesc}\", \"{menuRuleDisclaimer}\" );\n" +
            "[consequence][]set menu rule display sequence \"{displaySequence}\" = theClonedJobFact.setDisplaySequence( \"{displaySequence}\" );\n" +
            "\n" +
            "[consequence][]rename menu description \"{menuDescription}\" = theClonedJobFact.renameJob( \"{menuDescription}\" );\n" +
            "[consequence][]append to menu description \"{menuDescription}\" = theClonedJobFact.renameJob( theClonedJobFact.getJobDescription() + \" {menuDescription}\" );\n" +
            "[consequence][]remove part with partCode \"{primaryPartCode}\" = theClonedJobFact.removePartByCodePrimary( \"{primaryPartCode}\" );\n" +
            "[consequence][]remove all parts = theClonedJobFact.removeAllParts();\n" +
            "[consequence][]remove labour with labourCode \"{labourCode}\" = theClonedJobFact.removeLabourByCode( \"{labourCode}\" );\n" +
            "[consequence][]remove all labours = theClonedJobFact.removeAllLabours();\n" +
            "[consequence][]remove genericPart with code \"{code}\" = theClonedJobFact.removeGenericPartByCode( \"{code}\" );\n" +
            "[consequence][]remove all genericParts = theClonedJobFact.removeAllGenericParts();\n" +
            "\n" +
            "[consequence][]add part with partCode \"{primaryPartCode}\" and quantity \"{quantity}\" = theClonedJobFact.addPartByCodePrimary(\"{primaryPartCode}\", ((Double){quantity}) );\n" +
            "[consequence][]add labour with labourCode \"{labourCode}\" and quantity \"{quantity}\" = theClonedJobFact.addLabourByLabourCode(\"{labourCode}\", ((Double){quantity}) );\n" +
            "\n" +
            "[consequence][]replace part with partCode \"{primaryPartCode}\" by partCodes \"{primaryPartCodesReplacement}\" and quantity \"{quantity}\" = theClonedJobFact.replacePartByCodePrimary(\"{primaryPartCode}\", \"{primaryPartCodesReplacement}\", \"{quantity}\" );\n" +
            "[consequence][]replace labour with labourCode \"{labourCode}\" by labourCodes \"{labourCodesReplacement}\" and quantity \"{quantity}\" = theClonedJobFact.replaceLabourByLabourCode(\"{labourCode}\", \"{labourCodesReplacement}\", \"{quantity}\" );\n" +
            "[consequence][]replace genericPart with code \"{code}\" by partCodes \"{primaryPartCodesReplacement}\" and quantity \"{quantity}\" = theClonedJobFact.replaceGenericPartByCode(\"{code}\", \"{primaryPartCodesReplacement}\", \"{quantity}\" );\n" +
            "\n" +
            "\n" +
            "[consequence][]assign retail promotion code \"{promoCode}\" = theClonedJobFact.setPromotionCode( \"{promoCode}\" );\n" +
            "[consequence][]assign total price excluding tax \"{priceExclTax}\" = theClonedJobFact.setPriceExclTax( \"{priceExclTax}\" );\n" +
            "[consequence][]assign total price including tax \"{priceInclTax}\" = theClonedJobFact.setPriceInclTax( \"{priceInclTax}\" );\n" +
            "[consequence][]assign fixed labour price excluding tax \"{priceExclTax}\" = theClonedJobFact.setFixedLabourPriceExclTax( \"{priceExclTax}\" );\n" +
            "[consequence][]assign fixed labour price including tax \"{priceInclTax}\" = theClonedJobFact.setFixedLabourPriceInclTax( \"{priceInclTax}\" );\n" +
            "[consequence][]assign fixed part price excluding tax \"{priceExclTax}\" = theClonedJobFact.setFixedPartPriceExclTax( \"{priceExclTax}\" );\n" +
            "[consequence][]assign fixed part price including tax \"{priceInclTax}\" = theClonedJobFact.setFixedPartPriceInclTax( \"{priceInclTax}\" );\n" +
            "\n" +
            "[consequence][]assign labour rate excluding tax \"{labourRate}\" = theClonedJobFact.setLabourRateExclTax( \"{labourRate}\" );\n" +
            "[consequence][]assign labour rate including tax \"{labourRate}\" = theClonedJobFact.setLabourRateInclTax( \"{labourRate}\" );\n" +
            "\n" +
            "[consequence][]adjust any labour time by percentage \"{percentage}\" = theClonedJobFact.adjustAnyLabourTimeByPercentage( \"{percentage}\" );\n" +
            "\n" +
            "[consequence][]adjust part price by fixed discount including tax \"{discount}\" = theClonedJobFact.setPartPriceByFixedDiscountIncTax( \"{discount}\" );\n" +
            "[consequence][]adjust part price by fixed discount excluding tax \"{discount}\" = theClonedJobFact.setPartPriceByFixedDiscountExclTax( \"{discount}\" );\n" +
            "[consequence][]adjust labour price by fixed discount including tax \"{discount}\" = theClonedJobFact.setLabourPriceByFixedDiscountIncTax( \"{discount}\" );\n" +
            "[consequence][]adjust labour price by fixed discount excluding tax \"{discount}\" = theClonedJobFact.setLabourPriceByFixedDiscountExclTax( \"{discount}\" );\n" +
            "[consequence][]adjust total price by fixed discount including tax \"{discount}\" = theClonedJobFact.setTotalPriceByFixedDiscountIncTax( \"{discount}\" );\n" +
            "[consequence][]adjust total price by fixed discount excluding tax \"{discount}\" = theClonedJobFact.setTotalPriceByFixedDiscountExclTax( \"{discount}\" );\n" +
            "\n" +
            "[consequence][]set package price \"{price}\" = theClonedJobFact.setPackagePrice( \"{price}\" );\n" +
            "\n" +
            "[consequence][]adjust fixed labour price by percentage excluding tax \"{percentage}\" basedOn \"{targetPrice}\" = theClonedJobFact.setFixedLabourPriceByPercentageAdjustmentExclTax( \"{percentage}\", \"{targetPrice}\" );\n" +
            "[consequence][]adjust fixed labour price by percentage including tax \"{percentage}\" basedOn \"{targetPrice}\" = theClonedJobFact.setFixedLabourPriceByPercentageAdjustmentInclTax( \"{percentage}\", \"{targetPrice}\" );\n" +
            "[consequence][]adjust fixed part price by percentage excluding tax \"{percentage}\" basedOn \"{targetPrice}\" = theClonedJobFact.setFixedPartPriceByPercentageAdjustmentExclTax( \"{percentage}\", \"{targetPrice}\" );\n" +
            "[consequence][]adjust fixed part price by percentage including tax \"{percentage}\" basedOn \"{targetPrice}\" = theClonedJobFact.setFixedPartPriceByPercentageAdjustmentInclTax( \"{percentage}\", \"{targetPrice}\" );\n" +
            "\n" +
            "[consequence][]set job roundingDetails \"{roundingMode}\" to scale \"{scale}\" = theClonedJobFact.setRoundingDetails( \"{roundingMode}\", {scale} );\n" +
            "\n" +
            "[consequence][]set any labour time to value \"{quantity}\" = theClonedJobFact.setAnyLabourTimeToValue( \"{quantity}\" );\n" +
            "\n" +
            "[consequence][]adjust part price for item \"{itemCode}\" by percentage excluding tax \"{percentage}\" = theClonedJobFact.setPartPriceByPercentageAdjustmentExclTax( \"{itemCode}\", \"{percentage}\" );\n" +
            "[consequence][]adjust labour price for item \"{itemCode}\" by percentage excluding tax \"{percentage}\" = theClonedJobFact.setLabourPriceByPercentageAdjustmentExclTax( \"{itemCode}\", \"{percentage}\" );\n" +
            "[consequence][]assign labour price for item \"{itemCode}\" excluding tax \"{price}\" = theClonedJobFact.setLabourPriceExclTax( \"{itemCode}\", \"{price}\" );\n" +
            "[consequence][]assign part price for item \"{itemCode}\" excluding tax \"{price}\" = theClonedJobFact.setPartPriceExclTax( \"{itemCode}\", \"{price}\" );\n" +
            "\n" +
            "[consequence][]assign labour MAX price for item \"{itemCode}\" excluding tax \"{price}\" = theClonedJobFact.setMaxLabUnitPriceExclTax( \"{itemCode}\", \"{price}\" );\n" +
            "[consequence][]assign part MAX price for item \"{itemCode}\" excluding tax \"{price}\" = theClonedJobFact.setMaxPartUnitPriceExclTax( \"{itemCode}\", \"{price}\" );\n" +
            "\n" +
            "[consequence][]auto add menu with operationId \"{operationId}\" percentageOfParentOperation excl tax \"{percentage}\" = theClonedJobFact.addAutoAddJob( (Integer) {operationId}, org.apache.commons.lang.StringUtils.stripToNull( org.apache.commons.lang.StringUtils.strip( \"{percentage}\", \"null\" ) ) );\n" +
            "[consequence][]list menu with operationId \"{operationId}\" as addOn = theClonedJobFact.addAddOnJob( (Integer) {operationId} );\n" +
            "[consequence][]show info message \"{infoMessage}\" = theClonedJobFact.showInfoMessage( \"{infoMessage}\" );\n" +
            "\n" +
            "[consequence][]auto add custom job with jobId \"{customJobId}\" percentageOfParentOperation excl tax \"{percentage}\" = theClonedJobFact.addAutoAddCustomJob( (Integer) {customJobId}, org.apache.commons.lang.StringUtils.stripToNull( org.apache.commons.lang.StringUtils.strip( \"{percentage}\", \"null\" ) ) );\n" +
            "[consequence][]list custom job with jobId \"{customJobId}\" as addOn = theClonedJobFact.addAddOnCustomJob( (Integer) {customJobId} );\n" +
            "\n" +
            "[consequence][]assign customer type free text \"{customerType}\" = theClonedJobFact.setCustomerTypeFreeText( \"{customerType}\" );\n" +
            "\n" +
            "[consequence][]add hidden labour cost excl tax \"{hiddenCargeAsString}\" = theClonedJobFact.addHiddenLabourCostExclTax( \"{hiddenCargeAsString}\" );\n" +
            "\n" +
            "[consequence][]add additional labour time for \"{labourCode}\" by time \"{newTime}\"  = theClonedJobFact.addAdditionalLabourTime( \"{newTime}\", \"{labourCode}\" );\n";

    public DroolsDataBaseConfiguration(DataService dataService) {
        this.dataService = dataService;
    }


//    public KieFileSystem getKieFileSystem() throws IOException, ClassNotFoundException {
//        KieFileSystem kieFileSystem = getKieServices().newKieFileSystem();
//        for (Resource resource : getRulesFromDB()) {
//            kieFileSystem.write(resource);
//        }
//        kieFileSystem.write(ResourceFactory.newClassPathResource("rule.drl"));
//        return kieFileSystem;
//    }

    private List<String> getRulesFromDB() throws IOException, ClassNotFoundException {
        return dataService.getRulesFromDB();
    }

//    @Bean
//    public KieContainer getKieContainer() throws IOException, ClassNotFoundException, DroolsParserException {
//        final KieRepository kieRepository = getKieServices().getRepository();
//
//        kieRepository.addKieModule(new KieModule() {
//            public ReleaseId getReleaseId() {
//                return kieRepository.getDefaultReleaseId();
//            }
//        });
//
//        KieBuilder kieBuilder = getKieServices().newKieBuilder(getKieFileSystem());
//        kieBuilder.buildAll();
//        //
//
//
//
//        if (kieBuilder.getResults().hasMessages(Message.Level.ERROR)) {
//            for (Message result : kieBuilder.getResults().getMessages()) {
//                System.out.println("Error: " + result.getText());
//            }
//            throw new RuntimeException("Unable to create KJar for requested conditions resources");
//        }
//        return getKieServices().newKieContainer(kieRepository.getDefaultReleaseId());
//    }

    private KieServices getKieServices() {
        return KieServices.Factory.get();
    }

//    @Bean
//    public KieBase kieBase() throws IOException, ClassNotFoundException, DroolsParserException {
//        return getKieContainer().getKieBase();
//    }

    @Bean
    public Map<String,KieSession> kieSession() throws IOException, ClassNotFoundException, DroolsParserException {
        // Migrating from 5 - 7 version of drools
        // Generate rule drl from database (e.g RuleFactoryImpl)
        // Parsing with dsl file
        KnowledgeBuilderImpl knowledgeBuilder = new KnowledgeBuilderImpl();
        Map<String,KieSession> kieSessionMap = new HashMap<>();
        for(String rule : getRulesFromDB()){
            DrlParser drlParser = new DrlParser();
            // Building package
            PackageDescr parse = drlParser.parse(rule,new StringReader(localDsl));

            knowledgeBuilder.addPackage(parse);


        }

        final InternalKnowledgeBase kBase = KnowledgeBaseFactory.newKnowledgeBase();
        kBase.addPackages( Arrays.asList(knowledgeBuilder.getPackages()) );
        KieSession kieSession = kBase.newKieSession();
        kieSessionMap.put("dealerCode",kieSession);


//         PackageDescr parse = drlParser.parse(rule,new StringReader(localDsl));
        // Saving byte array to DB
//        byte[] streamOut = DroolsStreamUtils.streamOut( parse );


        // Kie server side
        // Fetching rules for each dealer and creating kie session
//        ByteArrayInputStream ruleBinaryStream = new ByteArrayInputStream( streamOut );
//        PackageDescr pck = (PackageDescr) DroolsStreamUtils.streamIn(ruleBinaryStream);


        // Creating map with kiesessions
        return kieSessionMap;
    }
}
