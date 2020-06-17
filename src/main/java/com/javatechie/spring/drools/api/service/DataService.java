package com.javatechie.spring.drools.api.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class DataService {


    public List<byte[]> getRulesFromDB() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        List<byte[]> rulesList = new ArrayList<>();
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setUrl("jdbc:sqlserver://127.0.0.1:1433;DatabaseName=MP2_GMG_MNU");
        dataSource.setUsername("sa");
        dataSource.setPassword("Lukas!98765");
        JdbcTemplate template = new JdbcTemplate(dataSource);
        try{
            List<Map<String, Object>> rules = template.queryForList("SELECT droolsBinaryExecution FROM  MenuRule");
            rules.forEach(map -> rulesList.add((byte[]) map.get("droolsBinaryExecution")));
            return rulesList;
        }finally {
            dataSource.destroy();
        }
    }
}
