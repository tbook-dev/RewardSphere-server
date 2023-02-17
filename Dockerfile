FROM ibm-semeru-runtimes:open-11-jre-focal

COPY target/RewardSphere-0.0.1-SNAPSHOT.jar /root/

CMD java -XX:MaxRAM=468m -Dspring.profiles.active=stag -jar /root/RewardSphere-0.0.1-SNAPSHOT.jar
