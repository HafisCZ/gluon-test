package com.hal0160.tests.implementations;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import com.gluonhq.charm.down.Services;
import com.gluonhq.charm.down.plugins.*;
import com.hal0160.tests.Test;
import com.hal0160.util.Constants;

public class WritePrefs extends Test {
	
	private final String content;
	
	public WritePrefs (int iterations, String content, int loops) {
		super(iterations);
		this.content = content;
		this.divisions = loops;
	}
	
	@Override
	protected void test() throws Exception {
		for (int i = 0; i < this.divisions; i++) {
			Services.get(SettingsService.class).ifPresent(service -> {
				service.store(Constants.SAMPLE_KEY, this.content);
				service.remove(Constants.SAMPLE_KEY);
			});
		}
	}
	
}
