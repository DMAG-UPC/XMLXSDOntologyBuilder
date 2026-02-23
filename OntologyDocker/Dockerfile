FROM eclipse-temurin:25.0.1_8-jdk-noble

ARG FUSEKI_DIR=/fuseki
ARG JENA_VERSION=5.0.0
ARG FUSEKI_JAR=jena-fuseki-server-${JENA_VERSION}.jar
ARG JAVA_MINIMAL=/opt/java-minimal

ARG REPO=https://repo1.maven.org/maven2
ARG JAR_URL=${REPO}/org/apache/jena/jena-fuseki-server/${JENA_VERSION}/${FUSEKI_JAR}
RUN apt -y update && apt -y upgrade
RUN apt install -y curl binutils
RUN apt install -y git maven

WORKDIR $FUSEKI_DIR
COPY OntologyDocker/download.sh .
RUN chmod a+x download.sh
RUN ./download.sh --chksum sha1 "$JAR_URL"

WORKDIR /
RUN mkdir OntologyGeneration
COPY ./OntologyGeneration/src /OntologyGeneration/src
COPY ./OntologyGeneration/pom.xml /OntologyGeneration/
WORKDIR /OntologyGeneration
RUN ls -la
RUN pwd
RUN mvn package
RUN ls -la target


ARG JDEPS_EXTRA="jdk.crypto.cryptoki,jdk.crypto.ec"
WORKDIR $FUSEKI_DIR
RUN \
  JDEPS="$(jdeps --multi-release base --print-module-deps --ignore-missing-deps ${FUSEKI_JAR})"  && \
  jlink \
        --compress 2 --strip-debug --no-header-files --no-man-pages \
        --output "${JAVA_MINIMAL}" \
        --add-modules "${JDEPS},${JDEPS_EXTRA}"


WORKDIR $FUSEKI_DIR

ARG LOGS=${FUSEKI_DIR}/logs
ARG DATA=${FUSEKI_DIR}/databases

#ARG JENA_USER=fuseki
#ARG JENA_GROUP=$JENA_USER
#ARG JENA_GID=1000
#ARG JENA_UID=1000
#
#RUN addgroup --gid "${JENA_GID}" "${JENA_GROUP}"
#RUN adduser "${JENA_USER}" --ingroup "${JENA_GROUP}" -u "${JENA_UID}"
#
#RUN mkdir --parents "${FUSEKI_DIR}"
#RUN chown -R $JENA_USER:$JENA_GROUP ${FUSEKI_DIR}

#USER $JENA_USER
ADD OntologyDocker/entrypoint.sh .
ADD OntologyDocker/log4j2.properties .
RUN \
    mkdir -p $LOGS && \
    mkdir -p $DATA && \
    chmod a+x entrypoint.sh

## Default environment variables.
ENV \
    JAVA_HOME=${JAVA_MINIMAL}           \
    JAVA_OPTIONS="-Xmx2048m -Xms2048m"  \
    JENA_VERSION=${JENA_VERSION}        \
    FUSEKI_JAR="${FUSEKI_JAR}"          \
    FUSEKI_DIR="${FUSEKI_DIR}"



COPY OntologyGeneration/src/main/resources/medsecurance_ontology.owl ./ontology.xml
RUN chmod a+r ontology.xml
COPY OntologyGeneration/src/main/resources/RequirementsToCVETypes.csv ./RequirementsToCVETypes.csv
RUN chmod a+r RequirementsToCVETypes.csv
COPY OntologyGeneration/src/main/resources/Requirements.xml  ./Requirements.xml
RUN  chmod a+r Requirements.xml
COPY OntologyGeneration/src/main/resources/medsecurance_ontology.owl ./medsecurance_ontology.owl
RUN chmod a+r medsecurance_ontology.owl
ENV PATH="$PATH:/fuseki"
RUN mkdir cache_merged
RUN mkdir cache_queries
RUN mkdir CVE_by_types
RUN mkdir -p databases/DB2


EXPOSE 3030
ENTRYPOINT ["./entrypoint.sh" ]
CMD []