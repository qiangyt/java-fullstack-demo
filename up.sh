#!/bin/sh

docker-compose up -d --build --remove-orphans
# docker compose up -d --build --remove-orphans

sleep 5

open http://localhost:8000