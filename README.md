# JB Assignment

## by Iurii Nikolaev

### Requirements

- Java 8+
- Git

### Installing

Project consists of two independent modules:

- `packages:backend`
- `packages:react-frontend`

There are two options to run it:

- As independent services;
- With `packages:react-frontend` hosted by `packages:backend`.

#### Installing with `packages:react-frontend` hosted by `packages:backend`

Windows:

```shell
git clone https://github.com/ferusm/jb-assignment.git
cd jb-assignment
.\gradlew.bat :packages:react-frontend:browserDistribution
.\gradlew.bat :packages:backend:openApiGenerate
.\gradlew.bat :packages:backend:shadowJar
```

Linux:

```shell
git clone https://github.com/ferusm/jb-assignment.git
cd jb-assignment
./gradlew :packages:react-frontend:browserDistribution
./gradlew :packages:backend:openApiGenerate
./gradlew :packages:backend:shadowJar
```

### Starting

Windows:

```shell
java -jar .\packages\backend\build\libs\backend-0.1.0-SNAPSHOT-all.jar
```

Linux:

```shell
java -jar ./packages/backend/build/libs/backend-0.1.0-SNAPSHOT-all.jar
```

### Using

Service listening `9090/tcp` port on `0.0.0.0` interface.

### Help

[UI](http://localhost:9090/)

[OpenAPI](http://localhost:9090/openapi)
