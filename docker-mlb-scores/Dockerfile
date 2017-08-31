FROM openjdk:alpine 

ARG GIT_DESCRIBE=SNAPSHOT
LABEL git-describe=$GIT_DESCRIBE

RUN mkdir /opt
COPY mlb-scores*.jar /opt/.
COPY startMlb.sh /opt/.

EXPOSE 8080

CMD ["/opt/startMlb.sh"]
