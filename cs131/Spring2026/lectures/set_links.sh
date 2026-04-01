#! /bin/sh

DEST=$1
ln -s ./reveal/css $1/css
ln -s ./reveal/js $1/js
ln -s ./reveal/dist $1/dist
ln -s ./reveal/plugin $1/plugin
