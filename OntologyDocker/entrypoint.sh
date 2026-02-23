#!/bin/sh
## Licensed under the terms of http://www.apache.org/licenses/LICENSE-2.0

# Preferir variable de entorno CVE_API_TOKEN; si no existe, intentar leer el secreto de Docker Compose "cvedetails_token"
if [ -n "$CVE_API_TOKEN" ]; then
  TOKEN="$CVE_API_TOKEN"
elif [ -f "/run/secrets/cvedetails_token" ]; then
  TOKEN="$(cat /run/secrets/cvedetails_token)"
fi

if [ -z "$TOKEN" ]; then
  echo "Error: CVEdetails API token not provided." >&2
  echo "Provide it either as env var CVE_API_TOKEN or as a Docker Compose secret named cvedetails_token." >&2
  echo "Example env:   -e CVE_API_TOKEN=\"<cvedetails-api-token>\"" >&2
  echo "Example secret: services: ... secrets: [cvedetails_token] ; secrets: cvedetails_token: { file: ./cvedetails_token }" >&2
  exit 1
fi

echo updating ontology with CVE data
/opt/java-minimal/bin/java -Djdk.xml.maxElementDepth=10000 -jar  /OntologyGeneration/target/ontology-generation-1.0-SNAPSHOT-jar-with-dependencies.jar --cve-download-mode=MONTH --cve-workdir-base-path=/cve-caches --output-path=databases/ontology_with_cves.owl --cvedetails-api-token="${TOKEN}"
echo readying fuseki with updated ontology
/opt/java-minimal/bin/java -Djdk.xml.maxElementDepth=10000 -cp "${FUSEKI_DIR}/${FUSEKI_JAR}" tdb2.tdbloader --loc databases/DB2 databases/ontology_with_cves.owl
echo starting fuseki
exec "$JAVA_HOME/bin/java" $JAVA_OPTIONS -jar "${FUSEKI_DIR}/${FUSEKI_JAR}" --loc databases/DB2 /ds
