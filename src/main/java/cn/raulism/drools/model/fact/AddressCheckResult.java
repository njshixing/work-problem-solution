package cn.raulism.drools.model.fact;


import cn.raulism.drools.model.DrlFact;
import lombok.Data;

import java.util.List;

@Data
public class AddressCheckResult {

    private List<DrlFact> facts;
}
