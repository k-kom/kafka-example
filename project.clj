(defproject kafka-example "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/data.generators "0.1.2"]
                 [org.apache.kafka/kafka-clients "2.1.0"]
                 [org.apache.kafka/kafka-streams "2.1.0"]
                 [com.taoensso/nippy "2.13.0"]]
  :main ^:skip-aot kafka-example.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
