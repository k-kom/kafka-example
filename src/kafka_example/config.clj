(ns kafka-example.config
  (:import (org.apache.kafka.common.serialization ByteArrayDeserializer
                                                  ByteArraySerializer)))

(def topic-name "my-topic")

(def bootstrap-server {"bootstrap.servers" "localhost:9092"})

(def producer-config
  (merge {"value.serializer" ByteArraySerializer
          "key.serializer"   ByteArraySerializer}
         bootstrap-server))

(def consumer-config
  (merge {"group.id"           "avg-rate-consumer"
          "auto.offset.reset"  "earliest"
          "enable.auto.commit" "false"
          "key.deserializer"   ByteArrayDeserializer
          "value.deserializer" ByteArrayDeserializer}
         bootstrap-server))

(def stream-config
  (merge {"application.id" "yelling-app-id"}
         bootstrap-server))