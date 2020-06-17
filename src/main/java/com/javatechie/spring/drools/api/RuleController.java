package com.javatechie.spring.drools.api;

import com.javatechie.spring.drools.api.model.Order;
import org.kie.api.runtime.KieSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RuleController {

	private KieSession session;

	public RuleController(KieSession session) {
		this.session = session;
	}


	@GetMapping("/rule")
	public void fireRules() {
		Order order = new Order();
		order.setCardType("ICICI");
		order.setPrice(15001);
		order.setName("test");
		order.setDiscount(1);
		session.insert( order );

		session.fireAllRules();
	}

}
