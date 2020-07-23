package cn.raulism.drools.model;


import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Address {

    private String postcode;

    private String street;

    private String state;

    private List<DrlFact> facts;

    private Map<String, DrlFact> factMap;

}
