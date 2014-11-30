(ns slice.components.overlay
  (:require [slice.state :as st]
            [slice.input.mouse :as mouse]
            [slice.components.document :as cdoc]
            [slice.components.dom :as dom]
            [slice.document :as doc]))

(defn move-tools [dx dy]
  (let [{:keys [x y]} (st/get-state :tools)]
    (st/put! :tools
             {:x (+ x dx)
              :y (- y dy)})))

(defn tools []
  (let [{:keys [x y]} (st/get-state :tools)
        mode (st/get-state :mode)]
    [:div.tools {:on-mouse-down mouse/mouse-down
                 :on-mouse-up mouse/mouse-up
                 :on-mouse-move (mouse/mouse-move move-tools)
                 :style {:left x :bottom y}}
     [:h3 (str "Mode: " (st/get-state :mode))]
     [:h3 "State:"]
     [:p.state (pr-str (dissoc (st/current-state) :document))]
     [:h3 "Current node:"]
     [:p.node (pr-str (dissoc (doc/document-node) :content))]
     [:div.dom-and-css
      [:div.document {:class (if (= mode :html) "active")}
       [:h3 "Document:"]
       [dom/component]]
      [:div.style {:class (if (= mode :css) "active")}
       [:h3 "Style:"]
       [dom/component]]]
     [:p.document (pr-str (cdoc/component))]]))


(defn component []
  [:div.overlay.layer
   [tools]])
