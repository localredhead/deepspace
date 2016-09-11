(ns deepspace.core
  (:require [promesa.core :as p]
            [cuerdas.core :as c]
            [httpurr.client.xhr :as http]))

(def Buffer (aget (js/require "buffer") "Buffer"))
(def ipfs-connection "https://ipfs..infura.io:5001/api/v0")
(def ipfs-local-connection "http://localhost:5001/api/v0")
(defonce connection (atom ipfs-connection))

(defn ipfs-encode-json
  [json]
  (js/JSON.stringify (clj->js json)))

(defn ipfs-decode-json
  [json]
  (js->clj (js/JSON.parse json) :keywordize-keys true))


(defn ->blob [data]
  (clj->js (if (c/starts-with? data "data:")
             {:uri data :type "application/octet-stream"}
             {:uri (str "data:application/octet-stream;base64," (.toString (Buffer. data) "base64")) :type "application/octet-stream"})))

(defn form-append
  [json]
  (let [form-data (doto (js/FormData.)
                        (.append "file" (->blob (ipfs-encode-json json))))]
    form-data))

(defn put!
  "Creates a IPFS object. Assumes json."
  ([json] (put! @connection json))
  ([connection json]
   (let [url (str connection "/add")]
     (-> (http/post url {:body (form-append json)})
         (p/then (fn [r] (prn r)
                   (:Hash (ipfs-decode-json (:body r)))))))))

(defn fetch
  "Returns the data associated with an IPFS hash"
  ([hash]
    (fetch @connection hash))
  ([connection hash]
   (-> (http/get (str connection "/cat?arg=" hash))
       (p/then (fn [r]
                 (ipfs-decode-json (:body r)))))))
