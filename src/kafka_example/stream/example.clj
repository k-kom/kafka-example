(ns kafka-example.stream.example
  (:require [kafka-example.config :as config])
  (:import (org.apache.kafka.streams.kstream KStream
                                             Consumed
                                             Produced
                                             ValueMapper
                                             Printed)
           (org.apache.kafka.streams StreamsBuilder
                                     KafkaStreams StreamsConfig)
           (org.apache.kafka.common.serialization Serdes)
           (java.util Properties)))

;; preparation
(def properties
  (doto (Properties.)
    (.put StreamsConfig/APPLICATION_ID_CONFIG "yelling_app_id")
    (.put StreamsConfig/BOOTSTRAP_SERVERS_CONFIG "localhost:9092")))

(def stream-config (StreamsConfig. properties))

(def builder (StreamsBuilder.))

(def string-serde (Serdes/String))

(def upper-case
  (reify ValueMapper
    (apply [_ v] (clojure.string/upper-case v))))

;; source topic definition
(def simple-first-stream (.stream builder
                                  "src-topic"
                                  (Consumed/with string-serde string-serde)))
;; map value
(def uppercase-stream (.mapValues simple-first-stream
                                  upper-case))

(defn log-me [s]
  (.print s (.withLabel (Printed/toFile "src-log") "src-topic")))

;; sink
(def sink (.to uppercase-stream
               "out-topic"
               (Produced/with string-serde string-serde)))

(defn kafka-streams []
  (log-me uppercase-stream)
  (KafkaStreams. (.build builder) stream-config))
