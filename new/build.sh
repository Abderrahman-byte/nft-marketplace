#!/bin/bash

# Setting environment variables from .env file

if [ -f .env ]; then 
    export $(grep -E -v '^#' .env | sed -e 's/\s=/=/g' -e 's/=\s/=/g' | xargs)
fi

if [ ! -z $1 ] ; then
    mvn clean install -pl $1 -am
fi