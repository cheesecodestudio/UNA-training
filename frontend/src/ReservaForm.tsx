import { useEffect, useState } from "react";

type EquipoMin = {
  id: number;
  nombre: string;
};

type UsuarioMin = {
  id: number;
  nombre: string;
};

type ReservaForm = {
  usuarioId: number;
  equipoId: number;
  inicioDate: string;
  inicioTime: string;
  finDate: string;
  finTime: string;
};

const ReservaForm = ({ onCreado }: { onCreado: () => void }) => {
  const [guardando, setGuardando] = useState(false);
  const [equipos, setEquipos] = useState<Array<EquipoMin>>();
  const [usuarios, setUsuarios] = useState<Array<UsuarioMin>>();
  const [error, setError] = useState<string | null>(null);
  const [reserva, setReserva] = useState<ReservaForm>({
    usuarioId: 0,
    equipoId: 0,
    inicioDate: "",
    inicioTime: "",
    finDate: "",
    finTime: "",
  });

  useEffect(() => {
    const controller = new AbortController();

    const cargarEquipos = async () => {
      try {
        const responseEquipos = await fetch(`/api/equipos/select`, {
          signal: controller.signal,
        });

        if (!responseEquipos.ok) {
          throw new Error(
            `${responseEquipos.status}: ${responseEquipos.json()}`,
          );
        }

        const equipos = await responseEquipos.json();
        setEquipos(equipos);
      } catch (err) {
        if (controller.signal.aborted) return;
        setError(
          err instanceof Error
            ? err.message
            : "Ocurrió un error al cargar los equipos.",
        );
      }
    };

    const cargarUsuarios = async () => {
      try {
        const responseUsuarios = await fetch(`/api/usuarios/select`, {
          signal: controller.signal,
        });

        if (!responseUsuarios.ok) {
          throw new Error(
            `${responseUsuarios.status}: ${responseUsuarios.json()}`,
          );
        }

        const usuarios = await responseUsuarios.json();
        setUsuarios(usuarios);
      } catch (err) {
        if (controller.signal.aborted) return;
        setError(
          err instanceof Error
            ? err.message
            : "Ocurrió un error al cargar los usuarios.",
        );
      }
    };

    cargarEquipos();
    cargarUsuarios();
    return () => controller.abort();
  }, []);

  const createHandler = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const controller = new AbortController();
    if (guardando) return;

    setGuardando(true);
    setError(null);

    try {
      const response = await fetch("/api/reservas", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          usuarioId: reserva.usuarioId,
          equipoId: reserva.equipoId,
          inicio: `${reserva.inicioDate}T${reserva.inicioTime}`,
          fin: `${reserva.finDate}T${reserva.finTime}`,
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

      setReserva({
        usuarioId: 0,
        equipoId: 0,
        inicioDate: "",
        inicioTime: "",
        finDate: "",
        finTime: "",
      });
      onCreado();
    } catch (err) {
      if (controller.signal.aborted) return;
      setError(
        err instanceof Error
          ? err.message
          : "Ocurrió un error al crear la reserva.",
      );
    } finally {
      setGuardando(false);
    }
  };

  return (
    <>
      {error && <p role="alert">{error}</p>}
      <form onSubmit={(event) => createHandler(event)}>
        <div>
          <label htmlFor="usuario">Usuario</label>
          <select
            name="usuario"
            id="usuario"
            onChange={(event) =>
              setReserva((prev) => ({
                ...prev,
                usuarioId: Number(event.target.value),
              }))
            }
          >
            <option value="">
              Seleccione usuario
            </option>
            {usuarios &&
              usuarios.map((usuario) => (
                <option key={usuario.id} value={usuario.id}>
                  {usuario.nombre}
                </option>
              ))}
          </select>
        </div>
        <div>
          <label htmlFor="equipo">Equipo</label>
          <select
            name="equipo"
            id="equipo"
            onChange={(event) =>
              setReserva((prev) => ({
                ...prev,
                equipoId: Number(event.target.value),
              }))
            }
          >
            <option value="">
              Seleccione equipo
            </option>
            {equipos &&
              equipos.map((equipo) => (
                <option key={equipo.id} value={equipo.id}>
                  {equipo.nombre}
                </option>
              ))}
          </select>
        </div>
        <div>
          <label htmlFor="inicio">Fecha de inicio</label>
          <input
            type="date"
            name="inicio"
            id="inicioDate"
            onChange={(event) =>
              setReserva((prev) => ({
                ...prev,
                inicioDate: event.target.value,
              }))
            }
          />
          <input
            type="time"
            name="inicio"
            id="inicioTime"
            onChange={(event) =>
              setReserva((prev) => ({
                ...prev,
                inicioTime: event.target.value,
              }))
            }
          />
        </div>

        <div>
          <label htmlFor="fin">Fecha de finalizacion</label>
          <input
            type="date"
            name="fin"
            id="finDate"
            onChange={(event) =>
              setReserva((prev) => ({
                ...prev,
                finDate: event.target.value,
              }))
            }
          />
          <input
            type="time"
            name="fin"
            id="finTime"
            onChange={(event) =>
              setReserva((prev) => ({
                ...prev,
                finTime: event.target.value,
              }))
            }
          />
        </div>
        <button type="submit">Reservar</button>
      </form>
    </>
  );
};

export default ReservaForm;
