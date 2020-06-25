package com.javatechie.spring.drools.api.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class DataService {


    public List<String> getRulesFromDB() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        List<String> rulesList = new ArrayList<>();
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setUrl("jdbc:sqlserver://127.0.0.1:1433;DatabaseName=MP2_GMG_MNU");
        dataSource.setUsername("sa");
        dataSource.setPassword("Lukas!98765");
        JdbcTemplate template = new JdbcTemplate(dataSource);
        try{
            List<Map<String, Object>> rules = template.queryForList("SELECT ruleDescription FROM  MenuRule");
            rules.stream()
                    .map(map ->  (String) map.get("ruleDescription"))
                    .filter(ruleDescription -> !"".equals(ruleDescription) && ruleDescription != null && ruleDescription.contains("package"))
                    .map(rule -> rule.replace("import com.cliffordthames.evolution.menumgmt.facts.*;","import com.javatechie.spring.drools.api.model.facts.*"))
                    .filter(rule -> !rule.contains("rule \"SML_dealerrecservice_manufactor\""))
                    .filter(rule -> !rule.contains("rule \"SML_dealerrecservice_custom\""))
                    .forEach(rulesList::add);
            return rulesList;
        }finally {
            dataSource.destroy();
        }
    }
}
