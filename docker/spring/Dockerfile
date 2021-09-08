FROM openjdk

COPY ./build/libs /data/spring

WORKDIR /data/spring

ENTRYPOINT ["java", "-jar", "./BestClick-1.0.0-RELEASE.jar"]