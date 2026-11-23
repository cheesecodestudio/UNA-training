package cr.ac.practica.campusreserva.equipo;

import java.util.List;

import org.springframework.stereotype.Service;

import cr.ac.practica.campusreserva.equipo.dto.CrearEquipoRequest;
import cr.ac.practica.campusreserva.equipo.dto.EquipoPaginaResponse;
import cr.ac.practica.campusreserva.equipo.dto.EquipoResponse;
import cr.ac.practica.campusreserva.exception.OwnException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;

@Service
public class EquipoService {
    private final EquipoRepository equipoRepository;

    public EquipoService(EquipoRepository equipoRepository) {
        this.equipoRepository = equipoRepository;
    }

    public List<EquipoResponse> listar() {
        return equipoRepository.findAll().stream()
                .map(equipo -> new EquipoResponse(
                        equipo.getId(),
                        equipo.getNombre(),
                        equipo.getCodigo(),
                        equipo.getActivo() == 1,
                        ""))
                .toList();
    }

    public EquipoResponse crear(CrearEquipoRequest request) {
        Equipo equipo = new Equipo(
                request.nombre(),
                request.codigo(),
                request.activo() ? 1 : 0);
        equipoRepository.save(equipo);
        return new EquipoResponse(
                equipo.getId(),
                equipo.getNombre(),
                equipo.getCodigo(),
                equipo.getActivo() == 1,
                "");
    }

    public EquipoResponse obtener(Long id) {
        Equipo equipo = equipoRepository.findById(id)
                .orElseThrow(() -> new OwnException.EntityNotFoundException("equipo", id));

        return new EquipoResponse(
                equipo.getId(),
                equipo.getNombre(),
                equipo.getCodigo(),
                equipo.getActivo() == 1,
                "");
    }

    public EquipoResponse actualizar(Long id, CrearEquipoRequest request) {
        Equipo equipo = equipoRepository.findById(id)
                .orElseThrow(() -> new OwnException.EntityNotFoundException("equipo", id));

        if (request.nombre() != null) {
            equipo.setNombre(request.nombre());
        }
        if (request.codigo() != null) {
            equipo.setCodigo(request.codigo());
        }
        equipo.setActivo(request.activo() ? 1 : 0);

        equipoRepository.save(equipo);

        return new EquipoResponse(
                equipo.getId(),
                equipo.getNombre(),
                equipo.getCodigo(),
                equipo.getActivo() == 1,
                "");
    }

    public void eliminar(Long id) {
        Equipo equipo = equipoRepository.findById(id)
                .orElseThrow(() -> new OwnException.EntityNotFoundException("equipo", id));
        equipoRepository.delete(equipo);
    }

    public EquipoPaginaResponse listarPaginado(int page, int size, String sort) {
        var request = PageRequest.of(page, size, Sort.by(sort).ascending());
        Page<Equipo> result = equipoRepository.findAll(request);
        List<EquipoResponse> equipos = result.getContent().stream()
                .map(equipo -> new EquipoResponse(
                        equipo.getId(),
                        equipo.getNombre(),
                        equipo.getCodigo(),
                        equipo.getActivo() == 1,
                        ""))
                .toList();
        return new EquipoPaginaResponse(
                equipos,
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages());
    }
}
