(ns f2c.infra.db
  (:require [clojure.string :as s]
            [f2c.infra.config :as config]
            [hikari-cp.core :as hikari]
            [migratus.core :as migratus]
            [mount.core :as mount])
  (:import [java.net URI]))

(defn- transform-database-url [connection-string]
  (let [db-uri (URI. connection-string)
        [username password] (s/split (. db-uri getUserInfo) #":")]
    {:jdbc-url (format "jdbc:postgresql://%s%s%s"
                       (.getHost db-uri)
                       (let [port (.getPort db-uri)]
                         (if (not= -1 port)
                           (str ":" port)
                           ""))
                       (.getPath db-uri))
     :username username
     :password password}))

(defn- create-datasource []
  (-> (config/db-connection-string)
      (transform-database-url)
      (hikari/make-datasource)))

(mount/defstate datasource
  :start (create-datasource)
  :stop (hikari/close-datasource datasource))

(defn migration-config []
  {:store :database
   :db {:datasource datasource}})

(comment 
  (migratus/create (migration-config) "initial-schema")
  )

