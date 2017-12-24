# Slice

## What is it?

Keyboard-oriented structural editor for HTML and CSS.


## What does it look like?

![screenshot](https://raw.githubusercontent.com/alesguzik/slice/master/docs/screenshot.png)


## How to work with it?

3 layers:

* Reference - draggable image that is put underneath the document you are working on
* preview - html/css that you have created
* tools - draggable window with dom and css panels. This is where the editing happens.

On the left you create a dom tree and assign classes.
On the right you write rules for current node's classes.
Current node/subtree is highlighted. Nodes/subtrees may be folded
for convenience.

List of all keyboard shortcuts may be found here: https://github.com/alesguzik/slice/blob/master/src/cljs/slice/config.cljs#L26-L98

## How to run it?

Run `lein ring server` and `lein figwheel` in two terminals and open your browser at http://localhost:3000
