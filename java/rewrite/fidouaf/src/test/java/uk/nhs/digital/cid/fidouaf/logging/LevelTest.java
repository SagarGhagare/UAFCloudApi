package uk.nhs.digital.cid.fidouaf.logging;

import org.junit.Assert;
import org.junit.Test;

public class LevelTest {
	private static final String INFO_NAME = "INFO";
	private static final String WARN_NAME = "WARN";
	private static final String ERROR_NAME = "ERROR";
	private static final String DEBUG_NAME = "DEBUG";

	@Test
	public void info() {
		Assert.assertEquals(INFO_NAME, Level.INFO.name());
	}

	@Test
	public void warn() {
		Assert.assertEquals(WARN_NAME, Level.WARN.name());
	}

	@Test
	public void error() {
		Assert.assertEquals(ERROR_NAME, Level.ERROR.name());
	}

	@Test
	public void debug() {
		Assert.assertEquals(DEBUG_NAME, Level.DEBUG.name());
	}
}
