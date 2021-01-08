# thrall

Thralls pulls messages from an SQS queue, interprets them, and modifies an Elasticsearch index accordingly.

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
