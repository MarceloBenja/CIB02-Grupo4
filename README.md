# Sistema de Matrícula Universitaria

Aplicación de escritorio para registrar la oferta académica de una facultad y la matrícula de sus estudiantes. Es el proyecto del **Grupo 4** del curso Ingeniería de Software (CIB02), Facultad de Ingeniería Eléctrica y Electrónica, Universidad Nacional de Ingeniería, ciclo 2026-2. Docente: Ing. M.I.A. Homar Dávila Padilla.

## Integrantes

- Arias Marin, Angelo Jhoel
- Choque Huaman, Marco Antonio
- Tiburcio Huamani, Marcelo Benjamín
- Zumaeta Yldefonso, Marvin Teddy

## Tecnología

| Componente | Versión |
|---|---|
| Lenguaje | Java, JDK 21 (21.0.12) |
| Interfaz | JavaFX 21 con vistas FXML (Scene Builder) |
| Construcción | Maven 3.8.5 mediante el Maven Wrapper (`mvnw`) |
| Base de datos | Microsoft SQL Server, controlador JDBC `mssql-jdbc` 12.6.2.jre11 |
| Entorno de desarrollo | IntelliJ IDEA 2026.2.3 (las guías usan la 2025.3.6.1; el proyecto es Maven y abre igual en ambas) |

El código sigue el patrón Modelo-Vista-Controlador, con una capa de acceso a datos:

| Paquete o carpeta | Contenido |
|---|---|
| `edu.uni.modelo` | Clases del dominio: cursos, secciones, estudiantes, matrículas |
| `edu.uni.controlador` | Un controlador por pantalla |
| `edu.uni.datos` | Conexión y clases DAO; todas las consultas usan `PreparedStatement` |
| `src/main/resources/edu/uni/vista` | Vistas `.fxml` |

## Estructura del repositorio

```
CIB02-Grupo4/
├── src/                    código fuente Java (proyecto Maven)
├── db-backup/              respaldo .bak de la base de datos y scripts de creación
├── docs/                   documento de sustentación y modelo de Visual Paradigm
├── db.properties.example   formato del archivo de conexión
├── pom.xml
└── README.md
```

## Configuración del entorno

Siga estos pasos en orden la primera vez que trabaje en una computadora. Con los pasos 1 a 3 el proyecto ya compila; los pasos 4 a 7 hacen falta para ejecutarlo con la base de datos.

### 1. Instalar las herramientas

| Herramienta | Versión que usa el equipo | Para qué sirve |
|---|---|---|
| JDK | 21 (21.0.12, Microsoft Build of OpenJDK) | Compilar y ejecutar. IntelliJ puede descargarlo en *File*, *Project Structure*, *SDKs*, *Add SDK*, *Download JDK*. |
| IntelliJ IDEA | 2026.2.3 | Editar, depurar y ejecutar el proyecto. |
| Git | 2.56 | Clonar el repositorio y registrar commits. |
| SQL Server | 2025 Express (también sirve la edición Developer) | Base de datos `UNI_Matricula`. |
| SQL Server Management Studio | La instalada en la guía 1 | Ejecutar los scripts y restaurar el respaldo. |
| Scene Builder (opcional) | La instalada en la Práctica 03A | Editar las vistas FXML de forma visual. |

No hace falta instalar Maven: el repositorio trae el Maven Wrapper (`mvnw`), que descarga Maven 3.8.5 la primera vez que se usa.

### 2. Clonar el repositorio

```
git clone https://github.com/MarceloBenja/CIB02-Grupo4.git
cd CIB02-Grupo4
```

### 3. Compilar desde la terminal

