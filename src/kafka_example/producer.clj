(ns kafka-example.producer
  (:require [taoensso.nippy :as nippy])
  (:import (org.apache.kafka.clients.producer KafkaProducer
                                              ProducerRecord)))

(defn ->producer [p-config]
  (KafkaProducer. p-config))

(defn ->producer-record
  [topic part k v]
  (ProducerRecord. topic
                   (int part)
                   (nippy/freeze k)
                   (nippy/freeze v)))

(defn send-to-kafka
  [p r]
  (.send p r))

