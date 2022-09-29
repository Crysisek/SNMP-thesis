#!/bin/bash

# Simply wait until original kafka container and zookeeper are started.
sleep 15.0s

# Parse string of kafka topics into an array.
# https://stackoverflow.com/a/10586169/1839360
kafkaTopicsArrayString="$KAFKA_TOPICS"
IFS=' ' read -r -a kafkaTopicsArray <<< "$kafkaTopicsArrayString"

# Broker hosts
bootstrapHostsValue=$BOOTSTRAP_HOSTS

echo "Broker host: "
echo "$bootstrapHostsValue"

# Create kafka topic for each item
for newTopic in "${kafkaTopicsArray[@]}"; do
  kafka-topics --create --bootstrap-server "$bootstrapHostsValue" --topic "$newTopic" --partitions 16 --replication-factor 1 --if-not-exists
done