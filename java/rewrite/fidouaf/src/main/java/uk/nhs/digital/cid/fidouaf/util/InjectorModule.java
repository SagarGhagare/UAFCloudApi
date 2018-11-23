package uk.nhs.digital.cid.fidouaf.util;

import java.io.PrintStream;

import org.ebayopensource.fido.uaf.crypto.Notary;
import org.ebayopensource.fido.uaf.storage.StorageInterface;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import uk.nhs.digital.cid.fidouaf.libs.ApiGatewayResponseBuilder;
import uk.nhs.digital.cid.fidouaf.logging.DefaultLogger;
import uk.nhs.digital.cid.fidouaf.logging.Level;
import uk.nhs.digital.cid.fidouaf.logging.LogConfiguration;
import uk.nhs.digital.cid.fidouaf.logging.Logger;
import uk.nhs.digital.cid.fidouaf.services.AuthenticationService;
import uk.nhs.digital.cid.fidouaf.services.DeregRequestProcessor;
import uk.nhs.digital.cid.fidouaf.services.IAuthenticationService;
import uk.nhs.digital.cid.fidouaf.services.IDeregRequestProcessor;
import uk.nhs.digital.cid.fidouaf.services.IProcessResponse;
import uk.nhs.digital.cid.fidouaf.services.IRegistrationService;
import uk.nhs.digital.cid.fidouaf.services.ISecretHelper;
import uk.nhs.digital.cid.fidouaf.services.NotaryImpl;
import uk.nhs.digital.cid.fidouaf.services.ProcessResponse;
import uk.nhs.digital.cid.fidouaf.services.RegistrationService;
import uk.nhs.digital.cid.fidouaf.services.SecretHelper;
import uk.nhs.digital.cid.fidouaf.services.StorageImpl;

public class InjectorModule extends AbstractModule {

	@Override
	protected void configure() {

		// Guice bindings
		bind(Configuration.class).to(EnvironmentVariableConfiguration.class).asEagerSingleton();
		bind(Logger.class).to(DefaultLogger.class).asEagerSingleton();
	}

	@Provides
    ApiGatewayResponseBuilder provideApiGatewayResponseBuilder(Provider<Configuration> configProvider) {
        return new ApiGatewayResponseBuilder("*");
    }
	
	@Provides
    LogConfiguration provideLogConfiguration(Provider<Configuration> configProvider) {
        Configuration config = configProvider.get();
        return new LogConfiguration() {
            @Override
            public String getServiceName() {
                return "fidouaf";
            }

            @Override
            public Level getLogLevel() {
                return config.getLogLevel();
            }
        };
    }
	
	@Provides
    public PrintStream providePrintStream() {
        return System.out;
    }
	
	@Provides
    @Singleton
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        return objectMapper;
    }
	
	@Provides
	IRegistrationService provideRegistrationService(Provider<Configuration> configProvider, Provider<StorageInterface> storageProvider, Provider<Notary> notaryProvider, Provider<IProcessResponse> processResponseProvider, Provider<IDeregRequestProcessor> deregRequestProcessorProvider) {
        return new RegistrationService(storageProvider.get(), notaryProvider.get(), processResponseProvider.get(), deregRequestProcessorProvider.get(), configProvider.get());
    }
	
	@Provides
	IAuthenticationService provideAuthenticationService(Provider<StorageInterface> storageProvider, Provider<Notary> notaryProvider, Provider<IProcessResponse> processResponseProvider, Provider<Logger> loggerProvider) {
        return new AuthenticationService(storageProvider.get(), notaryProvider.get(), processResponseProvider.get(), loggerProvider.get());
    }
	
	@Provides
	ISecretHelper provideSecretHelper(Provider<Configuration> configProvider, Provider<Logger> loggerProvider) {
        return new SecretHelper(configProvider.get(), loggerProvider.get());
    }
	
	@Provides
	Notary provideNotaryImpl(Provider<Configuration> configProvider, Provider<Logger> loggerProvider, Provider<ISecretHelper> scretHelperProvider) {
        return new NotaryImpl(configProvider.get(), loggerProvider.get(), scretHelperProvider.get());
    }
	
	@Provides
	StorageInterface provideStorageImpl(Provider<Configuration> configProvider, Provider<Logger> loggerProvider) {
        return new StorageImpl(configProvider.get(), loggerProvider.get());
    }
	
	@Provides
	IProcessResponse provideProcessResponse(Provider<Configuration> configProvider, Provider<Logger> loggerProvider, Provider<StorageInterface> storageProvider, Provider<Notary> notaryProvider) {
        return new ProcessResponse(configProvider.get(), loggerProvider.get(), notaryProvider.get(), storageProvider.get());
    }
	
	@Provides
	IDeregRequestProcessor provideDeregRequestProcessor(Provider<StorageInterface> storageProvider, Provider<Logger> loggerProvider) {
        return new DeregRequestProcessor(storageProvider.get(), loggerProvider.get());
    }
}
