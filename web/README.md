# Prototipo del portal web

Prototipo funcional de la vista **Oferta académica** del portal web del estudiante, que corresponde al caso de uso CU-01 (Consultar oferta académica). El portal complementa a la aplicación de escritorio: el estudiante consulta desde el navegador y la oficina de registros sigue trabajando en JavaFX, ambos sobre la misma base de datos `UNI_Matricula`.

## Cómo abrirlo

Abra `web/oferta/index.html` con doble clic en cualquier navegador actual. No necesita servidor ni instalación: los datos llegan desde `api-simulada.js`.

## Un archivo por responsabilidad

| Archivo | Responsabilidad |
|---|---|
| `oferta/index.html` | Estructura y significado: encabezado, navegación, formulario de búsqueda y tabla con encabezados de columna. |
| `oferta/estilos.css` | Presentación: colores, espaciado y diseño adaptable; en pantallas angostas cada fila se ve como tarjeta. |
| `oferta/app.js` | Comportamiento: carga la oferta, aplica los filtros sin recargar la página y dibuja la tabla. |
| `oferta/api-simulada.js` | Imita la respuesta del servidor a `GET /api/v1/oferta?periodo=2026-2`. |

## Relación con la API

En el sistema real, `obtenerOferta` de `api-simulada.js` se reemplaza por una llamada al backend en Java (Spring Boot):

```
GET /api/v1/oferta?periodo=2026-2
Authorization: Bearer <token>

200 OK
{ "periodo": "2026-2", "secciones": [ { "codigo": "CIB02", "seccion": "A", "vacantes": 4, ... } ] }
```

Los cursos, secciones y docentes del prototipo son datos de ejemplo. Los filtros del navegador son solo comodidad: las reglas de negocio y la autorización se aplican siempre en el servidor.
