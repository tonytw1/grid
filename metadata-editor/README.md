# metadata-editor

## Required config

thrall.kinesis.stream.name

Edits dynamo table
dynamo.table.edits


An SQS queue which don't know the propose of yet

indexed.images.sqs.queue.url

authentication



## Running from an image

```
docker run -it -p 9007:9007 -v /Users/tony/git/forks/grid/metadata-editor/application.conf:/usr/share/metadata-editor/conf/application.conf eu.gcr.io/grid-301122/metadata-editor:latest
```

Metadata Editor will be visible on ```http://localhost:9007```
