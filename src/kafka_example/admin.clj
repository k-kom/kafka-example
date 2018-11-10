(ns kafka-example.admin
  (:import (org.apache.kafka.clients.admin AdminClient
                                           KafkaAdminClient
                                           NewTopic)))

(def admin-client (AdminClient/create {"bootstrap.servers" "localhost:9092"}))

(def t "clj-example")

(defn create-topic
  [client topic-name p replication]
  (.createTopics client [(NewTopic. t (int p) replication)]))


(defn describe-topic
  [client t]
  (let [r (.values (.describeTopics client [t]))]
    (.get (get r t))))
