import { useEffect, useState } from "react";
import ReservaForm from "./ReservaForm";

export type Reserva = {
  id: number;
  usuarioId: number;
  equipoId: number;
  inicioDate: string;
  inicioTime: string;
  finDate: string;
  finTime: string;
  estado: string;
};

type ReservaJson = {
  id: number;
  usuarioId: number;
  equipoId: number;
  inicio: string;
  fin: string;
  estado: string;
};

type Reservas = {
  reservas: Reserva[] | null;
  page: number;
  size: number;
  totalPages: number;
  totalElements: number;
};

function ReservaList() {
  const [reservas, setReservas] = useState<Reservas>({
    reservas: null,
    page: 0,
    size: 5,
    totalElements: 0,
    totalPages: 0,
  });
  const [error, setError] = useState<string | null>(null);
  const [recarga, setRecarga] = useState(0);
  // const [equipoEditar, setEquipoEditar] = useState<Equipo>({
  //   id: 0,
  //   codigo: "",
  //   nombre: "",
  //   activo: true,
  // });

  useEffect(() => {
    const controller = new AbortController();

    const cargarReservas = async () => {
      try {
        const response = await fetch(
          `/api/reservas?page=${reservas.page}&size=${reservas.size}&sort=id`,
          {
            signal: controller.signal,
          },
        );

        if (!response.ok) {
          throw new Error(`${response.status}: ${response.json()}`);
        }

        const pagingReservas = await response.json();
        if (pagingReservas.reservas) {
          const reservaList = pagingReservas.reservas.map(
            (reserva: ReservaJson): Reserva => {
              const inicioParts = reserva.inicio.split("T");
              const finParts = reserva.fin.split("T");
              return {
                ...reserva,
                inicioDate: inicioParts[0],
                inicioTime: inicioParts[1],
                finDate: finParts[0],
                finTime: finParts[1],
              };
            },
          );
          pagingReservas.reservas = reservaList;
        }
        setReservas(pagingReservas);
      } catch (err) {
        if (controller.signal.aborted) return;
        setError(
          err instanceof Error
            ? err.message
            : "Ocurrió un error al cargar las reservas.",
        );
      }
    };

    cargarReservas();
    return () => controller.abort();
  }, [reservas.page, reservas.size, recarga]);

  // const editatHandler = (equipo: Equipo) => {
  //   setEquipoEditar(equipo);
  // };

  const cancelarHandler = async (id: number) => {
    const controller = new AbortController();
    const isCanceled = confirm("Esta seguro de cancelar esta reserva?");

    if (!isCanceled) return;

    try {
      const response = await fetch(`/api/reservas/${id}/cancelacion`, {
        method: "PATCH",
        signal: controller.signal,
      });

      if (!response.ok) {
        const problema = await response.json().catch(() => null);
        throw new Error(
          problema?.detail ||
            problema?.mensaje ||
            `No se pudo cancelar la reserva (${response.status}).`,
        );
      }
    } catch (err) {
      if (controller.signal.aborted) return;
      setError(
        err instanceof Error
          ? err.message
          : "Ocurrió un error al cancelar la reserva.",
      );
    } finally {
      setRecarga((valor) => valor + 1);
    }
  };

  return (
    <>
      {error && <p role="alert">{error}</p>}
      {!error && reservas.reservas === null && <p>Cargando equipos...</p>}
      {reservas.reservas?.length === 0 && <p>No hay equipos registrados.</p>}
      <h1>Reservas</h1>
      <h2>Formulario</h2>
      <ReservaForm onCreado={() => setRecarga((valor) => valor + 1)} />
      <h2>Lista</h2>
      <ul>
        {reservas.reservas?.map((reserva) => (
          <li key={reserva.id}>
            <p>
              Usuario: {reserva.usuarioId} | Equipo: {reserva.equipoId}
              <br />
              Fecha inicio: {`${reserva.inicioDate} ${reserva.inicioTime}`} -
              Fecha Fin: {`${reserva.finDate} ${reserva.finTime}`}{" "}
              {`(${reserva.estado})`}
            </p>
            {/* <button type="button" onClick={() => editatHandler(equipo)}>
              Editar
            </button> */}
            {reserva.estado === "ACTIVA" && (
              <button type="button" onClick={() => cancelarHandler(reserva.id)}>
                Cancelar
              </button>
            )}
          </li>
        ))}
      </ul>
      <ul className="paging">
        {Array.from({ length: reservas.totalPages }, (_, index) => (
          <li key={index}>
            <button
              className={reservas.page === index ? "active" : ""}
              onClick={() => setReservas((prev) => ({ ...prev, page: index }))}
            >
              {index + 1}
            </button>
          </li>
        ))}
      </ul>
    </>
  );
}

export default ReservaList;
