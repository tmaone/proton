(ns proton.layers.lang.html.core
  (:require [proton.lib.mode :as mode]
            [proton.lib.helpers :as helpers :refer [console!]]
            [proton.lib.atom :as atom-env]
            [proton.layers.core.actions :as actions])
  (:use [proton.layers.base :only [init-layer! get-packages register-layer-dependencies describe-mode init-package]]))

(defmethod get-packages :lang/html []
  [:language-html
   :autoclose-html
   :autocomplete-html-entities
   :emmet])

(defmethod init-layer! :lang/html []
  (console! "init" :lang/html)
  (register-layer-dependencies :tools/linter
    [:linter-bootlint
     :linter-xmllint]))

;; gives error during init
;; (defmethod init-package [:lang/html :autoclose-html] []
;;  (let [additionalGrammars (array-seq (atom-env/get-config "autoclose-html.additionalGrammars"))]
;;    (atom-env/set-config! "autoclose-html.additionalGrammars" (distinct (concat additionalGrammars ["XSL" "XML"])))))

(defmethod init-package [:lang/html :emmet] []
  (mode/define-package-mode :emmet
    {:mode-keybindings
      {:i {:category "image"
           :s {:action "emmet:update-image-size" :target actions/get-active-editor :title "update image size"}
           :e {:action "emmet:encode-decode-data-url" :target actions/get-active-editor :title "encode/decode image"}}
       :e {:action "emmet:next-edit-point" :target actions/get-active-editor :title "next edit point"}
       :tab {:action "emmet:expand-abbreviation" :target actions/get-active-editor :title "expand abbreviation"}
       :space {:action "emmet:interactive-expand-abbreviation" :target actions/get-active-editor :title "interactive expand"}
       (keyword "%") {:action "emmet:matching-pair" :target actions/get-active-editor :title "matching pair"}
       :n {:action "emmet:select-next-item" :target actions/get-active-editor :title "select next item"}
       :N {:action "emmet:select-previous-item" :target actions/get-active-editor :title "select previous item"}
       :j {:action "emmet:split-join-tag" :target actions/get-active-editor :title "split/join tag"}
       :d {:action "emmet:remove-tag" :target actions/get-active-editor :title "delete tag"}
       :c {:action "emmet:update-tag" :target actions/get-active-editor :title "change tag"}
       :v {:action "emmet:balance-outward" :target actions/get-active-editor :title "balance outward"}
       :V {:action "emmet:balance-inward" :target actions/get-active-editor :title "balance inward"}}})
  (mode/link-modes :html-major-mode (mode/package-mode-name :emmet)))

(defmethod describe-mode :lang/html []
  {:mode-name :html-major-mode
   :atom-scope "text.html.basic"})
