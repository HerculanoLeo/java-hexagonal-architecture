#!/bin/sh
set -eu

if [ -f /openbao/agent.hcl ]; then
  exec bao agent -config=/openbao/agent.hcl
fi

exec java -jar app.jar
