FROM java:8 

WORKDIR /code

# Adding source, compile and package into a fat jar
ADD target/wspdevops-0.0.1-SNAPSHOT.jar /code/wspdevops-0.0.1-SNAPSHOT.jar
RUN bash -c 'touch /code/wspdevops-0.0.1-SNAPSHOT.jar'
EXPOSE 7891
EXPOSE 7895
CMD java -jar /code/wspdevops-0.0.1-SNAPSHOT.jar 
