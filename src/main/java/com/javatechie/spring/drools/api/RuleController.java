package com.javatechie.spring.drools.api;

import com.javatechie.spring.drools.api.model.Order;
import org.kie.api.runtime.KieSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RuleController {

	private Map<String,KieSession> sessions;

	public RuleController(Map<String,KieSession> sessions) {
		this.sessions = sessions;
	}


	@GetMapping("/rule")
	public void fireRules() {
		KieSession kieSession = sessions.get("dealerCode");
		Order order = new Order();
		order.setCardType("ICICI");
		order.setPrice(15001);
		order.setName("test");
		order.setDiscount(1);
		kieSession.insert( order );

		kieSession.fireAllRules();
	}

}
