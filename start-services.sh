#!/bin/bash

docker-compose up --build -d postgres rabbitmq

./build-all.sh

docker-compose up --build -d