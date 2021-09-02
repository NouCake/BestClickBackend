FROM openjdk

RUN echo $JAVA_HOME

COPY . /data/spring

WORKDIR /data/spring
RUN ./gradlew build

ENTRYPOINT ["java", "-jar", "./build/libs/BestClick-1.0.0-RELEASE.jar"]