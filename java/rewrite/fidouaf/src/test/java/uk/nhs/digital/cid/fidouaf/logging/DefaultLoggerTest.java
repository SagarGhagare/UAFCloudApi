package uk.nhs.digital.cid.fidouaf.logging;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static uk.nhs.digital.cid.fidouaf.logging.Level.INFO;
import static uk.nhs.digital.cid.fidouaf.logging.Level.WARN;
import static uk.nhs.digital.cid.fidouaf.logging.Level.ERROR;

@RunWith(MockitoJUnitRunner.class)
public class DefaultLoggerTest {
	// Metric Test Data
	private static final String TEST_EVENT = "TEST_EVENT";
	private static final String TEST_DATA_KEY = "TEST_DATA_KEY";
	private static final String TEST_DATA_VALUE = "TEST_DATA_VALUE";
	private static final Map<String, String> TEST_DATA = new HashMap<>();
	private static final ArgumentCaptor<String> LOG_MESSAGE_CAPTOR = ArgumentCaptor.forClass(String.class);
	private static final Pattern TEST_METRIC_PATTERN = Pattern.compile("^([^ =]+=[^ =]+)( [^ =]+=[^ =]+)*$");

	private static final Level TEST_LOG_LEVEL = Level.INFO;
	private static final String TEST_LOG_MESSAGE = "TEST LOG MESSAGE";
	private static final ArgumentCaptor<LogEvent> LOG_EVENT_ARGUMENT_CAPTOR = ArgumentCaptor.forClass(LogEvent.class);
	private static final String TEST_SERIALISE = "TEST SERIALISE";
	private static final String TEST_EXCEPTION_MESSAGE = "TEST EXCEPTION MESSAGE";
	private static final Object TEST_PAYLOAD = new Object();
	private static final String TEST_DEFAULT_CORRELATION_ID = "";
	private static final String TEST_CORRELATION_ID_1 = "TEST CORRELATION ID 1";
	private static final String TEST_CORRELATION_ID_2 = "TEST CORRELATION ID 2";

	// Common Test Data
	private static final String TEST_SERVICE_NAME = "TEST SERVICE NAME";

	@Mock
	private PrintStream printStream;

	@Mock
	private ObjectMapper objectMapper;

	@Mock
	private JsonProcessingException jsonProcessingException;

	@Mock
	private LogConfiguration logConfiguration;

	private DefaultLogger logger;

	@BeforeClass
	public static void setUpData() {
		TEST_DATA.put(TEST_DATA_KEY, TEST_DATA_VALUE);
	}

	@Before
	public void setUp() {
		Mockito.doReturn(TEST_SERVICE_NAME).when(logConfiguration).getServiceName();
		Mockito.doReturn(TEST_LOG_LEVEL).when(logConfiguration).getLogLevel();
		logger = new DefaultLogger(printStream, objectMapper, logConfiguration);
	}

	@Test
	public void testCorrelationWithNoneSet()
			throws JsonProcessingException {
		testCorrelationId(TEST_DEFAULT_CORRELATION_ID);
	}

	@Test
	public void testCorrelationWithOneSet()
			throws JsonProcessingException {
		logger.setCorrelationId(TEST_CORRELATION_ID_1);
		testCorrelationId(TEST_CORRELATION_ID_1);
		logger.unsetCorrelationId();
		testCorrelationId(TEST_DEFAULT_CORRELATION_ID);
	}

	@Test
	public void testCorrelationWithTwoSet()
			throws JsonProcessingException {
		logger.setCorrelationId(TEST_CORRELATION_ID_2);
		testCorrelationId(TEST_CORRELATION_ID_2);
		logger.setCorrelationId(TEST_CORRELATION_ID_1);
		testCorrelationId(TEST_CORRELATION_ID_1);
		logger.unsetCorrelationId();
		testCorrelationId(TEST_DEFAULT_CORRELATION_ID);
	}

	@Test
	public void debugNonLoggedWithoutData() {
		logger.debug("UNUSED_LOG_MESSAGE");
		Mockito.verifyNoMoreInteractions(objectMapper, printStream);
	}

	@Test
	public void debugNonLoggedWithNullData() {
		logger.debug("UNUSED_LOG_MESSAGE");
		Mockito.verifyNoMoreInteractions(objectMapper, printStream);
	}

	@Test
	public void debugNonLoggedWithData() {
		logger.debug("UNUSED_LOG_MESSAGE", TEST_PAYLOAD);
		Mockito.verifyNoMoreInteractions(objectMapper, printStream);
	}

