FROM openjdk:11-jre
COPY target/universal/image-loader-0.1.zip /tmp
RUN /usr/bin/unzip /tmp/image-loader-0.1.zip
RUN mv /image-loader-0.1 /usr/share/image-loader
CMD ["/usr/share/image-loader/bin/image-loader"]
