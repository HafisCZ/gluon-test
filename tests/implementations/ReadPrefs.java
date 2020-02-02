package com.hal0160.tests.implementations;

import com.gluonhq.charm.down.Services;
import com.hal0160.tests.Test;
import com.hal0160.util.Constants;
import com.gluonhq.charm.down.plugins.*;

public class ReadPrefs extends Test {

	private final String content;
	
	public ReadPrefs (int iterations, String content, int loops) {
		super(iterations);
		this.content = content;
		this.divisions = loops;
	}
	
	@Override
	protected void prepare() throws Exception {
		Services.get(SettingsService.class).ifPresent(service -> {
			service.store(Constants.SAMPLE_KEY, this.content);
			service.remove(Constants.SAMPLE_KEY);
		});
	}
	
	@Override
	protected void test() throws Exception {
		for (int i = 0; i < this.divisions; i++) {
			Services.get(SettingsService.class).ifPresent(service -> {
				String result = service.retrieve(Constants.SAMPLE_KEY);
			});
		}
	}

}
