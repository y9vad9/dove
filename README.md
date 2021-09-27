![GitHub release (latest SemVer including pre-releases)](https://img.shields.io/github/v/release/y9neon/dove?include_prereleases) [![Hits-of-Code](https://hitsofcode.com/github/y9neon/dove?branch=master)](https://hitsofcode.com/github/y9neon/dove/view)  ![GitHub](https://img.shields.io/github/license/y9neon/dove)

# dove
Simple messenger made with Kotlin Multiplatform (Android + JVM Backend)

## Setup

### Enviroment
To run this application on your own host you need to provide next env variables:
* `SERVER_PORT`: port where API will be exposed.
* `POSTGRES_URL`: postgresql database url (e.x: `jdbc:postgresql://localhost:5432/dove`)
* `POSTGRES_USER`: user that owns database.
* `POSTGRES_PASSWORD`: user's password.
* `SMTP_ADDRESS`: smtp server address.
* `SMTP_PORT`: smtp server port.
* `EMAIL_SENDER`: user that sends emails.
* `EMAIL_PASSWORD`: user's password.
* `SERVER_UPLOAD_PATH`: absolute path to folder where files will be stored.

### Deploy
To publish `.jar` to your own server use `:backend:server:main:deploy` tasks. It will appear when you will create a file named `deploy.properties` in root project with next variables:
* `host`: server address
* `user`: ssh user
* `password`: ssh user's password
* `deployPath`: path where jar will be uploaded.

### Database
Backend creates and migrating database by itself, so that no need in your own setuping.
