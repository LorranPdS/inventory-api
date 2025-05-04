FROM maven:3.6.3-openjdk-17
WORKDIR /supermarket-api
COPY . .
RUN mvn clean install -DskipTests
EXPOSE 8081
CMD ["mvn", "spring-boot:run"]

# Run the command 'docker build -t supermarket-api .' in this directory.