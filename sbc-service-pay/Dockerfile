FROM 172.19.25.163:5000/jre8:0.4
ARG jarVersion
COPY service-pay-app/target/service-pay-app-$jarVersion.jar /data/server/bin/sbc.jar
RUN ln -s /data/server/bin/sbc.jar /data/server/bin/server.jar && \
    echo "spring.cloud.nacos.config.server-addr=127.0.0.1:8848" > /data/server/conf/application.properties && \
    echo "spring.application.name=sbc-service-pay" >> /data/server/conf/application.properties && \
    echo "spring.cloud.nacos.config.prefix=sbc-service-pay" >> /data/server/conf/application.properties && \
    echo "spring.cloud.nacos.config.file-extension=properties" >> /data/server/conf/application.properties && \
    echo "spring.cloud.nacos.config.enabled=true" >> /data/server/conf/application.properties && \
    echo "spring.cloud.nacos.config.shared-dataids=common.properties,mysql-setting.properties" >> /data/server/conf/application.properties && \
    echo "spring.cloud.nacos.config.refreshable-dataids=common.properties" >> /data/server/conf/application.properties
CMD /data/server/bin/startup.sh; sleep inf