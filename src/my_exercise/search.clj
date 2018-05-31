(ns my-exercise.search
  (:require 
    [hiccup.page :refer [html5]]
    [ring.util.anti-forgery :refer [anti-forgery-field]]
    [clj-http.client :as client] ; pretty sure I could have used ring for this but clj-http had better docs
  )
)

(use '[clojure.string :only (join split lower-case)]) ; shorten string method syntax

(defn header [_]
  [:head
   [:meta {:charset "UTF-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1.0, maximum-scale=1.0"}]
   [:title "Your Next Election(s)"]
   [:link {:rel "stylesheet" :href "default.css"}]])

(defn headline [_]
  [:div {:class "headline"}
    [:h2 "Current elections in your area"]
  ])

; I am not sure what the structing conventions for Clojure are, I structured in a Javascript-y way so I wouldn't get lost in parentheses.

(defn search-results [req]
  (let [{params :params, {zip :zip, street :street, city :city, state :state, street-2 :street-2} :params} req]
    (let [[state-ocd :as state-ocd] (str "ocd-division/country:us/state:" (lower-case state))]
      (let [[place-ocd :as place-ocd] (str state-ocd "/place:" (lower-case (join "_" (split city #"\s"))))]
        (let [[api-request :as api-request] (str "https://api.turbovote.org/elections/upcoming?district-divisions=" state-ocd "," place-ocd)]
          (client/get api-request)
        ) ; end of api-request let
      )  ; end of place-ocd let
    ) ; end of state-ocd let
  ) ; end of params let
) ; end of definiteion

; Here I was hoping to modularize my code a bit and parse out the EDN block.
; I couldn't figure out how to get access to the variables I had created inside searc-results.
; I did not have time to parse the EDN response.
; Moving forward with the project I would parse the EDN into something readable.
; I would also add error handling for misspelled or non-existent places.


; (defn display-results [request]
;   (search-results request)])
;   [:div {:class "display-results"}
;   [:h2 (str "Upcoming Elections in" city state)]
 
(defn page [request]
  (html5
    (header request)
    (headline request)
    (search-results request)
    ; (display-results request)
  )
)
