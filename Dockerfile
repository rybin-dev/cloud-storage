FROM openjdk:17
COPY build/libs/cloud-storage-0.0.1-SNAPSHOT.jar cloud-storage-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/cloud-storage-0.0.1-SNAPSHOT.jar"]