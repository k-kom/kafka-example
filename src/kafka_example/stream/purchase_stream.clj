(ns kafka-example.stream.purchase-stream
  (:import (org.apache.kafka.streams.kstream KStream
                                             Consumed
                                             Produced
                                             ValueMapper
                                             Printed)
           (org.apache.kafka.streams StreamsBuilder
                                     KafkaStreams StreamsConfig)
           (org.apache.kafka.common.serialization Serdes)
           (java.util Properties)))

(comment
  SerDes "https://kafka.apache.org/10/documentation/streams/developer-guide/datatypes.html"

  " print makes debugging easier"
  patternKStream.print(Printed.<String, PurchasePattern>toSysOut().withLabel("patterns"));
  )

;; custom serdes の実装がよくわからない
;; わざわざ serdes を作る必要ある? json にして parse すればいいだけだと思いますが

;; preparation
(def properties
  (doto (Properties.)
    (.put StreamsConfig/APPLICATION_ID_CONFIG "testing_stream_api")
    (.put StreamsConfig/BOOTSTRAP_SERVERS_CONFIG "localhost:9092")))

(def stream-config (StreamsConfig. properties))

(def builder (StreamsBuilder.))

(def string-serde (Serdes/String))