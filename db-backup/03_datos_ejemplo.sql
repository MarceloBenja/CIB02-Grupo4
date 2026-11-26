-- Sistema de Matrícula Universitaria (CIB02, Grupo 4)
-- Datos de ejemplo del periodo 2026-2: los mismos cursos, docentes y secciones que muestra el
-- prototipo web (web/oferta). Todas las personas son ficticias.
-- Ejecutar completo después de 02_tablas_matricula.sql.
-- Se puede repetir sin error: cada INSERT agrega solo las filas que todavía no existen.
--
-- Las vacantes de cada sección son las que quedan después de toda la matrícula del periodo.
-- Aquí solo se cargan las matrículas de ocho estudiantes de ejemplo, no las de todo el periodo.

SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
GO

USE UNI_Matricula;
GO

DECLARE @periodo CHAR(6) = '2026-2';

-- Cursos de la oferta (si ya se registraron desde la pantalla de cursos, no se duplican).
INSERT INTO dbo.Curso (codigo, nombre, creditos, ciclo)
SELECT v.codigo, v.nombre, v.creditos, v.ciclo
FROM (VALUES
    ('BMA01', 'Cálculo Diferencial',           5, 1),
    ('BIC01', 'Introducción a la Computación', 3, 1),
    ('BFI01', 'Física I',                      5, 2),
    ('BMA02', 'Cálculo Integral',              5, 2),
    ('EE306', 'Circuitos Eléctricos I',        4, 4),
    ('CIB02', 'Ingeniería de Software',        3, 8),
    ('EE458', 'Sistemas de Control Digital',   3, 8)
) AS v (codigo, nombre, creditos, ciclo)
WHERE NOT EXISTS (SELECT 1 FROM dbo.Curso c WHERE c.codigo = v.codigo);

-- Docentes.
INSERT INTO dbo.Docente (nombres, apellidos, correo)
SELECT v.nombres, v.apellidos, v.correo
FROM (VALUES
    ('Julia',  'Ramos Ortiz',    'jramos@uni.edu.pe'),
    ('Luis',   'Paredes Soto',   'lparedes@uni.edu.pe'),
    ('Ana',    'Flores Quiroz',  'aflores@uni.edu.pe'),
    ('Raúl',   'Mendoza Arce',   'rmendoza@uni.edu.pe'),
    ('Pedro',  'Castro Núñez',   'pcastro@uni.edu.pe'),
    ('Rosa',   'Vega Campos',    'rvega@uni.edu.pe'),
    ('Miguel', 'Salazar Prado',  'msalazar@uni.edu.pe'),
    ('Elena',  'Rojas Valdivia', 'erojas@uni.edu.pe')
) AS v (nombres, apellidos, correo)
WHERE NOT EXISTS (SELECT 1 FROM dbo.Docente d WHERE d.correo = v.correo);

-- Estudiantes: dos de primer ciclo, dos de segundo, dos de cuarto y dos de octavo.
INSERT INTO dbo.Estudiante (codigo, nombres, apellidos, correo)
SELECT v.codigo, v.nombres, v.apellidos, v.correo
FROM (VALUES
    ('20261001A', 'Lucía',   'Huamán Torres',   'lucia.huaman.t@uni.pe'),
    ('20261002B', 'Diego',   'Condori Salas',   'diego.condori.s@uni.pe'),
    ('20252003C', 'Valeria', 'Ccori Medina',    'valeria.ccori.m@uni.pe'),
    ('20252004D', 'Jorge',   'Paucar Linares',  'jorge.paucar.l@uni.pe'),
    ('20241005E', 'Camila',  'Ríos Ventura',    'camila.rios.v@uni.pe'),
    ('20241006F', 'Renzo',   'Chávez Aliaga',   'renzo.chavez.a@uni.pe'),
    ('20221007G', 'Sofía',   'Mamani Herrera',  'sofia.mamani.h@uni.pe'),
    ('20221008H', 'Bruno',   'Tapia Gutiérrez', 'bruno.tapia.g@uni.pe')
) AS v (codigo, nombres, apellidos, correo)
WHERE NOT EXISTS (SELECT 1 FROM dbo.Estudiante e WHERE e.codigo = v.codigo);

-- Secciones del periodo. BMA01 B y BMA02 A ya no tienen vacantes.
INSERT INTO dbo.Seccion (idCurso, idDocente, periodo, letra, capacidad, vacantes)
SELECT c.idCurso, d.idDocente, @periodo, v.letra, v.capacidad, v.vacantes
FROM (VALUES
    ('BMA01', 'A', 'jramos@uni.edu.pe',   40, 12),
    ('BMA01', 'B', 'lparedes@uni.edu.pe', 40,  0),
    ('BIC01', 'A', 'aflores@uni.edu.pe',  35,  3),
    ('BFI01', 'A', 'rmendoza@uni.edu.pe', 40, 18),
    ('BMA02', 'A', 'jramos@uni.edu.pe',   40,  0),
    ('EE306', 'A', 'pcastro@uni.edu.pe',  30,  7),
    ('EE306', 'B', 'rvega@uni.edu.pe',    30, 25),
    ('CIB02', 'A', 'msalazar@uni.edu.pe', 35,  4),
    ('EE458', 'A', 'erojas@uni.edu.pe',   30, 10)
) AS v (curso, letra, docente, capacidad, vacantes)
JOIN dbo.Curso c ON c.codigo = v.curso
JOIN dbo.Docente d ON d.correo = v.docente
WHERE NOT EXISTS (SELECT 1 FROM dbo.Seccion s
                  WHERE s.idCurso = c.idCurso AND s.periodo = @periodo AND s.letra = v.letra);

