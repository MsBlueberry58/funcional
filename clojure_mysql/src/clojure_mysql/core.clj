;;;;;;;;;; Namespace e imports ;;;;;;;;;;
(ns clojure-mysql.core
  (:require
   [clojure.java.jdbc :as jdbc]
   [io.pedestal.http :as http]
   [io.pedestal.http.route :as route]))

;;;;;;;;;; Globais ;;;;;;;;;;
(def host "localhost")
(def port 8899)

(let [db-host "localhost"
      db-port 5432 
      db-name "bd"] 

(def db {:classname "com.mysql.cj.jdbc.Driver"
         :subprotocol "mysql"
         :subname (str "//" db-host ";" db-port "/" db-name "?useTimezone=true&serverTimezone=UTC")
         :user "postgres"
         :password "root"}))

(def selectQuery (jdbc/query db ["SELECT * FROM gerente"]))
(def insertQuery (jdbc/query db ["INSERT VALUES ('234', 'Bruna', 'baixo', '22') INTO gerente"]))
(def updateQuery (jdbc/query db ["UPDATE gerente SET nomegerente = 'Bruno' WHERE nomegerente = 'Bruna'"]))
(def deleteQuery (jdbc/query db ["DELETE * FROM gerente WHERE idgerente = "]))

;;;;;;;;;; Handlers das rotas ;;;;;;;;;;
(defn index-handler [request]
  {:status 200
   :body {:port 
          :host}})

(defn create-handler [request]
  (println insertQuery)
  {:status 200
   :body "Foi criado um gerente"})

(defn read-handler [request]
  {:status 200
   :body (println selectQuery)})

(defn update-handler [request] 
  (println updateQuery)
  {:status 200
   :body "O gerente foi atualizado"})

(defn delete-handler [request] 
  (println deleteQuery)
  {:status 200
   :body "O gerente foi deletado"})

;;;;;;;;;; Setup API e servidor ;;;;;;;;;;
(def routes
  (route/expand-routes
   #{["/index" :get index-handler :route-name :index]
   ["/create" :get create-handler :route-name :create]
   ["/read" :get read-handler :route-name :read]
   ["/update" :get update-handler :route-name :update]
   ["/delete" :get delete-handler :route-name :delete]}))


(defn create-server []
  (http/create-server
   {::http/routes routes
    ::http/type :jetty
    ::http/port port}))

(defn start []
  (http/start (create-server)))