package com.imechanic.backend.project.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.imechanic.backend.project.controller.dto.VehiculoDTORequest;
import com.imechanic.backend.project.controller.dto.VehiculoDTOResponse;
import com.imechanic.backend.project.exception.EntidadNoEncontrada;
import com.imechanic.backend.project.exception.RoleNotAuthorized;
import com.imechanic.backend.project.model.Cuenta;
import com.imechanic.backend.project.model.Marca;
import com.imechanic.backend.project.model.Modelo;
import com.imechanic.backend.project.model.Vehiculo;
import com.imechanic.backend.project.repository.CuentaRepository;
import com.imechanic.backend.project.repository.MarcaRepository;
import com.imechanic.backend.project.repository.ModeloRepository;
import com.imechanic.backend.project.repository.VehiculoRepository;
import com.imechanic.backend.project.security.util.JwtAuthenticationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehiculoService {
    private final VehiculoRepository vehiculoRepository;
    private final MarcaRepository marcaRepository;
    private final ModeloRepository modeloRepository;
    private final CuentaRepository cuentaRepository;
    private final JwtAuthenticationManager jwtAuthenticationManager;

    public List<Marca> obtenerTodasLasMarcas() {
        return marcaRepository.findAll();
    }

    public List<Modelo> obtenerTodosLosModelosDeLaMarca(Long marcaId) {
        return modeloRepository.findByMarcaId(marcaId);
    }

    public VehiculoDTOResponse crearVehiculo(VehiculoDTORequest vehiculoDTORequest, DecodedJWT decodedJWT) {
        String roleName = jwtAuthenticationManager.getUserRole(decodedJWT);

        if (!roleName.equals("CLIENTE")) {
            throw new RoleNotAuthorized("El rol del usuario no es 'CLIENTE'");
        }

        String correoElectronico = decodedJWT.getSubject();

        Cuenta cuenta = cuentaRepository.findByCorreoElectronico(correoElectronico)
                .orElseThrow(() -> new EntidadNoEncontrada("No se encuentra la cuenta con correo " + correoElectronico));

        // Obtener la marca por su ID
        Marca marca = marcaRepository.findById(vehiculoDTORequest.getMarcaId())
                .orElseThrow(() -> new EntidadNoEncontrada("Marca no encontrada"));

        // Obtener el modelo por su ID
        Modelo modelo = modeloRepository.findById(vehiculoDTORequest.getModeloId())
                .orElseThrow(() -> new EntidadNoEncontrada("Modelo no encontrado"));

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlaca(vehiculoDTORequest.getPlaca());
        vehiculo.setMarca(marca);
        vehiculo.setModelo(modelo);
        vehiculo.setCategoria(vehiculoDTORequest.getCategoria());
        vehiculo.setCuenta(cuenta);

        vehiculoRepository.save(vehiculo);

        return new VehiculoDTOResponse(vehiculoDTORequest.getPlaca(), marca.getNombre(), modelo.getNombre(), vehiculoDTORequest.getCategoria().name());
    }

}
