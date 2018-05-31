(ns my-exercise.home
  (:require [hiccup.page :refer [html5]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [my-exercise.us-state :as us-state]
            [ring.util.codec :as codec]
            [ring.util.request :as req]
            ))

(defn header [_]
  [:head
   [:meta {:charset "UTF-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1.0, maximum-scale=1.0"}]
   [:title "Find my next election"]
   [:link {:rel "stylesheet" :href "default.css"}]])

(defn background-info [_]
  [:div {:class "background-info"}
    [:h2 "A little background info"]
    [:p "Hey! Thanks for taking the time to review my application."]
    [:p "I should let you know that I had never used Clojure before this project, as  you will probably be able to tell. "
      "I had a small moment of panic when I tried to start the project and had to download JDK, but I think Democracy Works is pretty rad and I didn't want to throw in the towel. "
      "Anyways, so I slugged through a few chapters of "
        [:a {:href "https://www.braveclojure.com/"} "Clojure for the Brave and True"]
        ", realized it was probably not neccessary to add Emacs to the situation, and then just decided to try and hack it together by trial and error. "
      "So here is what I was able to accomplish!"]
    [:h2 "State of the project"]
    [:ul
      [:li "x Create the missing /search route"]
      [:li "x Ingest the incoming form parameters"]
      [:li "x Derive a basic set of OCD-IDs from the address - currently only works for State and City (place)"]
      [:li "x Retrieve upcoming elections from the Democracy Works election API using those OCD-IDs"]
      [:li {:class "incomplete"} "Display any matching elections to the user - currently just displaying the EDN response"]]
    [:h2 "Future of the project"]
    [:ul
      [:li "Display the election data in a more lovely manner"]
      [:li "Add testing"]
      [:li "Styling"]
      [:li "Add error handling for non-existent places and misspellings"]
      [:li "Generalized views for displaying upcoming elections around the country"]
      [:li "Generalized views for displaying voter id requirements by State"]]
   ])

(defn instructions [request]
  [:div {:class "instructions"}
   (background-info request)])

(defn address-form [_]
  [:div {:class "address-form"}
   [:h1 "Find my next election"]
   [:form {:action "/search" :method "post"}
    (anti-forgery-field)
    [:p "Enter the address where you are registered to vote"]
    [:div
     [:label {:for "street-field"} "Street:"]
     [:input {:id "street-field"
              :type "text"
              :name "street"}]]
    [:div
     [:label {:for "street-2-field"} "Street 2:"]
     [:input {:id "street-2-field"
              :type "text"
              :name "street-2"}]]
    [:div
     [:label {:for "city-field"} "City:"]
     [:input {:id "city-field"
              :type "text"
              :name "city"}]
     [:label {:for "state-field"} "State:"]
     [:select {:id "state-field"
               :name "state"}
      [:option ""]
      (for [state us-state/postal-abbreviations]
        [:option {:value state} state])]
     [:label {:for "zip-field"} "ZIP:"]
     [:input {:id "zip-field"
              :type "text"
              :name "zip"
              :size "10"}]]
    [:div.button
     [:button {:type "submit"} "Search"]]

     ]])

(defn page [request]
  (html5
   (header request)
   (instructions request)
   (address-form request)))
