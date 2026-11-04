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

## Cómo ejecutarlo

1. Crear la base de datos `UNI_Matricula` en SQL Server: restaurar el backup o ejecutar los scripts de la carpeta `db-backup/` (ver la sección siguiente).
2. Copiar `db.properties.example` como `db.properties`, en la misma carpeta, y escribir el usuario y la contraseña de su instancia. Ese archivo está en `.gitignore` y **nunca** se sube al repositorio.
3. Abrir la carpeta en IntelliJ IDEA, que la reconoce como proyecto Maven, con el JDK 21.
4. Ejecutar la clase `edu.uni.Launcher`, o desde la terminal:

   ```
   mvnw.cmd javafx:run      (Windows)
   ./mvnw javafx:run        (Linux o macOS)
   ```

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
