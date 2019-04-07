(ns kafka-example.admin
  (:import (org.apache.kafka.clients.admin AdminClient
                                           KafkaAdminClient
                                           NewTopic)))

(def admin-client (AdminClient/create {"bootstrap.servers" "localhost:9092"}))

(defn create-topic
  [client topic-name partitions replication]
  (.createTopics client
                 [(NewTopic. topic-name
                             (int partitions)
                             replication)]))


(defn describe-topic
  [client t]
  (let [r (.values (.describeTopics client [t]))]
    (.get (get r t))))