-- Horario de cada sección: dos bloques por semana (1 = lunes ... 6 = sábado).
INSERT INTO dbo.Horario (idSeccion, diaSemana, horaInicio, horaFin)
SELECT s.idSeccion, v.dia, v.inicio, v.fin
FROM (VALUES
    ('BMA01', 'A', 1, '08:00', '10:00'), ('BMA01', 'A', 3, '08:00', '10:00'),
    ('BMA01', 'B', 2, '14:00', '16:00'), ('BMA01', 'B', 4, '14:00', '16:00'),
    ('BIC01', 'A', 1, '10:00', '12:00'), ('BIC01', 'A', 3, '10:00', '11:00'),
    ('BFI01', 'A', 2, '08:00', '10:00'), ('BFI01', 'A', 4, '08:00', '10:00'),
    ('BMA02', 'A', 1, '14:00', '16:00'), ('BMA02', 'A', 3, '14:00', '16:00'),
    ('EE306', 'A', 2, '10:00', '12:00'), ('EE306', 'A', 5, '10:00', '12:00'),
    ('EE306', 'B', 3, '16:00', '18:00'), ('EE306', 'B', 5, '16:00', '18:00'),
    ('CIB02', 'A', 4, '18:00', '20:00'), ('CIB02', 'A', 6, '08:00', '10:00'),
    ('EE458', 'A', 1, '18:00', '20:00'), ('EE458', 'A', 3, '18:00', '20:00')
) AS v (curso, letra, dia, inicio, fin)
JOIN dbo.Curso c ON c.codigo = v.curso
JOIN dbo.Seccion s ON s.idCurso = c.idCurso AND s.periodo = @periodo AND s.letra = v.letra
WHERE NOT EXISTS (SELECT 1 FROM dbo.Horario h
                  WHERE h.idSeccion = s.idSeccion AND h.diaSemana = v.dia AND h.horaInicio = v.inicio);

-- Matrículas sin cruces de horario. Renzo anuló EE306 A y se matriculó en EE306 B.
INSERT INTO dbo.Matricula (idEstudiante, idSeccion, estado)
SELECT e.idEstudiante, s.idSeccion, v.estado
FROM (VALUES
    ('20261001A', 'BMA01', 'A', 'ACTIVA'),  ('20261001A', 'BIC01', 'A', 'ACTIVA'),
    ('20261002B', 'BMA01', 'B', 'ACTIVA'),  ('20261002B', 'BIC01', 'A', 'ACTIVA'),
    ('20252003C', 'BFI01', 'A', 'ACTIVA'),  ('20252003C', 'BMA02', 'A', 'ACTIVA'),
    ('20252004D', 'BFI01', 'A', 'ACTIVA'),  ('20252004D', 'BMA02', 'A', 'ACTIVA'),
    ('20241005E', 'EE306', 'A', 'ACTIVA'),
    ('20241006F', 'EE306', 'A', 'ANULADA'), ('20241006F', 'EE306', 'B', 'ACTIVA'),
    ('20241006F', 'BMA02', 'A', 'ACTIVA'),
    ('20221007G', 'CIB02', 'A', 'ACTIVA'),  ('20221007G', 'EE458', 'A', 'ACTIVA'),
    ('20221008H', 'CIB02', 'A', 'ACTIVA'),  ('20221008H', 'EE458', 'A', 'ACTIVA')
) AS v (estudiante, curso, letra, estado)
JOIN dbo.Estudiante e ON e.codigo = v.estudiante
JOIN dbo.Curso c ON c.codigo = v.curso
JOIN dbo.Seccion s ON s.idCurso = c.idCurso AND s.periodo = @periodo AND s.letra = v.letra
WHERE NOT EXISTS (SELECT 1 FROM dbo.Matricula m
                  WHERE m.idEstudiante = e.idEstudiante AND m.idSeccion = s.idSeccion);
GO

-- Comprobación: cantidad de filas de cada tabla.
SELECT 'Curso' AS tabla, COUNT(*) AS filas FROM dbo.Curso
UNION ALL SELECT 'Docente', COUNT(*) FROM dbo.Docente
UNION ALL SELECT 'Estudiante', COUNT(*) FROM dbo.Estudiante
UNION ALL SELECT 'Seccion', COUNT(*) FROM dbo.Seccion
UNION ALL SELECT 'Horario', COUNT(*) FROM dbo.Horario
UNION ALL SELECT 'Matricula', COUNT(*) FROM dbo.Matricula;
GO
