from quay.io/keycloak/keycloak:11.0.2
 
LABEL maintainer="taegeon_woo@tmax.co.kr"

# 0. add tibero/postgresql.jdbc.jar & module.xml
RUN mkdir -p /opt/jboss/keycloak/modules/system/layers/base/com/tmax/tibero/jdbc/main \
/opt/jboss/tools/databases/tibero \
/opt/jboss/tools/cli/databases/tibero /opt/jboss/keycloak/modules/system/layers/keycloak/org/postgresql/main
ADD build/tibero/tibero6-jdbc.jar /opt/jboss/keycloak/modules/system/layers/base/com/tmax/tibero/jdbc/main/tibero6-jdbc.jar
ADD build/tibero/module.xml /opt/jboss/keycloak/modules/system/layers/base/com/tmax/tibero/jdbc/main/module.xml
ADD build/tibero/*.cli /opt/jboss/tools/cli/databases/tibero/
ADD build/tibero/module.xml /opt/jboss/tools/databases/tibero/module.xml
ADD build/postgresql/* /opt/jboss/keycloak/modules/system/layers/keycloak/org/postgresql/main/

# 1. Update stanalone.xml & domain.xml
ADD build/config/docker-entrypoint.sh /opt/jboss/tools/docker-entrypoint.sh

# 2. add tmax theme & hypercloud/login & supercloud/login & hyperspace/login & hyperauth & cnu & supervds

COPY themes/tmax /opt/jboss/keycloak/themes/tmax
COPY themes/hypercloud/login /opt/jboss/keycloak/themes/hypercloud/login
COPY themes/supercloud/login /opt/jboss/keycloak/themes/supercloud/login
COPY themes/hyperspace/login /opt/jboss/keycloak/themes/hyperspace/login
COPY themes/hyperauth /opt/jboss/keycloak/themes/hyperauth
COPY themes/cnu /opt/jboss/keycloak/themes/CNU
COPY themes/supervds /opt/jboss/keycloak/themes/superVDS

# 3. keycloak service jar & sql jar & server-spi-private jar change for tibero, this contains sql error fixme!!
RUN rm /opt/jboss/keycloak/modules/system/layers/keycloak/org/keycloak/keycloak-services/main/keycloak-services-11.0.2.jar \
 /opt/jboss/keycloak/modules/system/layers/keycloak/org/keycloak/keycloak-model-jpa/main/keycloak-model-jpa-11.0.2.jar  \
 /opt/jboss/keycloak/modules/system/layers/keycloak/org/keycloak/keycloak-server-spi-private/main/keycloak-server-spi-private-11.0.2.jar
ADD build/jar/keycloak-services-11.0.2.jar /opt/jboss/keycloak/modules/system/layers/keycloak/org/keycloak/keycloak-services/main/keycloak-services-11.0.2.jar
ADD build/jar/keycloak-model-jpa-11.0.2.jar /opt/jboss/keycloak/modules/system/layers/keycloak/org/keycloak/keycloak-model-jpa/main/keycloak-model-jpa-11.0.2.jar
ADD build/jar/keycloak-server-spi-private-11.0.2.jar /opt/jboss/keycloak/modules/system/layers/keycloak/org/keycloak/keycloak-server-spi-private/main/keycloak-server-spi-private-11.0.2.jar

# 4. hyperauth-spi.jar (SPI)
ADD target/keycloak-spi-jar-with-dependencies.jar /opt/jboss/keycloak/standalone/deployments/hyperauth-spi.jar

# 5. Naver & Kakao html
ADD build/config/realm-identity-provider-* /opt/jboss/keycloak/themes/base/admin/resources/partials/

# 6. For pdf term file
USER jboss
COPY build/config/TmaxOneAccount_Service_Policy_* /opt/jboss/keycloak/welcome-content/term/
COPY build/config/TmaxOneAccount_Privacy_Policy_* /opt/jboss/keycloak/welcome-content/term/

# 7. For Log to File
USER root
RUN mkdir -p /opt/jboss/keycloak/standalone/log/hyperauth /opt/jboss/startup-scripts
# 8. RUN mkdir -p /opt/jboss/startup-scripts
ADD build/config/jboss.cli /opt/jboss/startup-scripts/jboss.cli

# 9. Vesion Env for version API
ARG HYPERAUTH_VERSION
ENV HYPERAUTH_VERSION $HYPERAUTH_VERSION

# 10. For Profile Picture
RUN mkdir -p opt/jboss/keycloak/welcome-content/profile-picture \ chmod 755 opt/jboss/keycloak/welcome-content/profile-picture
# RUN chmod 755 opt/jboss/keycloak/welcome-content/profile-picture

# 11. For Elastic APM Jar
COPY build/jar/elastic-apm-agent-1.26.0.jar /opt/jboss/keycloak/elastic-apm-agent-1.26.0.jar
RUN chmod 755 /opt/jboss/keycloak/elastic-apm-agent-1.26.0.jar
