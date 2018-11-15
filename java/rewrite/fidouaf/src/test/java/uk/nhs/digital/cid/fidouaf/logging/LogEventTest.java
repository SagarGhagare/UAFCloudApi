package uk.nhs.digital.cid.fidouaf.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;

public class LogEventTest {
	private static final String TEST_FILE = "TEST FILE";
	private static final String TEST_CLASS = "TEST FILE";
	private static final String TEST_METHOD = "TEST FILE";
	private static final String TEST_SERVICE_NAME = "TEST FILE";
	private static final Date TEST_DATE = new DateTime(	1999,
														1,
														1,
														14,
														26,
														55,
														125,
														DateTimeZone.forOffsetHours(4)).toDate();
	private static final String TEST_DATE_STRING = "1999-01-01T10:26:55.125+0000";
	private static final Level TEST_LEVEL = Level.INFO;
	private static final String TEST_MESSAGE = "TEST FILE";
	private static final Object[] TEST_EMPTY_DATA = new Object[0];
	private static final Object[] TEST_POPULATED_DATA = new Object[2];
	private static final String TEST_CUSTOM_STRING_VALUE = "CUSTOM STRING";
	private static final Integer TEST_CUSTOM_NUMERIC_VALUE = 5;
	private static final String TEST_CORRELATION_ID = "TEST CORRELATION ID";

	private static final String EMPTY_OBJECT = "{}";
	private static final String EMPTY_ARRAY = "[]";
	private static final String QUOT = "\"";
	private static final String DATA_SEPARATOR = ":";

	private static final String KEY_FILE = "file";
	private static final String KEY_CLASS = "class";
	private static final String KEY_METHOD = "method";
	private static final String KEY_SERVICE_NAME = "serviceName";
	private static final String KEY_TIME = "time";
	private static final String KEY_LEVEL = "level";
	private static final String KEY_MESSAGE = "message";
	private static final String KEY_PAYLOAD = "payload";
	private static final String KEY_CORRELATION_ID = "correlationId";

	private ObjectMapper objectMapper = new ObjectMapper();

	@BeforeClass
	public static void configureTestData() {
		TEST_POPULATED_DATA[0] = TEST_CUSTOM_STRING_VALUE;
		TEST_POPULATED_DATA[1] = TEST_CUSTOM_NUMERIC_VALUE;
	}

	@Test
	public void nullSerialisation() throws JsonProcessingException {
		LogEvent logEvent = new LogEvent(null, null, null, null, null, null, null, null, null);
		final String output = objectMapper.writeValueAsString(logEvent);
		Assert.assertEquals(EMPTY_OBJECT, output);
	}

	@Test
	public void fullSerialisation() throws JsonProcessingException {
		LogEvent logEvent = new LogEvent(	TEST_FILE,
											TEST_CLASS,
											TEST_METHOD,
											TEST_SERVICE_NAME,
											TEST_DATE,
											TEST_LEVEL,
											TEST_MESSAGE,
											TEST_EMPTY_DATA,
											TEST_CORRELATION_ID);
		final String output = objectMapper.writeValueAsString(logEvent);
		checkData(output, KEY_FILE, TEST_FILE, true);
		checkData(output, KEY_CLASS, TEST_CLASS, true);
		checkData(output, KEY_METHOD, TEST_METHOD, true);
		checkData(output, KEY_SERVICE_NAME, TEST_SERVICE_NAME, true);
		checkData(output, KEY_TIME, TEST_DATE_STRING, true);
		checkData(output, KEY_LEVEL, TEST_LEVEL.name(), true);
		checkData(output, KEY_MESSAGE, TEST_MESSAGE, true);
		checkData(output, KEY_PAYLOAD, EMPTY_ARRAY, false);
		checkData(output, KEY_CORRELATION_ID, TEST_CORRELATION_ID, true);
	}

	@Test
	public void serialisationWithData() throws JsonProcessingException {
		LogEvent logEvent = new LogEvent(null, null, null, null, null, null, null, TEST_POPULATED_DATA, null);
		final String output = objectMapper.writeValueAsString(logEvent);
		checkPayload(output, TEST_CUSTOM_STRING_VALUE, true);
		checkPayload(output, TEST_CUSTOM_NUMERIC_VALUE, false);
	}

	private void checkPayload(String output, Object value, boolean isString) {
		StringBuilder expectedString = new StringBuilder();
		if(isString) {
			expectedString.append(QUOT).append(value).append(QUOT);
		} else {
			expectedString.append(value);
		}
		Assert.assertThat(output, CoreMatchers.containsString(expectedString.toString()));
	}

	private void checkData(String output, String field, Object value, boolean isString) {
		StringBuilder expectedString = new StringBuilder(QUOT).append(field).append(QUOT).append(DATA_SEPARATOR);
		if(isString) {
			expectedString.append(QUOT).append(value).append(QUOT);
		} else {
			expectedString.append(value);
		}
		Assert.assertThat(output, CoreMatchers.containsString(expectedString.toString()));
	}
}
