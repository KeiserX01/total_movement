#!/usr/bin/env bash

set -euo pipefail

echo "Generando contexto..."

npx repomix

echo "Creando Gist..."

URL=$(gh gist create repomix-output.xml \
  --public \
  --desc "AI Context - $(basename "$(pwd)")")

echo
echo "Gist creado:"
echo "$URL"