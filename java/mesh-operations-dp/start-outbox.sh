#!/bin/bash

curl -X PUT \
  "http://$DATAPLATFORM_IP:8083/connectors/salesorder.dbzsrc.outbox/config" \
  -H 'Content-Type: application/json' \
  -H 'Accept: application/json' \
  -d '{
  "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
  "tasks.max": "1",

  "database.server.name": "postgresql",
  "database.port": "5432",
  "database.user": "t0_customer",
  "database.password": "abc123!",
  "database.dbname": "t0_customerdb",
  "schema.include.list": "public",
  "table.include.list": "public.outbox",
  "plugin.name": "pgoutput",
  "publication.name":"debezium",
  "slot.name":"debezium",
  "tombstones.on.delete": "false",
  "database.hostname": "postgresql",
  "transforms": "outbox",
  "transforms.outbox.type": "io.debezium.transforms.outbox.EventRouter",
  "transforms.outbox.table.field.event.id": "id",
  "transforms.outbox.table.field.event.key": "event_key",
  "transforms.outbox.table.field.event.payload": "payload_avro",
  "transforms.outbox.table.field.event.timestamp": "created_at",
  "transforms.outbox.route.by.field": "event_type",
  "transforms.outbox.route.topic.replacement": "pub.customer.${routedByValue}.state.v1",
  "value.converter": "io.debezium.converters.ByteBufferConverter",
  "topic.creation.default.replication.factor": 3,
  "topic.creation.default.partitions": 8,
  "key.converter": "org.apache.kafka.connect.storage.StringConverter"
}'