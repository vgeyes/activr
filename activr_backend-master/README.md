activr_backend
==============

I pushed out some spring stuff to show what it would be like to do a backend in this way. Making and running this requires maven and java. If you want to make it and try it out do the following.

1. Download and install both java 8 and maven. Has to be java 8 because I was using lambdas.
2. Make sure JAVA_HOME gets set and put the maven home/bin directory on your classpath. (This should be done for java too)
3. Go to the project's root directory ../activr/ and run 'mvn package'. This will spit out some garbage and should complete successfully.
4. From the same directory run 'java -jar target/activr-0.0.1-SNAPSHOT.jar'
5. Now open a browser and go to the url 'localhost:8080/nmaki/interests'
6. You should be seeing a JSON response from the server.


Reasons I think Spring is a good option.

1. This particular spring library doesnt require managing an application server, its all embedded.
2. Its one of the most popular java frameworks so there are lots of resources and answered questions online.
3. We all know java so it should be fairly understandable.
4. I personally have the most experience with java web dev server side.