	@Test
	public void debugNonLoggedWithOnlyData() {
		logger.debug(TEST_PAYLOAD);
		Mockito.verifyNoMoreInteractions(objectMapper, printStream);
	}

	@Test
	public void infoSuccessWithoutData() throws JsonProcessingException {
		diagnosticSuccessWithoutData(s -> logger.info(s), INFO);
	}

	@Test
	public void infoSuccessWithEmptyData() throws JsonProcessingException {
		diagnosticSuccessWithEmptyData((s, o) -> logger.info(s, o), INFO);
	}

	@Test
	public void infoSuccessWithFullData() throws JsonProcessingException {
		diagnosticSuccessWithFullData((s, o) -> logger.info(s, o), INFO);
	}

	@Test
	public void infoSuccessWithOnlyData() throws JsonProcessingException {
		diagnosticSuccessWithOnlyData(o -> logger.info(o), INFO);
	}

	@Test
	public void infoErrorWithoutData() throws JsonProcessingException {
		diagnosticErrorWithoutData(s -> logger.info(s));
	}

	@Test
	public void infoErrorWithData() throws JsonProcessingException {
		diagnosticErrorWithData((s, o) -> logger.info(s, o));
	}

	@Test
	public void warnSuccessWithoutData() throws JsonProcessingException {
		diagnosticSuccessWithoutData(s -> logger.warn(s), WARN);
	}

	@Test
	public void warnSuccessWithEmptyData() throws JsonProcessingException {
		diagnosticSuccessWithEmptyData((s, o) -> logger.warn(s, o), WARN);
	}

	@Test
	public void warnSuccessWithFullData() throws JsonProcessingException {
		diagnosticSuccessWithFullData((s, o) -> logger.warn(s, o), WARN);
	}

	@Test
	public void warnSuccessWithOnlyData() throws JsonProcessingException {
		diagnosticSuccessWithOnlyData(o -> logger.warn(o), WARN);
	}

	@Test
	public void warnErrorWithoutData() throws JsonProcessingException {
		diagnosticErrorWithoutData(s -> logger.warn(s));
	}

	@Test
	public void warnErrorWithData() throws JsonProcessingException {
		diagnosticErrorWithData((s, o) -> logger.warn(s, o));
	}

	@Test
	public void errorSuccessWithoutData() throws JsonProcessingException {
		diagnosticSuccessWithoutData(s -> logger.error(s), ERROR);
	}

	@Test
	public void errorSuccessWithEmptyData() throws JsonProcessingException {
		diagnosticSuccessWithEmptyData((s, o) -> logger.error(s, o), ERROR);
	}

	@Test
	public void errorSuccessWithFullData() throws JsonProcessingException {
		diagnosticSuccessWithFullData((s, o) -> logger.error(s, o), ERROR);
	}

	@Test
	public void errorSuccessWithOnlyData() throws JsonProcessingException {
		diagnosticSuccessWithOnlyData(o -> logger.error(o), ERROR);
	}

	@Test
	public void errorErrorWithoutData() throws JsonProcessingException {
		diagnosticErrorWithoutData(s -> logger.error(s));
	}

	@Test
	public void errorErrorWithData() throws JsonProcessingException {
		diagnosticErrorWithData((s, o) -> logger.error(s, o));
	}

	@Test
	public void metricNoData() {
		Mockito.doNothing().when(printStream).println(LOG_MESSAGE_CAPTOR.capture());
		logger.event(TEST_EVENT);
		Mockito.verify(printStream).println(ArgumentMatchers.matches(TEST_METRIC_PATTERN));
		Assert.assertThat(LOG_MESSAGE_CAPTOR.getValue(), CoreMatchers.containsString(TEST_EVENT));
	}

	@Test
	public void metricNullData() {
		Mockito.doNothing().when(printStream).println(LOG_MESSAGE_CAPTOR.capture());
		logger.event(TEST_EVENT, null);
		Mockito.verify(printStream).println(ArgumentMatchers.matches(TEST_METRIC_PATTERN));
		Assert.assertThat(LOG_MESSAGE_CAPTOR.getValue(), CoreMatchers.containsString(TEST_EVENT));
	}

	@Test
	public void metricEmptyData() {
		Mockito.doNothing().when(printStream).println(LOG_MESSAGE_CAPTOR.capture());
		logger.event(TEST_EVENT, Collections.emptyMap());
		Mockito.verify(printStream).println(ArgumentMatchers.matches(TEST_METRIC_PATTERN));
		Assert.assertThat(LOG_MESSAGE_CAPTOR.getValue(), CoreMatchers.containsString(TEST_EVENT));
	}

