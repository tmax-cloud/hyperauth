from quay.io/keycloak/keycloak:11.0.2
 
LABEL maintainer="taegeon_woo@tmax.co.kr"

# 0. add tibero.jdbc.jar & module.xml
RUN mkdir -p /opt/jboss/keycloak/modules/system/layers/base/com/tmax/tibero/jdbc/main
ADD build/tibero/tibero6-jdbc.jar /opt/jboss/keycloak/modules/system/layers/base/com/tmax/tibero/jdbc/main/tibero6-jdbc.jar
ADD build/tibero/module.xml /opt/jboss/keycloak/modules/system/layers/base/com/tmax/tibero/jdbc/main/module.xml

RUN mkdir -p /opt/jboss/tools/databases/tibero
ADD build/tibero/module.xml /opt/jboss/tools/databases/tibero/module.xml

RUN mkdir -p /opt/jboss/tools/cli/databases/tibero
ADD build/tibero/change-database.cli /opt/jboss/tools/cli/databases/tibero/change-database.cli
ADD build/tibero/standalone-configuration.cli /opt/jboss/tools/cli/databases/tibero/standalone-configuration.cli
ADD build/tibero/standalone-ha-configuration.cli /opt/jboss/tools/cli/databases/tibero/standalone-ha-configuration.cli

# 1. add postgresql-jdbc.jar & module.xml
RUN mkdir -p /opt/jboss/keycloak/modules/system/layers/keycloak/org/postgresql/main
ADD build/postgresql/postgresql-42.2.14.jar /opt/jboss/keycloak/modules/system/layers/keycloak/org/postgresql/main/postgresql-42.2.14.jar
ADD build/postgresql/module.xml /opt/jboss/keycloak/modules/system/layers/keycloak/org/postgresql/main/module.xml

# 2. Update stanalone.xml & domain.xml
ADD build/config/docker-entrypoint.sh /opt/jboss/tools/docker-entrypoint.sh

# 3. add tmax theme & hypercloud/login & hyperspace/login & hyperauth & cnu & supervds
COPY themes/tmax /opt/jboss/keycloak/themes/tmax
COPY themes/hypercloud/login /opt/jboss/keycloak/themes/hypercloud/login
COPY themes/hyperspace/login /opt/jboss/keycloak/themes/hyperspace/login
COPY themes/hyperauth /opt/jboss/keycloak/themes/hyperauth
COPY themes/cnu /opt/jboss/keycloak/themes/CNU
COPY themes/supervds /opt/jboss/keycloak/themes/superVDS

# 4. keycloak service jar & sql jar & server-spi-private jar change for tibero, this contains sql error fixme!!
RUN rm /opt/jboss/keycloak/modules/system/layers/keycloak/org/keycloak/keycloak-services/main/keycloak-services-11.0.2.jar
ADD build/jar/keycloak-services-11.0.2.jar /opt/jboss/keycloak/modules/system/layers/keycloak/org/keycloak/keycloak-services/main/keycloak-services-11.0.2.jar
RUN rm /opt/jboss/keycloak/modules/system/layers/keycloak/org/keycloak/keycloak-model-jpa/main/keycloak-model-jpa-11.0.2.jar
ADD build/jar/keycloak-model-jpa-11.0.2.jar /opt/jboss/keycloak/modules/system/layers/keycloak/org/keycloak/keycloak-model-jpa/main/keycloak-model-jpa-11.0.2.jar
RUN rm /opt/jboss/keycloak/modules/system/layers/keycloak/org/keycloak/keycloak-server-spi-private/main/keycloak-server-spi-private-11.0.2.jar
ADD build/jar/keycloak-server-spi-private-11.0.2.jar /opt/jboss/keycloak/modules/system/layers/keycloak/org/keycloak/keycloak-server-spi-private/main/keycloak-server-spi-private-11.0.2.jar

# 5. hyperauth-spi.jar (SPI)
ADD target/keycloak-spi-jar-with-dependencies.jar /opt/jboss/keycloak/standalone/deployments/hyperauth-spi.jar

