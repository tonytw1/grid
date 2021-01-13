FROM openjdk:8-jre
COPY target/universal/thrall-0.1.zip /tmp
RUN /usr/bin/unzip /tmp/thrall-0.1.zip
RUN mv /thrall-0.1 /usr/share/thrall
CMD ["/usr/share/thrall# ./bin/thrall"]


