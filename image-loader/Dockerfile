FROM openjdk:11-jre

USER root
RUN apt-get update
RUN apt-get install -y apt-utils
RUN apt-get upgrade -y
RUN apt-get install -y graphicsmagick
RUN apt-get install -y graphicsmagick-imagemagick-compat
RUN apt-get install -y pngquant
RUN apt-get install -y libimage-exiftool-perl

COPY target/universal/image-loader-0.1.zip /tmp
RUN /usr/bin/unzip /tmp/image-loader-0.1.zip
RUN mv /image-loader-0.1 /usr/share/image-loader
CMD ["/usr/share/image-loader/bin/image-loader"]

COPY cmyk.icc /usr/share/image-loader
COPY facebook-TINYsRGB_c2.icc /usr/share/image-loader
COPY grayscale.icc /usr/share/image-loader
COPY srgb.icc /usr/share/image-loader
