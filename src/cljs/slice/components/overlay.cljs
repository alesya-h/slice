(ns slice.components.overlay
  (:require [slice.state :as st]
            [slice.input.mouse :as mouse]
            [slice.components.document :as cdoc]
            [slice.components.dom :as dom]
            [slice.components.css :as css]
            [slice.css :as style]
            [slice.document :as doc]))

(defn move-tools [dx dy]
  (let [{:keys [x y]} (st/get-state :tools)]
    (st/put! :tools
             {:x (+ x dx)
              :y (+ y dy)})))

(defn tools []
  (let [{:keys [x y]} (st/get-state :tools)
        mode (st/get-state :mode)]
    [:div.sl__tools {:on-mouse-down mouse/mouse-down
                 :on-mouse-up mouse/mouse-up
                 :on-mouse-move (mouse/mouse-move move-tools)
                 :style {:left x :top y}}
     [:h3 (str "Mode: " (st/get-state :mode))]
     ;; [:h3 "State:"]
     ;; [:p.state (pr-str (dissoc (st/current-state) :document))]
     ;; [:h3 "Current node:"]
     ;; [:p.node (pr-str (dissoc (doc/document-node) :content))]
     [:div.sl__dom-and-css
      [:div.sl__document {:class (if (= mode :html) "sl__active")}
       [:h3 "Document:"]
       [dom/component]]
      [:div.sl__style {:class (if (= mode :css) "sl__active")}
       [:h3 (str "Style: " (style/current-class))]
       [css/component]]]
     ;; [:p.document (pr-str (cdoc/component))]
     ]))

(defn component []
  [:div.sl__overlay.sl__layer
   [tools]])
