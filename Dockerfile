FROM h.cn/base/centos7pt:latest
ARG  env
ARG  idc=default
ARG  appName=sysmonitor
ENV  env=${env} idc=${idc} appName=${appName}
RUN  mkdir -p /opt/settings/ && echo -e "env=${env}\nidc=${idc}" > /opt/settings/server.properties
COPY target/$appName*.jar /opt/$appName.jar
EXPOSE 8080 8888
ENTRYPOINT /usr/java/jdk1.8.0_121/bin/java ${JAVA_OPTS} -jar /opt/$appName.jar