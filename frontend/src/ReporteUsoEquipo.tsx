import React, { useState } from "react";

type Reporte = {
  desdeDate: string;
  desdeTime: string;
  hastaDate: string;
  hastaTime: string;
};

type UsoEquipo = {
  idEquipo: number;
  nombreEquipo: string;
  totalReservas: number;
};

const ReporteUsoEquipo = () => {
  const [reporte, setReporte] = useState<Reporte>({
    desdeDate: "",
    desdeTime: "",
    hastaDate: "",
    hastaTime: "",
  });
  const [usoEquipos, setUsoEquipos] = useState<UsoEquipo[] | null>(null);
  const [cargando, setCargando] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const usoEquipoHandler = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const controller = new AbortController();
    if (cargando) return;
    setCargando(true);
    setError(null);
    try {
      const desde = `${reporte.desdeDate}T${reporte.desdeTime}`;
      const hasta = `${reporte.hastaDate}T${reporte.hastaTime}`;
      const params = new URLSearchParams({ desde, hasta });
      const response = await fetch(`/api/reportes/uso-equipos?${params}`, {
        signal: controller.signal,
      });

      if (!response.ok) {
        throw new Error(`${response.status}: ${response.json()}`);
      }

      const usoEquipos = await response.json();
      setUsoEquipos(usoEquipos);
    } catch (err) {
      if (controller.signal.aborted) return;
      setError(
        err instanceof Error
          ? err.message
          : "Ocurrió un error al cargar los equipos usados.",
      );
    } finally {
      setCargando(false);
    }
  };

  return (
    <>
      <h1>Reportes</h1>
      <h2>Reservas por Equipo</h2>
      {error && <p role="alert">{error}</p>}
      <form onSubmit={(event) => usoEquipoHandler(event)}>
        <div>
          <label htmlFor="desde">Desde</label>
          <input
            type="date"
            name="desdeDate"
            id="desdeDate"
            onChange={(event) =>
              setReporte((prev) => ({
                ...prev,
                desdeDate: event.target.value,
              }))
            }
          />
          <input
            type="time"
            name="desdeTime"
            id="desdeTime"
            onChange={(event) =>
              setReporte((prev) => ({
                ...prev,
                desdeTime: event.target.value,
              }))
            }
          />
        </div>

        <div>
          <label htmlFor="hastaDate">Hasta</label>
          <input
            type="date"
            name="hastaDate"
            id="hastaDate"
            onChange={(event) =>
              setReporte((prev) => ({
                ...prev,
                hastaDate: event.target.value,
              }))
            }
          />
          <input
            type="time"
            name="hastaTime"
            id="hastaTime"
            onChange={(event) =>
              setReporte((prev) => ({
                ...prev,
                hastaTime: event.target.value,
              }))
            }
          />
        </div>
        <button type="submit">Consultar</button>
      </form>
      <h3>Resultado</h3>
      {!error && usoEquipos === null && <p>Cargando equipos...</p>}
      {usoEquipos?.length === 0 && <p>No hay registrados.</p>}
      {usoEquipos && (
        <table>
          <thead>
            <tr>
              <th>Nombre Equipo</th>
              <th>Total Reservas</th>
            </tr>
          </thead>
          <tbody>
            {usoEquipos.map((equipo) => (
              <tr key={equipo.idEquipo}>
                <td>{equipo.nombreEquipo}</td>
                <td>{equipo.totalReservas}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </>
  );
};

export default ReporteUsoEquipo;
