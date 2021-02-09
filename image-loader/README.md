# image-loader

Image loader provides endpoints for submitting new media to the media service.

It is responsible for storing the submitted media in the image buckets and raising a notification (via the event queue) for the location and metadata of the media.


## Build

```
sbt image-loader/dist
```

```
docker build/image-loader
```


## Running from an image

```
docker run -it -p 9003:9003 -v /Users/tony/git/forks/grid/image-loader/application.conf:/usr/share/image-loader/conf/application.conf eu.gcr.io/grid-301122/image-loader:latest
```

Image loader will be visible on ```http://localhost:9003```


## Required configuration

Kinesis queue details
To consume update messages.

s3 bucket details
For handling delete image messages.

Image processors

Authentication

Key auth and Pandas for machine and user auth.


## Uploading an image

Update an image file and submit the optional original filename.

```
curl -H "X-Gu-Media-Key:my-api-key" -XPOST --data-binary "@my-image.jpg" --data filename=my-image.jpg http://localhost:9003/images?filename=my-image.jpg
```
