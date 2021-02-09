FROM openjdk:11-jre

COPY target/universal/media-api-0.1.zip /tmp
RUN /usr/bin/unzip /tmp/media-api-0.1.zip
RUN mv /media-api-0.1 /usr/share/media-api
CMD ["/usr/share/media-api/bin/media-api"]
