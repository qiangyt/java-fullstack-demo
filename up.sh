#!/bin/sh

docker-compose up --build --remove-orphans
# docker compose up --build --remove-orphans

open http://localhost:8000