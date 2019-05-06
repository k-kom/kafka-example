(ns kafka-example.stream.purchase-stream
  (:import (org.apache.kafka.streams.kstream KStream
                                             Consumed
                                             Produced
                                             ValueMapper
                                             Printed)
           (org.apache.kafka.streams StreamsBuilder
                                     KafkaStreams StreamsConfig)
           (org.apache.kafka.common.serialization Serdes)
           (org.apache.kafka.clients.consumer ConsumerConfig)
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
    (.put StreamsConfig/APPLICATION_ID_CONFIG "testing-stream-api")
    (.put StreamsConfig/BOOTSTRAP_SERVERS_CONFIG "localhost:9092")
    (.put ConsumerConfig/AUTO_OFFSET_RESET_CONFIG "latest")
    (.put StreamsConfig/REPLICATION_FACTOR_CONFIG (int 1))
    (.put StreamsConfig/CLIENT_ID_CONFIG "Example-Kafka-Streams-Job")
    (.put ConsumerConfig/GROUP_ID_CONFIG "streams-purchases")))

(def stream-config (StreamsConfig. properties))

(def builder (StreamsBuilder.))

(def string-serde (Serdes/String))

;; returns s with masked string
(defn- prepend-mask [s]
  (clojure.string/join "-"
                       (conj (vec (for [_ (range 3)]
                                    (apply str (repeat 4 "x"))))
                             s)))

;; value mappers
(def mask-credit-card-number
  (reify ValueMapper
    (apply [_ {:keys [credit-card-number]}]
      (let [raw-part (take 12 (clojure.string/replace credit-card-number #"-" ""))]
        (prepend-mask raw-part)))))

(def purchase->pattern
  (reify ValueMapper
    (apply [_ {:keys [zip-code item-purchased purchase-date price quantity]}]
      {:zip-code zip-code
       :item     item-purchased
       :date     purchase-date
       :amount   (* price quantity)})))

(def purchase->reward
  (reify ValueMapper
    (apply [_ {:keys [first-name last-name price quantity]}]
      (let [t (* price (double quantity))]
        {:customer-id    (str last-name "," first-name)
         :purchase-total t
         :reward-point   (int t)}))))

;; topic definitions
(def src-stream
  (.stream builder
           "source"
           (Consumed/with string-serde string-serde)))

(def transaction-stream
  (.. src-stream
    (mapValues mask-credit-card-number)
    (to "transactions"
        (Produced/with string-serde string-serde))))

(def pattern-stream
  (.. transaction-stream
    (mapValues purchase->pattern)
    (to "patterns"
        (Produced/with string-serde string-serde))))

(def reward-stream
  (.. purchase->reward
    (mapValues mask-credit-card-number)
    (to "rewards"
        (Produced/with string-serde string-serde))))

