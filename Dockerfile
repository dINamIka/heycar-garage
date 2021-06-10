FROM adoptopenjdk/openjdk15:jdk-15.0.2_7-alpine-slim as build

WORKDIR /app_build
COPY . /app_build

RUN ./gradlew clean build -x test --no-daemon && \
    mkdir -p build/libs && \
    cd build/libs && \
    rm heycar*-plain.jar && \
    jar -xf *.jar


FROM adoptopenjdk/openjdk15:jre-15.0.2_7-alpine

VOLUME /tmp
ARG DEPENDENCY=/app_build/build/libs
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java","-cp","app:app/lib/*","car.hey.Application"]
