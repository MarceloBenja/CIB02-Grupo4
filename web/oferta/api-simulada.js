'use strict';

/*
 * Simula la respuesta del backend a GET /api/v1/oferta?periodo=2026-2.
 * Permite probar la vista sin servidor: basta abrir index.html en el navegador.
 * En el sistema real, obtenerOferta se reemplaza por una llamada al servidor:
 *
 *   fetch(`/api/v1/oferta?periodo=${periodo}`, { headers: { Authorization: `Bearer ${token}` } })
 *     .then((respuesta) => respuesta.json());
 *
 * Los cursos, secciones y docentes son datos de ejemplo.
 */
function obtenerOferta(periodo) {
  const respuesta = {
    periodo: periodo,
    secciones: [
      { codigo: 'BMA01', curso: 'Cálculo Diferencial', ciclo: 1, creditos: 5, seccion: 'A',
        docente: 'Ramos, Julia', vacantes: 12, capacidad: 40,
        horario: [{ dia: 'Lun', inicio: '08:00', fin: '10:00' }, { dia: 'Mié', inicio: '08:00', fin: '10:00' }] },
      { codigo: 'BMA01', curso: 'Cálculo Diferencial', ciclo: 1, creditos: 5, seccion: 'B',
        docente: 'Paredes, Luis', vacantes: 0, capacidad: 40,
        horario: [{ dia: 'Mar', inicio: '14:00', fin: '16:00' }, { dia: 'Jue', inicio: '14:00', fin: '16:00' }] },
      { codigo: 'BIC01', curso: 'Introducción a la Computación', ciclo: 1, creditos: 3, seccion: 'A',
        docente: 'Flores, Ana', vacantes: 3, capacidad: 35,
        horario: [{ dia: 'Lun', inicio: '10:00', fin: '12:00' }, { dia: 'Mié', inicio: '10:00', fin: '11:00' }] },
      { codigo: 'BFI01', curso: 'Física I', ciclo: 2, creditos: 5, seccion: 'A',
        docente: 'Mendoza, Raúl', vacantes: 18, capacidad: 40,
        horario: [{ dia: 'Mar', inicio: '08:00', fin: '10:00' }, { dia: 'Jue', inicio: '08:00', fin: '10:00' }] },
      { codigo: 'BMA02', curso: 'Cálculo Integral', ciclo: 2, creditos: 5, seccion: 'A',
        docente: 'Ramos, Julia', vacantes: 0, capacidad: 40,
        horario: [{ dia: 'Lun', inicio: '14:00', fin: '16:00' }, { dia: 'Mié', inicio: '14:00', fin: '16:00' }] },
      { codigo: 'EE306', curso: 'Circuitos Eléctricos I', ciclo: 4, creditos: 4, seccion: 'A',
        docente: 'Castro, Pedro', vacantes: 7, capacidad: 30,
        horario: [{ dia: 'Mar', inicio: '10:00', fin: '12:00' }, { dia: 'Vie', inicio: '10:00', fin: '12:00' }] },
      { codigo: 'EE306', curso: 'Circuitos Eléctricos I', ciclo: 4, creditos: 4, seccion: 'B',
        docente: 'Vega, Rosa', vacantes: 25, capacidad: 30,
        horario: [{ dia: 'Mié', inicio: '16:00', fin: '18:00' }, { dia: 'Vie', inicio: '16:00', fin: '18:00' }] },
      { codigo: 'CIB02', curso: 'Ingeniería de Software', ciclo: 8, creditos: 3, seccion: 'A',
        docente: 'Salazar, Miguel', vacantes: 4, capacidad: 35,
        horario: [{ dia: 'Jue', inicio: '18:00', fin: '20:00' }, { dia: 'Sáb', inicio: '08:00', fin: '10:00' }] },
      { codigo: 'EE458', curso: 'Sistemas de Control Digital', ciclo: 8, creditos: 3, seccion: 'A',
        docente: 'Rojas, Elena', vacantes: 10, capacidad: 30,
        horario: [{ dia: 'Lun', inicio: '18:00', fin: '20:00' }, { dia: 'Mié', inicio: '18:00', fin: '20:00' }] }
    ]
  };
  // Una pequeña espera imita la demora de la red.
  return new Promise((resolver) => setTimeout(() => resolver(respuesta), 300));
}
