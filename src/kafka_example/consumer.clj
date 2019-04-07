(ns kafka-example.consumer
  (:require [taoensso.nippy :as nippy])
  (:import (org.apache.kafka.clients.consumer KafkaConsumer)))

(defn ->consumer [c-config topic-name]
  (doto (KafkaConsumer. c-config)
                (.subscribe [topic-name])))

(defn consume [consumer]
  (-> (.poll consumer 100)
      first
      (.value)
      nippy/thaw))
