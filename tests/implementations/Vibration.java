package com.hal0160.tests.implementations;

import com.gluonhq.charm.down.Services;
import com.gluonhq.charm.down.plugins.*;
import com.hal0160.tests.Test;

public class Vibration extends Test {

	public Vibration (int iterations) {
		super(iterations);
	}
	
	@Override
	protected void test () {
		Services.get(VibrationService.class).ifPresent(service -> {
			service.vibrate();
		});
	}
	
}
