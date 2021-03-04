FROM openjdk:11-jre

COPY target/universal/auth-0.1.zip /tmp
RUN /usr/bin/unzip /tmp/auth-0.1.zip
RUN mv /auth-0.1 /usr/share/auth
CMD ["/usr/share/auth/bin/auth"]
