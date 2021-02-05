package com.gu.mediaservice.lib.elasticsearch

case class ElasticSearchConfig(alias: String, url: String, shards: Int, replicas: Int)
