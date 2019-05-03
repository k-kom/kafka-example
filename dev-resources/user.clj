(ns user
  (:require [kafka-example.config :as config]
            [kafka-example.producer :as pro]
            [kafka-example.stream.example :as streams])
  (:import (org.apache.kafka.clients.producer KafkaProducer
                                              ProducerRecord)
           (org.apache.kafka.clients.consumer KafkaConsumer)
           (org.apache.kafka.common.serialization StringSerializer
                                                  StringDeserializer)))

(def yelling-producer
  (KafkaProducer. (merge config/bootstrap-server
                         {"value.serializer" StringSerializer
                          "key.serializer"   StringSerializer})))

(defn yelling-consumer [topic]
  (doto (KafkaConsumer. (merge config/bootstrap-server
                               {"group.id"           "avg-rate-consumer"
                                "value.serializer"   StringSerializer
                                "key.serializer"     StringSerializer
                                "value.deserializer" StringDeserializer
                                "key.deserializer"   StringDeserializer}))
    (.subscribe [topic])))

(defn ->producer-record [topic v]
  (ProducerRecord. topic
                   (int 0)
                   (str v)
                   v))

;;(future
;;  (let [kf (streams/kafka-streams)]
;;    (.start kf)
;;    (Thread/sleep 35000)
;;    (println "Shutting down the Yelling APP now")
;;    (.close kf)))
;;
;;(pro/send-to-kafka yelling-producer (->producer-record "src-topic" "foo"))
;;(pro/send-to-kafka yelling-producer (->producer-record "src-topic" "bar"))