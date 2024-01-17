# Drone API
The Drone API Is An Interesting REST Service Designed To Manage A Fleet Of Drones, 
each capable of delivering loads of medication items. 
The service allows for drone registration, loading of medication items onto drones, 
checking loaded medication items, monitoring available drones for loading, 
and retrieving battery levels for a given drone.

#### TECHNOLOGY USED
- **Java**: Programming language
- **Maven**: Build tool and project management
- **SpringBoot (Reactive/WebFlux)**: Framework for building Java-based enterprise applications
- **MongoDB (Reactive)**: NoSQL database for data storage
- **Swagger (SpringDoc)**: API documentation tool
- **Docker**: Containerization platform
- **Faker**: Tool for generating fake data for testing purposes


#### PROJECT REQUIREMENT

To Run The Application, Ensure You Have The Following:

- [Docker](https://docs.docker.com/engine/install/)


## Getting Started

#### Clone the Project:
You can clone the project using the following link:

```bash
git clone https://github.com/fabian-emmanuel/Drone-Api-MusalaSoft.git
```
#### Build And Run The Application
Navigate to the project's root directory and execute the following commands:

```bash
./mvnw clean install     # Build the project
docker-compose build     # Build Docker image
docker-compose up        # Run the project using Docker

```
#### Run The Tests
```bash
./mvnw test              # Run the tests
```

#### Accessing The Documentations
- [Swagger Documentation](http://localhost:9000/api/v1/webjars/swagger-ui/index.html) `Application Runs On Port: 9000`
- [Postman Documentation](https://documenter.getpostman.com/view/32364986/2s9YsQ8qDv)


### NOTES
For ease of use and testing, the database has been preloaded with test data. 
Detailed endpoint documentation is available on the SwaggerUI and Postman Doc [Links Provided Above]. 
After running the application, you can explore the documentation and test the endpoints accordingly.
