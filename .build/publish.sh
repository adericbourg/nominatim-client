#!/usr/bin/env bash
set -euo pipefail
IFS=$'\n\t'

function cleanup {
    echo "๐งน Cleanup"
    rm -f gradle.properties nominatim-client-dev-sign.asc
}

trap cleanup SIGINT SIGTERM ERR EXIT

echo "๐ Decrypting signing keys"

gpg --quiet --batch --yes --decrypt --passphrase="${GPG_SECRET}" \
    --output nominatim-client-dev-sign.asc .build/nominatim-client-dev-sign.asc.gpg

gpg --quiet --batch --yes --decrypt --passphrase="${GPG_SECRET}" \
    --output gradle.properties .build/gradle.properties.gpg

gpg --fast-import --no-tty --batch --yes nominatim-client-dev-sign.asc

echo "๐ฆ Publishing"

./gradlew publish

echo "โ Done!"
