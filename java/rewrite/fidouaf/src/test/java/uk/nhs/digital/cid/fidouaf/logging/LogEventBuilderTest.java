package uk.nhs.digital.cid.fidouaf.logging;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class LogEventBuilderTest {

	private static final String TEST_MESSAGE_1 = "TEST MESSAGE 1";
	private static final String TEST_CLASS_1 = "TEST CLASS 1";
	private static final String TEST_FILE_1 = "TEST FILE 1";
	private static final String TEST_METHOD_1 = "TEST METHOD 1";
	private static final String TEST_SERVICE_NAME_1 = "TEST SERVICE NAME 1";
	private static final Object[] TEST_DATA_1 = new Object[0];
	private static final Level TEST_LEVEL_1 = Level.INFO;
	private static final Date TEST_TIME_1 = new DateTime(1999, 1, 1, 12, 15, 30, 500, DateTimeZone.UTC).toDate();
	private static final String TEST_CORRELATION_ID_1 = "TEST CORRELATION ID 1";

	private static final String TEST_MESSAGE_2 = "TEST MESSAGE 2";
	private static final String TEST_CLASS_2 = "TEST CLASS 2";
	private static final String TEST_FILE_2 = "TEST FILE 2";
	private static final String TEST_METHOD_2 = "TEST METHOD 2";
	private static final String TEST_SERVICE_NAME_2 = "TEST SERVICE NAME 2";
	private static final Object[] TEST_DATA_2 = new Object[1];
	private static final Level TEST_LEVEL_2 = Level.WARN;
	private static final Date TEST_TIME_2 = new DateTime(1999, 2, 1, 12, 15, 30, 500, DateTimeZone.UTC).toDate();
	private static final String TEST_CORRELATION_ID_2 = "TEST CORRELATION ID 2";


	@Test
	public void nullsTest() {
		final LogEvent logEvent = new LogEventBuilder().build();
		Assert.assertNull(logEvent.message);
		Assert.assertNull(logEvent.clazz);
		Assert.assertNull(logEvent.file);
		Assert.assertNull(logEvent.method);
		Assert.assertNull(logEvent.serviceName);
		Assert.assertNull(logEvent.payload);
		Assert.assertNull(logEvent.level);
		Assert.assertNull(logEvent.time);
	}

	@Test
	public void singleTest() {
		final LogEvent logEvent = new LogEventBuilder()	.withMessage(TEST_MESSAGE_1)
														.withClass(TEST_CLASS_1)
														.withFile(TEST_FILE_1)
														.withMethod(TEST_METHOD_1)
														.withServiceName(TEST_SERVICE_NAME_1)
														.withPayload(TEST_DATA_1)
														.withLevel(TEST_LEVEL_1)
														.withTime(TEST_TIME_1)
														.withCorrelationId(TEST_CORRELATION_ID_1)
														.build();

		Assert.assertEquals(TEST_MESSAGE_1, logEvent.message);
		Assert.assertEquals(TEST_CLASS_1, logEvent.clazz);
		Assert.assertEquals(TEST_FILE_1, logEvent.file);
		Assert.assertEquals(TEST_METHOD_1, logEvent.method);
		Assert.assertEquals(TEST_SERVICE_NAME_1, logEvent.serviceName);
		Assert.assertArrayEquals(TEST_DATA_1, logEvent.payload);
		Assert.assertEquals(TEST_LEVEL_1, logEvent.level);
		Assert.assertEquals(TEST_TIME_1, logEvent.time);
		Assert.assertEquals(TEST_CORRELATION_ID_1, logEvent.correlationId);
	}

	@Test
	public void doubleTest() {
		final LogEvent logEvent = new LogEventBuilder()	.withMessage(TEST_MESSAGE_1)
														.withMessage(TEST_MESSAGE_2)
														.withClass(TEST_CLASS_1)
														.withClass(TEST_CLASS_2)
														.withFile(TEST_FILE_1)
														.withFile(TEST_FILE_2)
														.withMethod(TEST_METHOD_1)
														.withMethod(TEST_METHOD_2)
														.withServiceName(TEST_SERVICE_NAME_1)
														.withServiceName(TEST_SERVICE_NAME_2)
														.withPayload(TEST_DATA_1)
														.withPayload(TEST_DATA_2)
														.withLevel(TEST_LEVEL_1)
														.withLevel(TEST_LEVEL_2)
														.withTime(TEST_TIME_1)
														.withTime(TEST_TIME_2)
														   .withCorrelationId(TEST_CORRELATION_ID_1)
														   .withCorrelationId(TEST_CORRELATION_ID_2)
														.build();

		Assert.assertNotEquals(TEST_MESSAGE_1, logEvent.message);
		Assert.assertNotEquals(TEST_CLASS_1, logEvent.clazz);
		Assert.assertNotEquals(TEST_FILE_1, logEvent.file);
		Assert.assertNotEquals(TEST_METHOD_1, logEvent.method);
		Assert.assertNotEquals(TEST_SERVICE_NAME_1, logEvent.serviceName);
		Assert.assertNotEquals(TEST_DATA_1, logEvent.payload);
		Assert.assertNotEquals(TEST_LEVEL_1, logEvent.level);
		Assert.assertNotEquals(TEST_TIME_1, logEvent.time);
		Assert.assertNotEquals(TEST_CORRELATION_ID_1, logEvent.correlationId);

		Assert.assertEquals(TEST_MESSAGE_2, logEvent.message);
		Assert.assertEquals(TEST_CLASS_2, logEvent.clazz);
		Assert.assertEquals(TEST_FILE_2, logEvent.file);
		Assert.assertEquals(TEST_METHOD_2, logEvent.method);
		Assert.assertEquals(TEST_SERVICE_NAME_2, logEvent.serviceName);
		Assert.assertArrayEquals(TEST_DATA_2, logEvent.payload);
		Assert.assertEquals(TEST_LEVEL_2, logEvent.level);
		Assert.assertEquals(TEST_TIME_2, logEvent.time);
		Assert.assertEquals(TEST_CORRELATION_ID_2, logEvent.correlationId);
	}
}