# 6. Naver & Kakao html
ADD build/config/realm-identity-provider-naver.html /opt/jboss/keycloak/themes/base/admin/resources/partials/realm-identity-provider-naver.html
ADD build/config/realm-identity-provider-naver-ext.html /opt/jboss/keycloak/themes/base/admin/resources/partials/realm-identity-provider-naver-ext.html
ADD build/config/realm-identity-provider-kakao.html /opt/jboss/keycloak/themes/base/admin/resources/partials/realm-identity-provider-kakao.html
ADD build/config/realm-identity-provider-kakao-ext.html /opt/jboss/keycloak/themes/base/admin/resources/partials/realm-identity-provider-kakao-ext.html

# 7. For pdf term file
USER jboss
COPY build/config/TmaxOneAccount_Privacy_Policy_210401.pdf /opt/jboss/keycloak/welcome-content/term/TmaxOneAccount_Privacy_Policy.pdf
COPY build/config/TmaxOneAccount_Service_Policy_210401.pdf /opt/jboss/keycloak/welcome-content/term/TmaxOneAccount_Service_Policy.pdf
COPY build/config/TmaxOneAccount_Privacy_Policy_210222.pdf /opt/jboss/keycloak/welcome-content/term/TmaxOneAccount_Privacy_Policy_210222.pdf
COPY build/config/TmaxOneAccount_Service_Policy_210222.pdf /opt/jboss/keycloak/welcome-content/term/TmaxOneAccount_Service_Policy_210222.pdf
COPY build/config/TmaxOneAccount_Privacy_Policy_210105.pdf /opt/jboss/keycloak/welcome-content/term/TmaxOneAccount_Privacy_Policy_210105.pdf
COPY build/config/TmaxOneAccount_Service_Policy_210105.pdf /opt/jboss/keycloak/welcome-content/term/TmaxOneAccount_Service_Policy_210105.pdf
COPY build/config/TmaxOneAccount_Privacy_Policy_210401.pdf /opt/jboss/keycloak/welcome-content/term/TmaxOneAccount_Privacy_Policy_210401.pdf
COPY build/config/TmaxOneAccount_Service_Policy_210401.pdf /opt/jboss/keycloak/welcome-content/term/TmaxOneAccount_Service_Policy_210401.pdf

# 8. For Log to File
USER root
RUN mkdir -p /opt/jboss/keycloak/standalone/log/hyperauth
RUN mkdir -p /opt/jboss/startup-scripts
ADD build/config/jboss.cli /opt/jboss/startup-scripts/jboss.cli

# 8. Vesion Env for version API
ARG HYPERAUTH_VERSION
ENV HYPERAUTH_VERSION $HYPERAUTH_VERSION

# 9. For Profile Picture
RUN mkdir -p opt/jboss/keycloak/welcome-content/profile-picture
RUN chmod 755 opt/jboss/keycloak/welcome-content/profile-picture

# For Security Test
COPY build/jar/elastic-apm-agent-1.26.0.jar /opt/jboss/keycloak/elastic-apm-agent-1.26.0.jar
RUN chmod 755 /opt/jboss/keycloak/elastic-apm-agent-1.26.0.jar

#RUN chmod 750 /opt/jboss/keycloak/standalone/log
#RUN rm /etc/profile
#ADD profile /etc/profile
#RUN rm /etc/bashrc
#ADD bashrc /etc/bashrc
#RUN rm /etc/csh.cshrc
#ADD csh.cshrc /etc/csh.cshrc
#RUN rm -rf /opt/jboss/keycloak/docs/example
#RUN rm /opt/jboss/keycloak/modules/system/layers/keycloak/org/keycloak/keycloak-server-subsystem/main/server-war/WEB-INF/web.xml
#ADD web.xml /opt/jboss/keycloak/modules/system/layers/keycloak/org/keycloak/keycloak-server-subsystem/main/server-war/WEB-INF/web.xml
#RUN rm /opt/jboss/keycloak/bin/standalone.conf
#ADD standalone.conf /opt/jboss/keycloak/bin/standalone.conf




