# Project description

This project is a mini-twitter app which enables console based users to post messages, follow other users and see their timeline and message wall.

The domain model uses only two entities:

`Timeline (Id, Username, Message, Created)` - keeps all users messages
and
`Follows (Id, Username, followUser)` - keeps which user follows which

The server side utilises Spring Data to abstract out the data access (H2 is used for standalone test and MySQL for deployment) and then provides a HATEOAS REST interface through Spring MVC.

The main console is a very simple java app that connects to the server side via REST interface.

Technologies used:
* Java 8 (date, streams, lamdbas)
* Spring (REST, Data, MVC)
* Flyway
* Gradle
* Embedded Tomcat with H2 for standalone tests
* Docker to build a Tomcat + MySQL container and deploy the app there.

All critical and complex methods are test covered.


Console live test output:
```
cd console
gradlew run

> Alice -> I love the weather today
Posted a new message for Alice
> Bob -> Damn! We lost!
Posted a new message for Bob
> Bob -> Good game though.
Posted a new message for Bob
> Alice
Alice's timeline:
I love the weather today (39 seconds ago)
> Bob
Bob's timeline:
Good game though. (7 seconds ago)
Damn! We lost! (28 seconds ago)
> Charlie -> I'm in New York today! Anyone wants to have a coffee?
Posted a new message for Charlie
> Charlie follows Alice
Charlie is now following Alice
> Charlie follows Bob
Charlie is now following Bob
> Charlie wall
Charlie's wall:
Charlie - I'm in New York today! Anyone wants to have a coffee?(2 minutes ago)
Bob - Good game though.(3 minutes ago)
Bob - Damn! We lost!(4 minutes ago)
Alice - I love the weather today(4 minutes ago)
```

# How to run the server

## Using embedded Apache Tomcat

```
./gradlew tomcatRunWar
```

## Using [Docker Compose](https://docs.docker.com/compose/install/)

```
docker-compose build
docker-compose up
```

To clear docker volumes:
```
cd docker-volumes
./clear.sh
```

# How to run the client

```
cd console
./gradlew run
```

# Server configuration

## Profiles
* You can choose between two profiles: `embedded` and `mysql`
* You can change the default profile in: `src/main/webapp/WEB-INF/web.xml` - `spring.profiles.active` context-param

## Properties
You can change the current properties in `gradle.properties` file - use local `~/.gradle/gradle.properties` to store the passwords securely

# API endpoints

* http://localhost:8080/javanetwork/api/v1/`{userId}`/timeline.json
* http://localhost:8080/javanetwork/api/v1/`{userId}`/follows.json
* http://localhost:8080/javanetwork/api/v1/`{userId}`/wall.json

