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
import uk.nhs.digital.cid.fidouaf.services.IAuthenticationService;
import uk.nhs.digital.cid.fidouaf.services.IRegistrationService;
import uk.nhs.digital.cid.fidouaf.services.NotaryImpl;
import uk.nhs.digital.cid.fidouaf.services.RegistrationService;
import uk.nhs.digital.cid.fidouaf.services.StorageImpl;

public class InjectorModule extends AbstractModule {

	@Override
	protected void configure() {

		// Guice bindings
		bind(Configuration.class).to(EnvironmentVariableConfiguration.class).asEagerSingleton();
		bind(Logger.class).to(DefaultLogger.class).asEagerSingleton();
		bind(StorageInterface.class).to(StorageImpl.class);
		bind(Notary.class).to(NotaryImpl.class);
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
	IRegistrationService provideRegistrationService(Provider<StorageInterface> storageProvider, Provider<Notary> notaryProvider) {
        return new RegistrationService(storageProvider.get(), notaryProvider.get());
    }
	
	@Provides
	IAuthenticationService provideAuthenticationService(Provider<StorageInterface> storageProvider, Provider<Notary> notaryProvider) {
        return new AuthenticationService(storageProvider.get(), notaryProvider.get());
    }	
}
