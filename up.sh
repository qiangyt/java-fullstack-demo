#!/bin/sh

docker-compose up -d --build --remove-orphans
# docker compose up -d --build --remove-orphans

open http://localhost:8000