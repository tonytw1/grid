# thrall

Thrall pulls update messages from a queue, interprets them and modifies the Elasticsearch index and image buckets accordingly.

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

The active Play config file is:
```
/usr/share/thrall/conf/application.conf
```

So a conf file can be past in like so:
```
docker run -it -v /Users/tony/git/forks/grid/thrall/application.conf:/usr/share/thrall/conf/application.conf eu.gcr.io/grid-301122/thrall:latest
```

The following configuration must be supplied:

Kinesis queue details
To consume update messages.

s3 bucket details
For handling delete image messages.

Elastic search details
For connecting to Elastic search and setting up the initial index if required.

indexed.image.sns.topic.arn
???
