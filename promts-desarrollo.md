# Prompts de [desarrollo]

## Prompt 1: Advertencia de vulnerabilidad
### Prompt Utilizado:
Por que mi pom.xml me da una advertencia de vulnerabilidad? [codigo pom.xml]

### Respuesta Recibida:
Buena pregunta. Esas “vulnerabilidades” te las está marcando el analizador del IDE (Mend.io / Qodana) porque tus starters de Spring traen dependencias transitivas de Spring Framework 6.2.9 (por ej. spring-beans y spring-webmvc) con un CVE reciente (path traversal en contenedores Servlet no-compliant). Aunque uses el Tomcat embebido (que es compliant y no suele verse afectado), el scanner marca la versión igual.

Cómo lo solucionás / mitigás:

1) Asegurate de heredar del parent de Spring Boot actualizado

<parent>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-parent</artifactId>
  <version>3.5.5</version>   <!-- o la última 3.5.x disponible -->
  <relativePath/>
</parent>


Boot 3.5.5 alinea a Spring Framework 6.2.10+, que incluye el parche de ese CVE.
Después de cambiarlo: Reload Maven y ejecutá ./mvnw clean verify.

2) No declares versiones manuales de Spring Framework

Si en algún momento agregaste explícitamente:

<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-webmvc</artifactId>
  <version>6.2.9</version>   <!-- ❌ -->
</dependency>


quitá esas dependencias “peladas” o sacá la versión y dejá que el BOM de Spring Boot gestione todo (usá siempre los starters).

3) Verificá qué versión quedó realmente

Para chequear qué versión estás usando efectivamente:

# En cada microservicio
./mvnw dependency:tree -Dincludes=org.springframework:spring-beans
./mvnw dependency:tree -Dincludes=org.springframework:spring-webmvc


Deberías ver 6.2.10 (o superior) después de subir Boot a 3.5.5.

4) Si el scanner sigue avisando

A veces el IDE tarda en refrescar su base. Probá:

Reload All Maven Projects (ventana Maven).

File → Invalidate Caches / Restart en IntelliJ.

5) Contexto (para que estés tranquila)

El CVE que te marca es condicional (“non-compliant Servlet containers”). Con Tomcat embebido estándar, el riesgo práctico es bajo.

Aun así, la buena práctica es actualizar a la última 3.5.x de Spring Boot para arrastrar el fix.

Si querés, te reviso tus pom.xml ahora mismo (pegame el contenido de ambos) y te digo si ya quedó alineado.

### Modificaciones Realizadas:
- Ninguna, no me funciono la solucion

### Explicación del Prompt:
Queria saber como hacer el pom.xml con la estructura correcta para evitar vulnerabilidades

### Aprendizajes Obtenidos:
- ES un problema de vulnerabilidad por la version de springboot

# Prompts de [desarrollo]

## Prompt 2: Error de paquetes
### Prompt Utilizado:
Por que cuando creo el BusinessServiceApplication.java me da este error? Spring Boot Application in default package

### Respuesta Recibida:
Ese error sale porque tu clase BusinessServiceApplication está en el paquete por defecto (no tiene package ... arriba) o está ubicada en una carpeta que no corresponde con su paquete. Spring Boot escanea componentes desde el paquete donde está la clase @SpringBootApplication hacia abajo; si está en el default package, el escaneo y el autoconfig pueden fallar.

Cómo arreglarlo (2 minutos)

Crea un paquete raíz para el servicio y mueve ahí todo el código del servicio:

En business-service/src/main/java crea el paquete:

com.um.businessservice


Dentro, opcionalmente, subpaquetes:

com.um.businessservice.client
com.um.businessservice.controller
com.um.businessservice.service
com.um.businessservice.config


Mueve las clases:

BusinessServiceApplication.java → com/um/businessservice/

DataServiceClient.java → com/um/businessservice/client/

(Opcional pero recomendado) Repite el mismo criterio en data-service: por ejemplo com.um.dataservice como paquete raíz.

Listo: desaparece el warning “Spring Boot Application in default package” y el escaneo de componentes funcionará bien (controllers, services, Feign, etc.).

Cualquier otra clase del servicio → bajo com.um.businessservice...

### Modificaciones Realizadas:
- Cree un package dataService y businessService

### Explicación del Prompt:
Queria saber como arreglar el error de paquetes y por que me daba ese error

### Aprendizajes Obtenidos:
- Ne