#!/usr/bin/env bash
set -euo pipefail
IFS=$'\n\t'

function cleanup {
    echo "🧹 Cleanup"
    rm -f gradle.properties nominatim-client-dev-sign.asc
}

trap cleanup SIGINT SIGTERM ERR EXIT

echo "🔑 Decrypting signing keys"

gpg --quiet --batch --yes --decrypt --passphrase="${GPG_SECRET}" \
    --output nominatim-client-dev-sign.asc .build/nominatim-client-dev-sign.asc.gpg

gpg --quiet --batch --yes --decrypt --passphrase="${GPG_SECRET}" \
    --output gradle.properties .build/gradle.properties.gpg

gpg --fast-import --no-tty --batch --yes nominatim-client-dev-sign.asc

echo "📦 Publishing"

./gradlew publish

echo "✅ Done!"
