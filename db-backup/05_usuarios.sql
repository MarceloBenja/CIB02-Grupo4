-- Sistema de Matrícula Universitaria (CIB02, Grupo 4)
-- Crea la tabla Usuario para el inicio de sesión (HU-01) y cuatro usuarios de prueba, uno por rol.
-- Ejecutar completo después de 01_crear_base.sql. Se puede repetir sin error.
--
-- La contraseña nunca se guarda: solo su derivación PBKDF2-HMAC-SHA256 con una sal aleatoria
-- por usuario (clase edu.uni.datos.ClaveSegura). Los cuatro usuarios de prueba tienen la
-- contraseña Matricula2026. Existen solo para la base de ejemplo: no se crean en una base real.

SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
GO

IF DB_ID('UNI_Matricula') IS NULL
BEGIN
    RAISERROR('No existe la base UNI_Matricula. Ejecute primero 01_crear_base.sql.', 16, 1);
    SET NOEXEC ON;
END
GO

USE UNI_Matricula;
GO

-- Cuenta de acceso al sistema. El rol decide qué opciones del menú puede usar.
IF OBJECT_ID('dbo.Usuario', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.Usuario (
        idUsuario    INT IDENTITY(1,1) CONSTRAINT PK_Usuario PRIMARY KEY,
        usuario      VARCHAR(30)   NOT NULL CONSTRAINT UQ_Usuario_usuario UNIQUE,
        sal          VARBINARY(16) NOT NULL,
        claveHash    VARBINARY(32) NOT NULL,
        iteraciones  INT           NOT NULL
                     CONSTRAINT CK_Usuario_iteraciones CHECK (iteraciones >= 100000),
        rol          VARCHAR(15)   NOT NULL
                     CONSTRAINT CK_Usuario_rol
                         CHECK (rol IN ('ESTUDIANTE', 'DOCENTE', 'REGISTROS', 'ADMINISTRADOR')),
        activo       BIT           NOT NULL CONSTRAINT DF_Usuario_activo DEFAULT 1
    );
END
GO

-- Usuarios de prueba (contraseña Matricula2026). Sal y derivación generadas con ClaveSegura.
INSERT INTO dbo.Usuario (usuario, rol, sal, claveHash, iteraciones)
SELECT v.usuario, v.rol, v.sal, v.claveHash, v.iteraciones
FROM (VALUES
    ('admin', 'ADMINISTRADOR', 0x5565EA536729294D0569939269FCC5F8,
     0x72F21389FF6B6433DBD9C070F2ADD2403B0AB99BA6703DE7E97AFBED4417A81F, 600000),
    ('registros', 'REGISTROS', 0xD192896817BAC5F3E47406AE0CF40A29,
     0xDA9B1D8F73026DAEFFFCB718EFD79DB54C5A4853CFFFE4EF22CAC2726C093322, 600000),
    ('jramos', 'DOCENTE', 0x6B634C5C4BB684F204E8B74C6C68C75C,
     0x44E688D3873EEA7B19A27D229B1385DE173DC5293A4AA3BF8CD9DE1071CB979A, 600000),
    ('20261001A', 'ESTUDIANTE', 0x09C89B3F8033350D8BC461A55C7341D6,
     0x85BD411578F1884D226B354A94A796C481269AD136EE39E814EB184D583FBADA, 600000)
) AS v (usuario, rol, sal, claveHash, iteraciones)
WHERE NOT EXISTS (SELECT 1 FROM dbo.Usuario u WHERE u.usuario = v.usuario);
GO

SELECT usuario, rol, activo FROM dbo.Usuario ORDER BY idUsuario;
GO

SET NOEXEC OFF;
GO
