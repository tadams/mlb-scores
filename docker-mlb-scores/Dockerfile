FROM openjdk:alpine 

RUN mkdir /opt
COPY mlb-scores*.jar /opt/.
COPY startMlb.sh /opt/.

EXPOSE 8080

CMD ["/opt/startMlb.sh"]
