Java JDK 11 required.

Commands for Linux systems while being in project's root directory.

Run MongoDB at port 27017

``docker run -d -p 27017:27017 --name mongo mongo``

Build project 

``./gradlew build``

Run project

``java -jar build/libs/social-media-app-core-0.0.1-SNAPSHOT.jar``

Or set run configurations in you favourite IDE

When run, server available at localhost:8080

To test, send POST request to ``localhost:8080/api/posts`` with example body:

``
{
    "content": "This is a test post context"
}
``

Docs

``localhost:8080/api/swagger-ui/#/``