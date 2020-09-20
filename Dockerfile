FROM adoptopenjdk/openjdk1.8.0_261:ubi
RUN mkdir /opt/app
COPY crud-employees.jar /opt/app
CMD ["java", "-jar", "/opt/app/crud-employees.jar"]