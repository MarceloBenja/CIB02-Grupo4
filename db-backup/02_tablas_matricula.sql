-- Sistema de Matrícula Universitaria (CIB02, Grupo 4)
-- Crea las tablas de docentes, estudiantes, secciones, horarios y matrículas.
-- Ejecutar completo en SQL Server Management Studio, después de 01_crear_base.sql.
-- Se puede repetir sin error: solo crea lo que todavía no existe.

SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
GO

-- Si falta la base o la tabla Curso, el resto del script no se ejecuta.
IF DB_ID('UNI_Matricula') IS NULL
BEGIN
    RAISERROR('No existe la base UNI_Matricula. Ejecute primero 01_crear_base.sql.', 16, 1);
    SET NOEXEC ON;
END
GO

USE UNI_Matricula;
GO

IF OBJECT_ID('dbo.Curso', 'U') IS NULL
BEGIN
    RAISERROR('No existe la tabla Curso. Ejecute primero 01_crear_base.sql.', 16, 1);
    SET NOEXEC ON;
END
GO

-- Docente que dicta las secciones. Cada docente se identifica por su correo institucional.
IF OBJECT_ID('dbo.Docente', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.Docente (
        idDocente  INT IDENTITY(1,1) CONSTRAINT PK_Docente PRIMARY KEY,
        nombres    VARCHAR(60)  NOT NULL,
        apellidos  VARCHAR(60)  NOT NULL,
        correo     VARCHAR(100) NOT NULL
                   CONSTRAINT UQ_Docente_correo UNIQUE
                   CONSTRAINT CK_Docente_correo CHECK (correo LIKE '_%@uni.edu.pe')
    );
END
GO

-- Estudiante que se matricula. El código UNI tiene 8 dígitos y una letra, por ejemplo 20261001A.
IF OBJECT_ID('dbo.Estudiante', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.Estudiante (
        idEstudiante INT IDENTITY(1,1) CONSTRAINT PK_Estudiante PRIMARY KEY,
        codigo       CHAR(9)      NOT NULL
                     CONSTRAINT UQ_Estudiante_codigo UNIQUE
                     CONSTRAINT CK_Estudiante_codigo
                         CHECK (codigo LIKE '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][A-Z]'),
        nombres      VARCHAR(60)  NOT NULL,
        apellidos    VARCHAR(60)  NOT NULL,
        correo       VARCHAR(100) NOT NULL
                     CONSTRAINT UQ_Estudiante_correo UNIQUE
                     CONSTRAINT CK_Estudiante_correo CHECK (correo LIKE '_%@uni.pe')
    );
END
GO

-- Sección de un curso en un periodo académico, con su docente y sus vacantes.
-- Cada matrícula descuenta una vacante. La restricción CK_Seccion_vacantes impide que las
-- vacantes queden negativas o superen la capacidad: la base rechaza cualquier sobrecupo (RF-01).
IF OBJECT_ID('dbo.Seccion', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.Seccion (
        idSeccion  INT IDENTITY(1,1) CONSTRAINT PK_Seccion PRIMARY KEY,
        idCurso    INT     NOT NULL CONSTRAINT FK_Seccion_Curso REFERENCES dbo.Curso (idCurso),
        idDocente  INT     NOT NULL CONSTRAINT FK_Seccion_Docente REFERENCES dbo.Docente (idDocente),
        periodo    CHAR(6) NOT NULL
                   CONSTRAINT CK_Seccion_periodo CHECK (periodo LIKE '[0-9][0-9][0-9][0-9]-[12]'),
        letra      CHAR(1) NOT NULL CONSTRAINT CK_Seccion_letra CHECK (letra LIKE '[A-Z]'),
        capacidad  INT     NOT NULL CONSTRAINT CK_Seccion_capacidad CHECK (capacidad BETWEEN 1 AND 60),
        vacantes   INT     NOT NULL,
        CONSTRAINT CK_Seccion_vacantes CHECK (vacantes BETWEEN 0 AND capacidad),
        CONSTRAINT UQ_Seccion_curso_periodo_letra UNIQUE (idCurso, periodo, letra)
    );
END
GO

-- Bloques de clase de cada sección. diaSemana va de 1 (lunes) a 6 (sábado).
-- Sirven para mostrar la oferta (CU-01) y para detectar cruces de horario (CU-10).
IF OBJECT_ID('dbo.Horario', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.Horario (
        idHorario   INT IDENTITY(1,1) CONSTRAINT PK_Horario PRIMARY KEY,
        idSeccion   INT     NOT NULL
                    CONSTRAINT FK_Horario_Seccion REFERENCES dbo.Seccion (idSeccion) ON DELETE CASCADE,
        diaSemana   TINYINT NOT NULL CONSTRAINT CK_Horario_dia CHECK (diaSemana BETWEEN 1 AND 6),
        horaInicio  TIME(0) NOT NULL,
        horaFin     TIME(0) NOT NULL,
        CONSTRAINT CK_Horario_horas CHECK (horaInicio < horaFin),
        CONSTRAINT UQ_Horario_bloque UNIQUE (idSeccion, diaSemana, horaInicio)
    );
END
GO

-- Matrícula de un estudiante en una sección. Al anularla (RF-04) la fila no se borra:
-- cambia a ANULADA para conservar el historial.
IF OBJECT_ID('dbo.Matricula', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.Matricula (
        idMatricula    INT IDENTITY(1,1) CONSTRAINT PK_Matricula PRIMARY KEY,
        idEstudiante   INT          NOT NULL
                       CONSTRAINT FK_Matricula_Estudiante REFERENCES dbo.Estudiante (idEstudiante),
        idSeccion      INT          NOT NULL
                       CONSTRAINT FK_Matricula_Seccion REFERENCES dbo.Seccion (idSeccion),
        fechaRegistro  DATETIME2(0) NOT NULL CONSTRAINT DF_Matricula_fecha DEFAULT SYSDATETIME(),
        estado         VARCHAR(8)   NOT NULL CONSTRAINT DF_Matricula_estado DEFAULT 'ACTIVA'
                       CONSTRAINT CK_Matricula_estado CHECK (estado IN ('ACTIVA', 'ANULADA'))
    );
END
GO

-- Un estudiante no puede tener dos matrículas activas en la misma sección.
-- Es un índice único filtrado: una matrícula ANULADA no impide volver a matricularse.
IF NOT EXISTS (SELECT 1 FROM sys.indexes
               WHERE name = 'UX_Matricula_activa' AND object_id = OBJECT_ID('dbo.Matricula'))
BEGIN
    CREATE UNIQUE INDEX UX_Matricula_activa
        ON dbo.Matricula (idEstudiante, idSeccion)
        WHERE estado = 'ACTIVA';
END
GO

SET NOEXEC OFF;
GO
