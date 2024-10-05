# API de Operaciones

## Descripción
Operaciones es una API REST para BTG, construida usando Spring Boot. Proporciona varias funcionalidades, incluyendo integración con MongoDB, envío de correos electrónicos a través de JavaMailSender y envío de SMS a través de Twilio.

## Prerrequisitos
- Java 17
- Maven
- MongoDB
- Cuenta de AWS

## Despliegue Local

### Paso 1: Clonar el Repositorio
```sh
git clone https://github.com/holsaf/OperacionesBtg.git
cd operaciones
```

### Paso 2: Configurar Variables de Entorno
1. Editar las variables en el archivo `application.properties` en `src/main/resources`.
2. Para Twilio, revisar este tutorial: [Twilio](https://help.twilio.com/articles/14726256820123-What-is-a-Twilio-Account-SID-and-where-can-I-find-it-).
3. Para SMTP, revisar este tutorial: [SMTP](https://support.google.com/mail/answer/185833?hl=es-419#zippy=%2Cpor-qu%C3%A9-es-posible-que-necesites-una-contrase%C3%B1a-de-la-aplicaci%C3%B3n).
4. Para MongoDB, revisar la sección de configuración de MongoDB Atlas más adelante.

### Paso 3: Construir el Proyecto
```sh
mvn clean install
```

### Paso 4: Ejecutar la Aplicación
```sh
mvn spring-boot:run
```

## Configurar MongoDB Atlas

### Crear un Cluster Gratis
1. Ve a [MongoDB Atlas](https://www.mongodb.com/cloud/atlas).
2. Regístrate para una cuenta gratuita o inicia sesión si ya tienes una.
3. Haz clic en "Build a Cluster" y selecciona el nivel gratuito.
4. Sigue las indicaciones para crear tu cluster.

### Configurar Acceso a la Base de Datos
1. Una vez creado tu cluster, ve a la pestaña "Database Access".
2. Haz clic en "Add New Database User".
3. Establece un nombre de usuario y una contraseña para tu usuario de base de datos.
4. Asigna los roles necesarios y haz clic en "Add User".

### Lista Blanca de tu Dirección IP
1. Ve a la pestaña "Network Access".
2. Haz clic en "Add IP Address".
3. Agrega tu dirección IP o permite el acceso desde cualquier lugar agregando `0.0.0.0/0`.

### Obtener tu Cadena de Conexión
1. Ve a la pestaña "Clusters".
2. Haz clic en "Connect" para tu cluster.
3. Selecciona "Connect your application".
4. Copia la cadena de conexión proporcionada y reemplaza `<username>` y `<password>` con las credenciales de tu usuario de base de datos.

## Despliegue con AWS CloudFormation

### Paso 1: Empaquetar la Aplicación
Genera el archivo `.JAR` de la aplicación con todas las dependencias. Ten cuidado con la configuración de codificación del IDE y la versión de Java de tu IDE.

### Paso 2: Crear una Plantilla de CloudFormation
Usa un archivo `cloudformation-template.yaml` con los recursos necesarios de AWS, como instancias EC2, buckets S3 y roles IAM.

### Paso 3: Crear una Cuenta AWS
Abre tu cuenta gratuita de AWS, que te permite usar servicios de forma gratuita inicialmente.

### Paso 4: Crear un Servicio KeyPair
Crea inicialmente un servicio de KeyPair en AWS.

### Paso 5: Crear un Bucket S3
Crea un bucket S3 básico, donde puedas cargar el archivo `cloudformation-template.yaml` y el archivo `.jar` de la aplicación.

### Paso 6: Desplegar CloudFormation
En la consola, ve a la sección de CloudFormation y crea una pila usando el archivo `cloudformation-template.yaml` cargado desde el S3, y sigue los pasos.

### Paso 7: Acceder a la Instancia
Si el proceso terminó con éxito, ve a la sección de EC2 y consulta la IP pública de la instancia creada. Ya puedes consumir el microservicio de esta manera:
`Por Ejemplo: http://54.84.15.157:8080/api/transacciones`

## Paso 8: Consumir la API
Se adjunta una colección de Postman en el archivo `Operaciones.postman_collection.json` para probar la API.
```