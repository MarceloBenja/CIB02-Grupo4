-- Sistema de Matricula Universitaria (CIB02, Grupo 4)
-- Crea la base de datos UNI_Matricula y la tabla Curso de la oferta academica.
-- Ejecutar completo en SQL Server Management Studio. Se puede repetir sin error.

IF DB_ID('UNI_Matricula') IS NULL
    CREATE DATABASE UNI_Matricula;
GO

USE UNI_Matricula;
GO

IF OBJECT_ID('dbo.Curso', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.Curso (
        idCurso   INT IDENTITY(1,1) CONSTRAINT PK_Curso PRIMARY KEY,
        codigo    VARCHAR(10)  NOT NULL CONSTRAINT UQ_Curso_codigo UNIQUE,
        nombre    VARCHAR(100) NOT NULL,
        creditos  INT          NOT NULL CONSTRAINT CK_Curso_creditos CHECK (creditos BETWEEN 1 AND 6),
        ciclo     INT          NOT NULL CONSTRAINT CK_Curso_ciclo CHECK (ciclo BETWEEN 1 AND 10)
    );
END
GO