	@Test
	public void metricBasicData() {
		Mockito.doNothing().when(printStream).println(LOG_MESSAGE_CAPTOR.capture());
		logger.event(TEST_EVENT, TEST_DATA);
		Mockito.verify(printStream).println(ArgumentMatchers.matches(TEST_METRIC_PATTERN));
		Assert.assertThat(LOG_MESSAGE_CAPTOR.getValue(), CoreMatchers.containsString(TEST_EVENT));
		Assert.assertThat(LOG_MESSAGE_CAPTOR.getValue(), CoreMatchers.containsString(TEST_DATA_KEY));
		Assert.assertThat(LOG_MESSAGE_CAPTOR.getValue(), CoreMatchers.containsString(TEST_DATA_VALUE));
	}

	private void testCorrelationId(String correlationId)
			throws JsonProcessingException {
		Mockito.doReturn(TEST_SERIALISE).when(objectMapper).writeValueAsString(LOG_EVENT_ARGUMENT_CAPTOR.capture());
		Mockito.doNothing().when(printStream).println(TEST_SERIALISE);
		logger.info();
		Mockito.verify(printStream).println(TEST_SERIALISE);
		Mockito.verify(objectMapper).writeValueAsString(Mockito.any(LogEvent.class));
		Mockito.verifyNoMoreInteractions(printStream, objectMapper);
		Mockito.reset(printStream, objectMapper);
		LogEvent logEvent = LOG_EVENT_ARGUMENT_CAPTOR.getValue();
		Assert.assertEquals(INFO, logEvent.level);
		Assert.assertEquals(TEST_SERVICE_NAME, logEvent.serviceName);
		Assert.assertThat(logEvent.method, CoreMatchers.containsString("testCorrelationId"));
		Assert.assertEquals(this.getClass().getName(), logEvent.clazz);
		Assert.assertEquals(correlationId, logEvent.correlationId);
	}

	private void diagnosticSuccessWithFullData(
												BiConsumer<String, Object> loggerCall, Level level
	) throws JsonProcessingException {
		Mockito.doReturn(TEST_SERIALISE).when(objectMapper).writeValueAsString(LOG_EVENT_ARGUMENT_CAPTOR.capture());
		Mockito.doNothing().when(printStream).println(TEST_SERIALISE);
		loggerCall.accept(TEST_LOG_MESSAGE, TEST_PAYLOAD);
		Mockito.verify(printStream).println(TEST_SERIALISE);
		Mockito.verify(objectMapper).writeValueAsString(Mockito.any(LogEvent.class));
		Mockito.verifyNoMoreInteractions(printStream, objectMapper);
		LogEvent logEvent = LOG_EVENT_ARGUMENT_CAPTOR.getValue();
		Assert.assertEquals(TEST_LOG_MESSAGE, logEvent.message);
		Assert.assertEquals(level, logEvent.level);
		Assert.assertThat(Arrays.asList(logEvent.payload), CoreMatchers.hasItem(TEST_PAYLOAD));
		Assert.assertEquals(TEST_SERVICE_NAME, logEvent.serviceName);
		//Assert.assertThat(logEvent.method, CoreMatchers.containsString("SuccessWithFullData"));
		Assert.assertEquals(this.getClass().getName(), logEvent.clazz);
	}

	private void diagnosticSuccessWithoutData(Consumer<String> logMethod, Level level) throws JsonProcessingException {
		Mockito.doReturn(TEST_SERIALISE).when(objectMapper).writeValueAsString(LOG_EVENT_ARGUMENT_CAPTOR.capture());
		Mockito.doNothing().when(printStream).println(TEST_SERIALISE);
		logMethod.accept(TEST_LOG_MESSAGE);
		Mockito.verify(printStream).println(TEST_SERIALISE);
		Mockito.verify(objectMapper).writeValueAsString(Mockito.any(LogEvent.class));
		Mockito.verifyNoMoreInteractions(printStream, objectMapper);
		LogEvent logEvent = LOG_EVENT_ARGUMENT_CAPTOR.getValue();
		Assert.assertEquals(TEST_LOG_MESSAGE, logEvent.message);
		Assert.assertEquals(level, logEvent.level);
		Assert.assertNotNull(logEvent.payload);
		Assert.assertEquals(0, logEvent.payload.length);
		Assert.assertEquals(TEST_SERVICE_NAME, logEvent.serviceName);
		//Assert.assertThat(logEvent.method, CoreMatchers.containsString("SuccessWithoutData"));
		Assert.assertEquals(this.getClass().getName(), logEvent.clazz);
	}

