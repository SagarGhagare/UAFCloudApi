build:
	rm -rf deployment-artifacts
	mkdir -p deployment-artifacts

	make build-java
	cp java/rewrite/fidouaf/target/fidouaf-1.0.jar deployment-artifacts/fidouaf-api.jar

build-java:
	cd java/rewrite/fido-uaf-core; mvn clean install
	cd java/rewrite/fidouaf; mvn clean package