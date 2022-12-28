(ns f2c.infra.db
  (:require [clojure.string :as s]
            [f2c.infra.config :as config]
            [hikari-cp.core :as hikari]
            [migratus.core :as migratus]
            [honeyeql.db :as heql-db]
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
     :password password
     :connection-init-sql "SET TIME ZONE 'UTC';"}))

(defn- create-datasource []
  (-> (config/db-connection-string)
      (transform-database-url)
      (hikari/make-datasource)))

(mount/defstate datasource
  :start (create-datasource)
  :stop (hikari/close-datasource datasource))

(mount/defstate adapter
  :start (heql-db/initialize datasource))

(defn migration-config []
  {:store :database
   :db {:datasource datasource}})

(defn migrate []
  (mount/start #'config/root #'datasource)
  (try
    (migratus/migrate (migration-config))
    (finally
      (mount/stop #'datasource))))

(defn rollback []
  (mount/start #'config/root #'datasource)
  (try
    (migratus/rollback (migration-config))
    (finally
      (mount/stop #'datasource))))


(comment
  (migrate)
  (rollback)
  (migratus/create (migration-config) "item_unit_of_measures"))

