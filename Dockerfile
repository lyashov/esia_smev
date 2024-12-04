FROM vm-dcp-repo-int.dh.rt.ru:5001/base/openjdk:8-jdk-alpine
COPY /target/esia-0.0.1-SNAPSHOT.jar esia-0.0.1-SNAPSHOT.jar

CMD ["java","-jar","esia-0.0.1-SNAPSHOT.jar"]