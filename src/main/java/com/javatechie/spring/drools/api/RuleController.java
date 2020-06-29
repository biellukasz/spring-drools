package com.javatechie.spring.drools.api;

import com.javatechie.spring.drools.api.model.FactsDTO;
import com.javatechie.spring.drools.api.model.Order;
import org.kie.api.runtime.KieSession;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class RuleController {

	private final Map<String,KieSession> sessions;

	public RuleController(Map<String,KieSession> sessions) {
		this.sessions = sessions;
	}


	@GetMapping("/rule/{dealerCode}")
	public void fireRules(@PathVariable String dealerCode, FactsDTO facts) {
		KieSession kieSession = sessions.get(dealerCode);
		kieSession.insert( facts );
		kieSession.fireAllRules();
	}

	@GetMapping("/test")
	public void testRule(){
		Order order = new Order();
		order.setCardType("HDFC");
		order.setPrice(1111011);
		KieSession dealerSession = sessions.get("dealerCode");
		dealerSession.insert(order);
		dealerSession.fireAllRules();
	}

}
