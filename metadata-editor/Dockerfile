FROM openjdk:11-jre

COPY target/universal/metadata-editor-0.1.zip /tmp
RUN /usr/bin/unzip /tmp/metadata-editor-0.1.zip
RUN mv /metadata-editor-0.1 /usr/share/metadata-editor
CMD ["/usr/share/metadata-editor/bin/metadata-editor"]
