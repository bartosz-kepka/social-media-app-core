Java JDK 11 required.

Commands for Linux systems while being in project's root directory.

### IMPORTANT: Run MongoDB at port 27017

``docker run -d -p 27017:27017 --name mongo mongo``

### Build project 

``./gradlew build``

### Run project

``java -jar build/libs/social-media-app-core-0.0.1-SNAPSHOT.jar``

Or set run configurations in you favourite IDE

### Api available at port 8080, socket.io service at port 8180

Docs

``localhost:8080/api/swagger-ui/#/``
