#!/bin/sh
set -eu

AGENT_CONFIG=/openbao/agent.hcl
RUNTIME_YAML=/run/openbao/config/application-runtime.yml

if [ -f "$AGENT_CONFIG" ]; then
  # Modo env (exec no HCL): Agent é PID 1 e supervisiona o processo filho.
  if grep -Eq '^[[:space:]]*exec[[:space:]]*\{' "$AGENT_CONFIG"; then
    exec bao agent -config="$AGENT_CONFIG"
  fi

  # Modo file (só template {}): Agent em background; Java é PID 1 após o wait.
  # Vault/OpenBao não permite template + exec no mesmo agent.hcl.
  bao agent -config="$AGENT_CONFIG" &
  AGENT_PID=$!

  cleanup() {
    kill "$AGENT_PID" 2>/dev/null || true
    wait "$AGENT_PID" 2>/dev/null || true
  }
  trap cleanup EXIT INT TERM

  i=0
  while [ ! -f "$RUNTIME_YAML" ]; do
    i=$((i + 1))
    if [ "$i" -ge 60 ]; then
      echo "openbao: timeout a aguardar $RUNTIME_YAML" >&2
      exit 1
    fi
    if ! kill -0 "$AGENT_PID" 2>/dev/null; then
      echo "openbao: agent terminou antes de materializar $RUNTIME_YAML" >&2
      exit 1
    fi
    sleep 1
  done

  exec java -jar app.jar
fi

exec java -jar app.jar
