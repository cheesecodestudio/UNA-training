import { useEffect, useState } from "react";
import EquipoForm from "./EquipoForm";

export type Equipo = {
  id: number;
  codigo: string;
  nombre: string;
  activo: boolean;
};

type Equipos = {
  equipos: Equipo[] | null;
  page: number;
  size: number;
  totalPages: number;
  totalElements: number;
};

function App() {
  const [equipos, setEquipos] = useState<Equipos>({
    equipos: null,
    page: 0,
    size: 3,
    totalElements: 0,
    totalPages: 0,
  });
  const [error, setError] = useState<string | null>(null);
  const [recarga, setRecarga] = useState(0);
  const [equipoEditar, setEquipoEditar] = useState<Equipo>({
    id: 0,
    codigo: "",
    nombre: "",
    activo: true,
  });

  useEffect(() => {
    const controller = new AbortController();

    const cargarEquipos = async () => {
      try {
        const response = await fetch(
          `/api/equipos?page=${equipos.page}&size=${equipos.size}`,
          {
            signal: controller.signal,
          },
        );

        if (!response.ok) {
          throw new Error(`${response.status}: ${response.json()}`);
        }

        const pagingEquipos = await response.json();
        setEquipos(pagingEquipos);
      } catch (err) {
        if (controller.signal.aborted) return;
        setError(
          err instanceof Error
            ? err.message
            : "Ocurrió un error al cargar los equipos.",
        );
      }
    };

    cargarEquipos();
    return () => controller.abort();
  }, [equipos.page, equipos.size, recarga]);

  const editatHandler = (equipo: Equipo) => {
    setEquipoEditar(equipo);
  };

  const eliminarHandler = async (id: number) => {
    const controller = new AbortController();
    const isDelete = confirm("Esta seguro de eliminar este equipo?");

    if (!isDelete) return;

    try {
      const response = await fetch(`/api/equipos/${id}`, {
        method: "DELETE",
        signal: controller.signal,
      });

      if (!response.ok) {
        const problema = await response.json().catch(() => null);
        throw new Error(
          problema?.detail ||
            problema?.mensaje ||
            `No se pudo crear el equipo (${response.status}).`,
        );
      }
    } catch (err) {
      if (controller.signal.aborted) return;
      setError(
        err instanceof Error
          ? err.message
          : "Ocurrió un error al crear el equipo.",
      );
    } finally {
      setRecarga((valor) => valor + 1);
    }
  };

  return (
    <main>
      <h1>Equipos</h1>
      {error && <p role="alert">{error}</p>}
      {!error && equipos.equipos === null && <p>Cargando equipos...</p>}
      {equipos.equipos?.length === 0 && <p>No hay equipos registrados.</p>}

      <h2>Formulario</h2>
      <EquipoForm
        key={equipoEditar.id}
        onCreado={() => setRecarga((valor) => valor + 1)}
        equipoEditar={equipoEditar}
        setEquipoEditar={setEquipoEditar}
      />

      <h2>Lista</h2>
      <ul>
        {equipos.equipos?.map((equipo) => (
          <li key={equipo.id}>
            <p>
              {equipo.codigo} - {equipo.nombre} (
              {equipo.activo ? "Activo" : "Inactivo"})
            </p>
            <button type="button" onClick={() => editatHandler(equipo)}>
              Editar
            </button>
            <button type="button" onClick={() => eliminarHandler(equipo.id)}>
              Eliminar
            </button>
          </li>
        ))}
      </ul>
      <ul className="paging">
        {Array.from({ length: equipos.totalPages }, (_, index) => (
          <li key={index}>
            <button
              className={equipos.page === index ? "active" : ""}
              onClick={() => setEquipos((prev) => ({ ...prev, page: index }))}
            >
              {index + 1}
            </button>
          </li>
        ))}
      </ul>
    </main>
  );
}

export default App;