	private void diagnosticSuccessWithEmptyData(
												BiConsumer<String, Object> logMethod, Level level
	) throws JsonProcessingException {
		Mockito.doReturn(TEST_SERIALISE).when(objectMapper).writeValueAsString(LOG_EVENT_ARGUMENT_CAPTOR.capture());
		Mockito.doNothing().when(printStream).println(TEST_SERIALISE);
		logMethod.accept(TEST_LOG_MESSAGE, null);
		Mockito.verify(printStream).println(TEST_SERIALISE);
		Mockito.verify(objectMapper).writeValueAsString(Mockito.any(LogEvent.class));
		Mockito.verifyNoMoreInteractions(printStream, objectMapper);
		LogEvent logEvent = LOG_EVENT_ARGUMENT_CAPTOR.getValue();
		Assert.assertEquals(TEST_LOG_MESSAGE, logEvent.message);
		Assert.assertEquals(level, logEvent.level);
		Assert.assertNotNull(logEvent.payload);
		Assert.assertEquals(1, logEvent.payload.length);
		Assert.assertNull(logEvent.payload[0]);
		Assert.assertEquals(TEST_SERVICE_NAME, logEvent.serviceName);
		//Assert.assertThat(logEvent.method, CoreMatchers.containsString("SuccessWithEmptyData"));
		Assert.assertEquals(this.getClass().getName(), logEvent.clazz);
	}

	private void diagnosticSuccessWithOnlyData(Consumer<Object> logMethod, Level level) throws JsonProcessingException {
		Mockito.doReturn(TEST_SERIALISE).when(objectMapper).writeValueAsString(LOG_EVENT_ARGUMENT_CAPTOR.capture());
		Mockito.doNothing().when(printStream).println(TEST_SERIALISE);
		logMethod.accept(TEST_PAYLOAD);
		Mockito.verify(printStream).println(TEST_SERIALISE);
		Mockito.verify(objectMapper).writeValueAsString(Mockito.any(LogEvent.class));
		Mockito.verifyNoMoreInteractions(printStream, objectMapper);
		LogEvent logEvent = LOG_EVENT_ARGUMENT_CAPTOR.getValue();
		Assert.assertEquals(level, logEvent.level);
		Assert.assertThat(Arrays.asList(logEvent.payload), CoreMatchers.hasItem(TEST_PAYLOAD));
		Assert.assertEquals(TEST_SERVICE_NAME, logEvent.serviceName);
		//Assert.assertThat(logEvent.method, CoreMatchers.containsString("SuccessWithOnlyData"));
		Assert.assertEquals(this.getClass().getName(), logEvent.clazz);
	}

	private void diagnosticErrorWithoutData(Consumer<String> logMethod) throws JsonProcessingException {
		Mockito.doReturn(TEST_EXCEPTION_MESSAGE).when(jsonProcessingException).getMessage();
		Mockito.doNothing().when(jsonProcessingException).printStackTrace(printStream);
		Mockito.doThrow(jsonProcessingException).when(objectMapper).writeValueAsString(Mockito.any(LogEvent.class));
		Mockito.doNothing().when(printStream).println(TEST_EXCEPTION_MESSAGE);
		logMethod.accept(TEST_LOG_MESSAGE);
		Mockito.verify(printStream).println(TEST_EXCEPTION_MESSAGE);
		Mockito.verify(objectMapper).writeValueAsString(Mockito.any(LogEvent.class));
		Mockito.verify(jsonProcessingException).getMessage();
		Mockito.verify(jsonProcessingException).printStackTrace(printStream);
		Mockito.verifyNoMoreInteractions(printStream, objectMapper, jsonProcessingException);
	}

	private void diagnosticErrorWithData(BiConsumer<String, Object> logMethod) throws JsonProcessingException {
		Mockito.doReturn(TEST_EXCEPTION_MESSAGE).when(jsonProcessingException).getMessage();
		Mockito.doNothing().when(jsonProcessingException).printStackTrace(printStream);
		Mockito.doThrow(jsonProcessingException).when(objectMapper).writeValueAsString(Mockito.any(LogEvent.class));
		Mockito.doNothing().when(printStream).println(TEST_EXCEPTION_MESSAGE);
		logMethod.accept(TEST_LOG_MESSAGE, TEST_PAYLOAD);
		Mockito.verify(printStream).println(TEST_EXCEPTION_MESSAGE);
		Mockito.verify(objectMapper).writeValueAsString(Mockito.any(LogEvent.class));
		Mockito.verify(jsonProcessingException).getMessage();
		Mockito.verify(jsonProcessingException).printStackTrace(printStream);
		Mockito.verifyNoMoreInteractions(printStream, objectMapper, jsonProcessingException);
	}
}
