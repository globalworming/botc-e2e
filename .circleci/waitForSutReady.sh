#!/usr/bin/env bash

stopAfterTries=100
currentTry=0

pollUntilOkResponse() {
  port=$1
  url="http://localhost:${port}"
  echo "waiting for ${url}"
  until $(curl --output /dev/null --silent --head --fail ${url}); do
      countTries
      printf "."
      sleep 5
  done
}

countTries() {
  currentTry=$((currentTry+1))
  if [ ${currentTry} -ge ${stopAfterTries} ]; then
    echo "gave up"
    exit 1
  fi
}

pollUntilOkResponse 3000
pollUntilOkResponse 8080

$*