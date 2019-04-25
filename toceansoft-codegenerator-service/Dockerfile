FROM java:8
EXPOSE 80 8441

VOLUME /tmp
ADD toceanos-audio-service.jar /app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-jar","/app.jar"]
