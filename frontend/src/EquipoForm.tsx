import { useState, type FormEvent } from "react";
import type { Equipo } from "./App";

type EquipoForm = {
  codigo: string;
  nombre: string;
  activo: boolean;
};

function EquipoForm({
  onCreado,
  equipoEditar,
  setEquipoEditar,
}: {
  onCreado: () => void;
  equipoEditar: Equipo;
  setEquipoEditar: React.Dispatch<React.SetStateAction<Equipo>>;
}) {
  const [equipo, setEquipo] = useState<EquipoForm>({
    codigo: equipoEditar.codigo,
    nombre: equipoEditar.nombre,
    activo: equipoEditar.activo,
  });
  const editando = equipoEditar.id > 0;
  const [guardando, setGuardando] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const createHandler = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const controller = new AbortController();
    if (guardando) return;

    setGuardando(true);
    setError(null);

    try {
      const response = await fetch("/api/equipos", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          ...equipo,
          nombre: equipo.nombre.trim(),
          codigo: equipo.codigo.trim(),
        }),
      });

      if (!response.ok) {
        const problema = await response.json().catch(() => null);
        throw new Error(
          problema?.detail ||
            problema?.mensaje ||
            `No se pudo crear el equipo (${response.status}).`,
        );
      }

      setEquipo({
        nombre: "",
        codigo: "",
        activo: true,
      });
      onCreado();
    } catch (err) {
      if (controller.signal.aborted) return;
      setError(
        err instanceof Error
          ? err.message
          : "Ocurrió un error al crear el equipo.",
      );
    } finally {
      setGuardando(false);
    }
  };

  const editHandler = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const controller = new AbortController();
    if (guardando) return;

    setGuardando(true);
    setError(null);

    try {
      const response = await fetch(`/api/equipos/${equipoEditar.id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          activo: equipo.activo,
          nombre: equipo.nombre.trim(),
          codigo: equipo.codigo.trim(),
        }),
      });

      if (!response.ok) {
        const problema = await response.json().catch(() => null);
        throw new Error(
          problema?.detail ||
            problema?.mensaje ||
            `No se pudo actualizar el equipo (${response.status}).`,
        );
      }

      setEquipo({
        nombre: "",
        codigo: "",
        activo: true,
      });
      setEquipoEditar({
        id: 0,
        nombre: "",
        codigo: "",
        activo: true,
      });
      onCreado();
    } catch (err) {
      if (controller.signal.aborted) return;
      setError(
        err instanceof Error
          ? err.message
          : "Ocurrió un error al crear el equipo.",
      );
    } finally {
      setGuardando(false);
    }
  };

  return (
    <>
      {error && <p role="alert">{error}</p>}
      <form
        onSubmit={(event) => {
          if (editando) editHandler(event);
          else createHandler(event);
        }}
      >
        <div>
          <label htmlFor="nombre">Nombre</label>
          <input
            name="nombre"
            type="text"
            value={equipo.nombre}
            onChange={(event) =>
              setEquipo((prev) => ({ ...prev, nombre: event.target.value }))
            }
          />
        </div>
        <div>
          <label htmlFor="codigo">Codigo</label>
          <input
            name="codigo"
            type="text"
            value={equipo.codigo}
            onChange={(event) =>
              setEquipo((prev) => ({ ...prev, codigo: event.target.value }))
            }
          />
        </div>
        <div>
          <label htmlFor="activo">Activo</label>
          <input
            type="checkbox"
            name="activo"
            checked={equipo.activo}
            onChange={(event) =>
              setEquipo((prev) => ({ ...prev, activo: event.target.checked }))
            }
          />
        </div>
        <button type="submit" disabled={guardando}>
          {guardando ? "Guardando…" : editando ? "Guardar cambios" : "Crear"}
        </button>
      </form>
    </>
  );
}

export default EquipoForm;
