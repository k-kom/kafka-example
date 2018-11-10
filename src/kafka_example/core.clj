(ns kafka-example.core
  (:require [taoensso.nippy :as nippy])
  (:import (org.apache.kafka.clients.producer KafkaProducer
                                              ProducerRecord)
           (org.apache.kafka.clients.consumer KafkaConsumer)
           (org.apache.kafka.common.serialization ByteArraySerializer
                                                  ByteArrayDeserializer))
  (:gen-class))

;;; https://kafka.apache.org/quickstart
;;; wget http://ftp.jaist.ac.jp/pub/apache/kafka/1.1.0/kafka_2.11-1.1.0.tgz
;;; tar -xzf kafka_2.11-1.1.0.tgz
;;; cd kafka_2.11-1.1.0
;;; bin/zookeeper-server-start.sh config/zookeeper.properties
;;; bin/kafka-server-start.sh config/server.properties
;;; bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic clj-example

(def p-cfg {"value.serializer" ByteArraySerializer
            "key.serializer" ByteArraySerializer
            "bootstrap.servers" "localhost:9092"})

(def producer (KafkaProducer. p-cfg))

(def c-cfg
  {"bootstrap.servers" "localhost:9092"
   "group.id" "avg-rate-consumer"
   "auto.offset.reset" "earliest"
   "enable.auto.commit" "false"
   "key.deserializer" ByteArrayDeserializer
   "value.deserializer" ByteArrayDeserializer})

(def consumer (doto (KafkaConsumer. c-cfg)
                (.subscribe ["clj-example"])))

(defn ->producer-record
  [topic part k v]
  (ProducerRecord. topic (int part) k (nippy/freeze v)))

(defn send-to-kafka
  [p r]
  (.send p r))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
