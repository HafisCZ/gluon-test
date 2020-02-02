package com.hal0160.tests.implementations;

import com.gluonhq.charm.down.Services;
import com.gluonhq.charm.down.plugins.*;
import com.hal0160.tests.Test;
import com.hal0160.util.Constants;

public class Audio extends Test {

	public Audio (int iterations) {
		super(iterations);
	}

	@Override
	protected void test() {
		Services.get(VideoService.class).ifPresent(service -> {
            service.setControlsVisible(true);
            service.getPlaylist().add(Constants.SAMPLE_AUDIO_FILE);
            service.play();
		});
	}

}
