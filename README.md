# IKARITA SERVER

## Description

API server for the Ikarita project that allows authentication and access to resources.

# Requirements

- Java JDK 21
- Podman >= 5.5.2

## Prepare Podman for testcontainers

Before being able to run the testcontainers ensure that the podman socket is running

```shell
systemctl --user enable --now podman.socket
```

To ensure that it can interact with rootless containers expose the following environment variable which will disable Ryuk.

```shell
export DOCKER_HOST="unix:///run/user/$UID/podman/podman.sock"
export TESTCONTAINERS_RYUK_DISABLED=true
```

# Build Artefact

The server relies on the existence of Postgres database.

1. Clone the project on your local machine
```shell
git clone https:\\github.com\ikarita\ikarita-server.git
```

2. Move the directory where the project was cloned
```shell
cd ikarita-server
```
3. Move run the package command of mvn
```shell
mvn clean package
```

4. Alternatively, if you did not define the variables for podman you can run the following command
```shell
DOCKER_HOST="unix:///run/user/$UID/podman/podman.sock" \
TESTCONTAINERS_RYUK_DISABLED="true" \
mvn clean package
```

## Build Docker Image

This repository provides a `Containerfile` that will rely on the built package to generate an image.

First ensure that the file `target/server-<VERSION>.jar` exists. If not refer to the `Build Artefact` section.

Then run the following command to generate the OCI image

```shell
podman build --format docker -t ikarita-server .
```

## API

The [API Documentation](https://petstore.swagger.io/?url=https://raw.githubusercontent.com/ikarita/ikarita-server-api/dev/openapi.json) 
is available through the Open API format V3.