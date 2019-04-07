(ns kafka-example.core
  (:require [kafka-example.config :as config]
            [kafka-example.admin :as admin]
            [kafka-example.consumer :as consumer]
            [kafka-example.producer :as producer])
  (:gen-class))

;;; https://kafka.apache.org/quickstart
;;; wget http://ftp.jaist.ac.jp/pub/apache/kafka/1.1.0/kafka_2.11-1.1.0.tgz
;;; tar -xzf kafka_2.11-1.1.0.tgz
;;; cd kafka_2.11-1.1.0
;;; bin/zookeeper-server-start.sh config/zookeeper.properties
;;; bin/kafka-server-start.sh config/server.properties
;;; bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic clj-example

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(def p (producer/->producer config/producer-config))

;;; create a consumer
(def c (consumer/->consumer config/consumer-config config/topic-name))

;;; create 2nd consumer with another group.id
(def c2 (consumer/->consumer (merge config/consumer-config
                                    {"group.id" "group-2"})
                             config/topic-name))

;;; send something
;;(producer/send-to-kafka p
;;                        (producer/->producer-record config/topic-name
;;                                                    1
;;                                                    1
;;                                                    {:name "john"}))

;;; each consumer can consume previous message
;;; because they have different group.id
;;; http://kafka.apache.org/documentation.html#intro_consumers

;;; (consumer/consume c)
;;; (consumer/consume c2)