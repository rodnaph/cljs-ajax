(ns ajax.async
  (:require [ajax.core :as ajax]
            [cljs.core.async :refer [>! close! chan]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defn with-async-handler [ch opts]
  (assoc opts :handler
         (fn [res] (go (>! ch res)
                       (close! ch)))))

(defn GET
  [uri & [opts]]
  (let [ch (chan 1)]
    (ajax/GET uri (with-async-handler ch opts))
    ch))

(comment

  ;; Usage

  (let [ch (GET uri opts)]
    (go
      (foo (<! ch))))

  ;; With timeout

  (let [ch (GET uri opts)]
    (go
      (let [[c ch] (alt! [ch (timeout 60000)])]
        ; etc
        ))))
