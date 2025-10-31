# Prompts de [testing]

## Prompt 1: Error con test
### Prompt Utilizado:
Por que al correr los test me da este error? [Inserte el error]
### Respuesta Recibida:
¡Eso que te salió es normal! Estás corriendo mvn test en una carpeta sin pom.xml. Como decidiste la opción B (microservicios independientes), cada servicio tiene su propio pom.xml, así que tenés que ejecutar Maven dentro de cada microservicio (o apuntar al pom con -f).

Cómo correr tus tests
Opción 1: pararte en cada servicio
# tests del business-service
cd microservice-system/business-service
./mvnw test     # o mvn test si no usás wrapper

# tests del data-service
cd ../data-service
./mvnw test

Opción 2: desde la raíz, apuntando al pom
# business
mvn -f microservice-system/business-service/pom.xml test

# data
mvn -f microservice-system/data-service/pom.xml test

### Modificaciones Realizadas:
- Corri los test donde debia

### Explicación del Prompt:
Queria saber porque los test no me funcionaban
### Aprendizajes Obtenidos:
- Debo correr los test donde esta el pom

