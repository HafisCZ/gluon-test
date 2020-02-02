package com.hal0160.tests.implementations;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import com.gluonhq.charm.down.Services;
import com.gluonhq.charm.down.plugins.StorageService;
import com.hal0160.tests.Test;
import com.hal0160.util.Constants;

public class WriteFile extends Test {

	private final String content;
	
	public WriteFile (int iterations, String content, int loops) {
		super(iterations);
		this.content = content;
		this.divisions = loops;
	}
	
	@Override
	protected void test() throws Exception {
		for (int i = 0; i < this.divisions; i++) {
			// Get file
			File directory = Services.get(StorageService.class).map(service -> service.getPrivateStorage().get()).get();
			File file = new File(directory, Constants.SAMPLE_FILE);
			
			// Write data to file
			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file));
			writer.write(this.content);
			writer.close();
			
			// Remove the file
			file.delete();
		}
	}

}