El Maven Wrapper necesita que la variable `JAVA_HOME` apunte al JDK 21. Si IntelliJ descargó el JDK, está en `C:\Users\<usuario>\.jdks\`.

En PowerShell (Windows):

```
$env:JAVA_HOME = "C:\Users\<usuario>\.jdks\<carpeta-del-jdk-21>"
.\mvnw.cmd -B verify
```

En Git Bash, Linux o macOS:

```
export JAVA_HOME=/ruta/al/jdk-21
./mvnw -B verify
```

La compilación termina con `BUILD SUCCESS` y deja el archivo `target/sistema-matricula-1.0-SNAPSHOT.jar`. Es el mismo comando que ejecuta GitHub Actions en cada solicitud de fusión.

### 4. Abrir el proyecto en IntelliJ IDEA

1. *File*, *Open* y elegir la carpeta `CIB02-Grupo4`. IntelliJ la reconoce como proyecto Maven por el `pom.xml`.
2. En *File*, *Project Structure*, *Project*, elegir el SDK 21.
3. Si las dependencias no aparecen, abrir la ventana *Maven* y pulsar *Reload All Maven Projects*.

### 5. Preparar SQL Server (una vez por computadora)

La aplicación se conecta por JDBC al puerto 1433 con usuario y contraseña, como en la guía 5. Una instalación nueva de SQL Server Express suele traer solo la autenticación de Windows y el protocolo TCP/IP desactivado, así que hay que hacer estos cambios:

1. **Autenticación mixta.** En SSMS, clic derecho en el servidor, *Propiedades* (*Properties*), *Seguridad* (*Security*), y marcar *Modo de autenticación de SQL Server y de Windows*.
2. **Usuario `sa`.** En una consulta nueva de SSMS:

   ```sql
   ALTER LOGIN sa WITH PASSWORD = 'escriba_una_contraseña_segura';
   ALTER LOGIN sa ENABLE;
   ```

3. **TCP/IP en el puerto 1433.** En *SQL Server Configuration Manager*, *Configuración de red de SQL Server*, *Protocolos de SQLEXPRESS*: habilitar *TCP/IP*. En sus propiedades, pestaña *Direcciones IP*, sección *IPAll*, dejar vacío *Puertos dinámicos TCP* y escribir `1433` en *Puerto TCP*.
4. **Reiniciar** el servicio *SQL Server (SQLEXPRESS)* para aplicar los cambios.
5. **Crear la base `UNI_Matricula`.** Restaurar el respaldo `.bak` (ver "Backup de la base de datos") o ejecutar en SSMS los scripts `.sql` de la carpeta `db-backup/`, en orden numérico.

### 6. Configurar la conexión

```
copy db.properties.example db.properties      (Windows)
cp db.properties.example db.properties        (Linux o macOS)
```

Abrir `db.properties` y escribir en `db.clave` la contraseña del paso 5. Si su instancia usa otro puerto, cambiarlo en `db.url`. Este archivo está en `.gitignore` y **nunca** se sube al repositorio.

### 7. Ejecutar la aplicación

- En IntelliJ: ejecutar la clase `edu.uni.Launcher`.
- Desde la terminal, en la carpeta del proyecto: `.\mvnw.cmd javafx:run` (Windows) o `./mvnw javafx:run` (Linux o macOS).

La aplicación busca `db.properties` en la carpeta desde la que se ejecuta, que debe ser la carpeta del proyecto.

### Problemas frecuentes

| Mensaje | Causa | Solución |
|---|---|---|
| `Error: JAVA_HOME not found in your environment.` | La variable `JAVA_HOME` no está definida. | Paso 3. |
| `Error: JAVA_HOME is set to an invalid directory.` | `JAVA_HOME` apunta a una carpeta que no es un JDK. | Corregir la ruta del paso 3. |
| `El término 'mvnw.cmd' no se reconoce...` | En PowerShell falta `.\` delante del comando. | Escribir `.\mvnw.cmd`. |
| `No se encontró ...\db.properties` (en la consola) | Falta el archivo o la aplicación se ejecutó desde otra carpeta. | Paso 6 y ejecutar desde la carpeta del proyecto. |
| `The TCP/IP connection to the host localhost, port 1433 has failed` | TCP/IP desactivado o servicio detenido. | Paso 5, puntos 3 y 4. |
| `Login failed for user 'sa'` | Autenticación mixta desactivada o contraseña distinta. | Paso 5, puntos 1 y 2, y revisar `db.clave`. |
| Un cambio en un `.fxml` no aparece al ejecutar | Quedó una versión compilada anterior. | `.\mvnw.cmd clean verify` o *Build*, *Rebuild Project*. |

## Backup de la base de datos

El respaldo de la base `UNI_Matricula` se guarda en la carpeta [`db-backup/`](db-backup/) con el nombre `UNI_Matricula.bak`. Para restaurarlo en SQL Server Management Studio: clic derecho en *Bases de datos*, *Restaurar base de datos*, *Dispositivo* y seleccionar el archivo. Si el respaldo supera el límite de GitHub, la carpeta contendrá un `RUTA.txt` con el enlace de descarga.

## Flujo de ramas: GitHub Flow

El equipo trabaja con **GitHub Flow**: una sola rama permanente, `main`, y una rama corta por cada cambio, que entra a `main` mediante una solicitud de fusión revisada por otro integrante. Es la opción que la Semana 9 del curso recomienda para un proyecto de este tamaño. GitFlow se descartó porque sus ramas `develop` y `release` solo se justifican cuando hay que sostener varias versiones a la vez.

### Ramas

| Rama | Uso | Ejemplo |
|---|---|---|
| `main` | Rama principal. Siempre compila y funciona. Nadie hace commits directos en ella. | |
| `feature/<tema>` | Funcionalidad nueva | `feature/crud-curso` |
| `bugfix/<tema>` | Corrección de un defecto | `bugfix/validar-creditos` |
| `hotfix/<tema>` | Corrección urgente sobre lo ya entregado | `hotfix/conexion-bd` |
| `docs/<tema>` | Cambios solo de documentación | `docs/avance-semana10` |

Los nombres van en minúsculas, sin tildes y con las palabras separadas por guiones.

### Ciclo de cada cambio

1. Actualizar `main` y crear la rama desde ahí:

   ```
   git switch main
   git pull
   git switch -c feature/<tema>
   ```

2. Trabajar con commits atómicos (ver la convención). Si la rama dura más de un día, traer los cambios de `main` con `git merge main`.
3. Publicar la rama: `git push -u origin feature/<tema>`.
4. Abrir en GitHub una solicitud de fusión (*pull request*) hacia `main`, completar la plantilla y asignar como revisor a otro integrante.
5. GitHub Actions compila el proyecto. Si la compilación falla, la solicitud no se fusiona.
6. El revisor lee los cambios, comenta y aprueba. El autor nunca aprueba su propia solicitud.
7. Fusionar con **Create a merge commit**, que conserva la historia real de la rama, y borrar la rama. No se usa `rebase` sobre ramas ya publicadas.

La rama `main` está protegida en GitHub: exige una solicitud de fusión con al menos una aprobación. La única excepción fue la configuración inicial del repositorio, hecha antes de que existiera el remoto.

### Convención de commits

- Un commit, un cambio coherente: compila por sí solo y se puede revertir por separado.
- Asunto en español, en imperativo y de 60 caracteres como máximo, que diga **qué** cambia. Ejemplo: `Agrega la pantalla de mantenimiento de cursos`.
- Cuerpo, separado por una línea en blanco, que explique **por qué** se hizo el cambio.
- No se aceptan mensajes como `cambios`, `arreglos varios` o `update`, ni el nombre del archivo como mensaje.
- Nunca se confirman `db.properties`, contraseñas, tokens ni archivos generados como `target/`.

Desde la terminal, el asunto y el cuerpo se escriben así:

```
git commit -m "Asunto en imperativo" -m "Por qué se hizo el cambio."
```

## Guía de codificación

El código sigue la guía del equipo de la Semana 7, basada en Google Java Style y SEI CERT para Java: llaves siempre, nombres en español sin tildes, sin valores mágicos, ningún error ignorado y consultas SQL solo con parámetros.
