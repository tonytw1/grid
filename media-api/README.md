# media-api

## Image search

    GET /images

### Optional query string parameters

| Key        | Description                               | Default          |
|------------|-------------------------------------------|------------------|
| q          | Text search query                         | [empty]
| offset     | Start offset for results                  | 0
| length     | Maximum number of results                 | 10
| orderBy    | Field for ordering (prepend '-' for DESC) | -uploadTime
| since      | Search images uploaded since this time    | [no lower bound]
| until      | Search images uploaded before this time   | [no upper bound]

Date-time values (e.g. since) can be provided in ISO format (no milliseconds), e.g. `2013-10-24T11:09:38Z` (i.e.
the same format as date-times in the documents). They can also be provided as relative durations, e.g. `7.days`.

### Example

http://media-ser-mediaapi-1uzj4tw8g9lmy-1465883965.eu-west-1.elb.amazonaws.com/images?q=horse&since=2.weeks

See the [routes file](https://github.com/guardian/media-service/blob/master/media-api/conf/routes) for more API
"documentation".


## Build

```
sbt media-api/dist
```

```
docker build media-api/
```

## Required configuration


### Configuration the Quota Store

Requires it's own s3 bucket
```
s3.config.bucket
```

[
  {agency: count}

]

## Running from an image

```
docker run -it -p 9001:9001 -v /Users/tony/git/forks/grid/media-api/application.conf:/usr/share/media-api/conf/application.conf eu.gcr.io/grid-301122/media-api:latest
```

Media API will be visible on ```http://localhost:9001```


## Example requests

### List images

```
curl -H "X-Gu-Media-Key:my-api-key" http://localhost:9001/images
```
