# thrall

Thralls pulls update messages from a queue, interprets them, and modifies an Elasticsearch index accordingly.

## Build

Thrall is packaged using the Play framework zip file format.
From the root of the grid checkout

```
sbt thrall/dist
```

This produces this zip file:

```
thrall/target/universal/thrall-0.1.zip
```

This archive should extracted to /usr/share/thrall


## Running from an image

```
docker run -it eu.gcr.io/grid-301122/thrall:latest
```

The following configuration must be supplied:

Oops, cannot start the server.
java.lang.RuntimeException: Required string configuration property missing: thrall.kinesis.stream.name


