'use strict';

/*
 * Comportamiento de la vista de oferta académica: carga los datos, aplica los filtros y dibuja la tabla.
 * Las filas se crean con textContent y nunca con innerHTML, para que ningún dato recibido se interprete como HTML.
 * Filtrar en el navegador es solo comodidad: las reglas de negocio se aplican siempre en el servidor.
 */
(function () {
  const PERIODO = '2026-2';
  const POCAS_VACANTES = 5;
  const COLUMNAS = ['Código', 'Curso', 'Ciclo', 'Créditos', 'Sección', 'Docente', 'Horario', 'Vacantes'];

  const formulario = document.getElementById('filtros');
  const selectorCiclo = document.getElementById('ciclo');
  const campoBuscar = document.getElementById('buscar');
  const casillaVacantes = document.getElementById('con-vacantes');
  const cuerpoTabla = document.querySelector('#tabla-oferta tbody');
  const estado = document.getElementById('estado');

  let secciones = [];

  iniciar();

  async function iniciar() {
    formulario.addEventListener('input', mostrar);
    formulario.addEventListener('submit', (evento) => evento.preventDefault());
    // El evento reset ocurre antes de que el navegador limpie los campos; se espera un instante.
    formulario.addEventListener('reset', () => setTimeout(mostrar, 0));
    try {
      const respuesta = await obtenerOferta(PERIODO);
      secciones = respuesta.secciones;
      document.getElementById('periodo').textContent = respuesta.periodo;
      llenarCiclos();
      mostrar();
    } catch (error) {
      estado.textContent = 'No se pudo cargar la oferta. Intente nuevamente en unos minutos.';
      console.error(error);
    }
  }

  function llenarCiclos() {
    const ciclos = [...new Set(secciones.map((s) => s.ciclo))].sort((a, b) => a - b);
    for (const ciclo of ciclos) {
      selectorCiclo.append(new Option(`Ciclo ${ciclo}`, String(ciclo)));
    }
  }

  /** Quita tildes y mayúsculas para que "fisica" encuentre "Física". */
  function normalizar(texto) {
    return texto.normalize('NFD').replace(/[̀-ͯ]/g, '').toLowerCase().trim();
  }

  function filtrar() {
    const ciclo = selectorCiclo.value;
    const buscado = normalizar(campoBuscar.value);
    const soloConVacantes = casillaVacantes.checked;
    return secciones
      .filter((s) => ciclo === '' || String(s.ciclo) === ciclo)
      .filter((s) => buscado === '' || normalizar(`${s.codigo} ${s.curso}`).includes(buscado))
      .filter((s) => !soloConVacantes || s.vacantes > 0)
      .sort((a, b) => a.ciclo - b.ciclo || a.codigo.localeCompare(b.codigo) || a.seccion.localeCompare(b.seccion));
  }

  function mostrar() {
    const visibles = filtrar();
    cuerpoTabla.replaceChildren(...visibles.map(crearFila));
    estado.textContent = visibles.length === 0
      ? 'Ninguna sección coincide con los filtros. Pruebe con otro ciclo o borre la búsqueda.'
      : `Se muestran ${visibles.length} de ${secciones.length} secciones.`;
  }

  function crearFila(s) {
    const fila = document.createElement('tr');
    if (s.vacantes === 0) {
      fila.classList.add('fila-agotada');
    }
    const valores = [s.codigo, s.curso, s.ciclo, s.creditos, s.seccion, s.docente];
    valores.forEach((valor, i) => fila.append(crearCelda(COLUMNAS[i], String(valor))));
    fila.append(crearCelda(COLUMNAS[6], crearHorario(s.horario)));
    fila.append(crearCelda(COLUMNAS[7], crearVacantes(s)));
    return fila;
  }

  /** La etiqueta de la columna se usa en pantallas angostas, donde cada fila se ve como tarjeta. */
  function crearCelda(etiqueta, contenido) {
    const celda = document.createElement('td');
    celda.dataset.etiqueta = etiqueta;
    if (typeof contenido === 'string') {
      celda.textContent = contenido;
    } else {
      celda.append(contenido);
    }
    return celda;
  }

  function crearHorario(bloques) {
    const lista = document.createElement('ul');
    lista.className = 'horario';
    for (const bloque of bloques) {
      const elemento = document.createElement('li');
      const inicio = document.createElement('time');
      inicio.dateTime = bloque.inicio;
      inicio.textContent = bloque.inicio;
      const fin = document.createElement('time');
      fin.dateTime = bloque.fin;
      fin.textContent = bloque.fin;
      elemento.append(`${bloque.dia} `, inicio, ' a ', fin);
      lista.append(elemento);
    }
    return lista;
  }

  function crearVacantes(s) {
    const marca = document.createElement('span');
    marca.className = 'vacantes';
    if (s.vacantes === 0) {
      marca.textContent = 'Sin vacantes';
    } else {
      marca.textContent = `${s.vacantes} de ${s.capacidad}`;
      if (s.vacantes <= POCAS_VACANTES) {
        marca.classList.add('pocas');
      }
    }
    return marca;
  }
})();
