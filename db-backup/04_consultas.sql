-- Sistema de Matrícula Universitaria (CIB02, Grupo 4)
-- Regla de matrícula con control de vacantes y consultas principales del sistema.
-- Ejecutar completo después de 03_datos_ejemplo.sql.

SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
SET XACT_ABORT ON;  -- ante cualquier error, la transacción se deshace completa
GO

USE UNI_Matricula;
GO

-- 1. Regla RF-01: matricular solo si la sección tiene vacantes.
--    El UPDATE descuenta la vacante únicamente si todavía queda alguna (vacantes > 0) y el
--    INSERT registra la matrícula. Ambos van en una sola transacción: si dos estudiantes piden
--    la última vacante al mismo tiempo, solo uno la obtiene y nunca hay sobrecupo.
--    Ejemplo: Camila Ríos (20241005E) se matricula en Física I, sección A.
DECLARE @codigoEstudiante CHAR(9)    = '20241005E';
DECLARE @codigoCurso      VARCHAR(10) = 'BFI01';
DECLARE @letra            CHAR(1)    = 'A';

DECLARE @idEstudiante INT = (SELECT idEstudiante FROM dbo.Estudiante WHERE codigo = @codigoEstudiante);
DECLARE @idSeccion INT = (SELECT s.idSeccion
                          FROM dbo.Seccion s
                          JOIN dbo.Curso c ON c.idCurso = s.idCurso
                          WHERE c.codigo = @codigoCurso AND s.letra = @letra AND s.periodo = '2026-2');

IF @idEstudiante IS NULL OR @idSeccion IS NULL
BEGIN
    PRINT 'No existe el estudiante o la sección. Ejecute antes 03_datos_ejemplo.sql.';
END
ELSE IF EXISTS (SELECT 1 FROM dbo.Matricula
                WHERE idEstudiante = @idEstudiante AND idSeccion = @idSeccion AND estado = 'ACTIVA')
BEGIN
    PRINT 'El estudiante ya está matriculado en esta sección.';
END
ELSE
BEGIN
    BEGIN TRANSACTION;

    UPDATE dbo.Seccion
    SET vacantes = vacantes - 1
    WHERE idSeccion = @idSeccion AND vacantes > 0;

    IF @@ROWCOUNT = 1
    BEGIN
        INSERT INTO dbo.Matricula (idEstudiante, idSeccion) VALUES (@idEstudiante, @idSeccion);
        COMMIT TRANSACTION;
        PRINT 'Matrícula registrada: la sección tiene una vacante menos.';
    END
    ELSE
    BEGIN
        ROLLBACK TRANSACTION;
        PRINT 'Matrícula rechazada: la sección ya no tiene vacantes.';
    END
END

SELECT c.codigo AS curso, s.letra AS seccion, s.vacantes, s.capacidad
FROM dbo.Seccion s
JOIN dbo.Curso c ON c.idCurso = s.idCurso
WHERE s.idSeccion = @idSeccion;
GO

-- 2. La misma regla en una sección llena: BMA02 A tiene 0 vacantes, así que el UPDATE
--    no encuentra ninguna fila que cumpla "vacantes > 0" y no cambia nada (0 filas).
--    Con 0 filas actualizadas, la matrícula se rechaza.
UPDATE s
SET s.vacantes = s.vacantes - 1
FROM dbo.Seccion s
JOIN dbo.Curso c ON c.idCurso = s.idCurso
WHERE c.codigo = 'BMA02' AND s.letra = 'A' AND s.periodo = '2026-2' AND s.vacantes > 0;

SELECT @@ROWCOUNT AS filas_actualizadas;
GO

-- 3. Consulta con JOIN: lista de matriculados de cada sección (CU-05), con curso y docente.
SELECT c.codigo AS curso,
       s.letra AS seccion,
       d.apellidos + ', ' + d.nombres AS docente,
       e.codigo AS codigo_estudiante,
       e.apellidos + ', ' + e.nombres AS estudiante
FROM dbo.Matricula m
JOIN dbo.Estudiante e ON e.idEstudiante = m.idEstudiante
JOIN dbo.Seccion s ON s.idSeccion = m.idSeccion
JOIN dbo.Curso c ON c.idCurso = s.idCurso
JOIN dbo.Docente d ON d.idDocente = s.idDocente
WHERE m.estado = 'ACTIVA' AND s.periodo = '2026-2'
ORDER BY c.codigo, s.letra, e.apellidos;
GO

-- 4. Consulta con agregación: cursos y créditos matriculados por estudiante en el periodo.
--    Sirve para controlar el límite de créditos por ciclo (HU-04.3).
SELECT e.codigo AS codigo_estudiante,
       e.apellidos + ', ' + e.nombres AS estudiante,
       COUNT(*) AS cursos,
       SUM(c.creditos) AS creditos
FROM dbo.Matricula m
JOIN dbo.Estudiante e ON e.idEstudiante = m.idEstudiante
JOIN dbo.Seccion s ON s.idSeccion = m.idSeccion
JOIN dbo.Curso c ON c.idCurso = s.idCurso
WHERE m.estado = 'ACTIVA' AND s.periodo = '2026-2'
GROUP BY e.codigo, e.apellidos, e.nombres
ORDER BY creditos DESC, e.codigo;
GO

-- 5. Oferta académica del periodo (CU-01), con vacantes y horario de cada sección.
--    Devuelve lo mismo que muestra el prototipo web.
SELECT c.codigo,
       c.nombre AS curso,
       c.ciclo,
       c.creditos,
       s.letra AS seccion,
       d.apellidos + ', ' + d.nombres AS docente,
       CONCAT(s.vacantes, ' de ', s.capacidad) AS vacantes,
       STRING_AGG(CONCAT(CHOOSE(h.diaSemana, 'Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb'), ' ',
                         CONVERT(CHAR(5), h.horaInicio, 108), '-',
                         CONVERT(CHAR(5), h.horaFin, 108)), ', ')
           WITHIN GROUP (ORDER BY h.diaSemana, h.horaInicio) AS horario
FROM dbo.Seccion s
JOIN dbo.Curso c ON c.idCurso = s.idCurso
JOIN dbo.Docente d ON d.idDocente = s.idDocente
LEFT JOIN dbo.Horario h ON h.idSeccion = s.idSeccion
WHERE s.periodo = '2026-2'
GROUP BY c.codigo, c.nombre, c.ciclo, c.creditos, s.letra, d.apellidos, d.nombres, s.vacantes, s.capacidad
ORDER BY c.ciclo, c.codigo, s.letra;
GO
