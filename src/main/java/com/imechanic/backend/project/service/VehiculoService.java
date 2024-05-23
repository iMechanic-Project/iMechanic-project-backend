package com.imechanic.backend.project.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.imechanic.backend.project.controller.dto.VehiculoDTORequest;
import com.imechanic.backend.project.controller.dto.VehiculoDTOResponse;
import com.imechanic.backend.project.controller.dto.VehiculoSearchDTORequest;
import com.imechanic.backend.project.controller.dto.VehiculoSearchDTOResponse;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehiculoService {
    private final VehiculoRepository vehiculoRepository;
    private final MarcaRepository marcaRepository;
    private final ModeloRepository modeloRepository;
    private final CuentaRepository cuentaRepository;
    private final JwtAuthenticationManager jwtAuthenticationManager;

    @Transactional(readOnly = true)
    public VehiculoSearchDTOResponse buscarPorPlaca(DecodedJWT decodedJWT, VehiculoSearchDTORequest vehiculoSearchDTORequest) {
        String roleName = jwtAuthenticationManager.getUserRole(decodedJWT);

        if (!roleName.equals("TALLER")) {
            throw new RoleNotAuthorized("El rol del usuario no es 'TALLER'");
        }

        String correoElectronico = decodedJWT.getSubject();

        Cuenta cuentaTaller = cuentaRepository.findByCorreoElectronico(correoElectronico)
                .orElseThrow(() -> new EntidadNoEncontrada("No se encontró la cuenta del cliente con correo electronico " + correoElectronico));

        Vehiculo vehiculo = vehiculoRepository.findByPlaca(vehiculoSearchDTORequest.getPlaca())
                .orElseThrow(() -> new EntidadNoEncontrada("No se encontró el vehiculo con placa: " + vehiculoSearchDTORequest.getPlaca()));

        return new VehiculoSearchDTOResponse(vehiculo.getCuenta().getNombre(), vehiculo.getCuenta().getDireccion(), vehiculo.getCuenta().getTelefono(), vehiculo.getPlaca(), vehiculo.getMarca().getNombre(), vehiculo.getModelo().getNombre(), vehiculo.getCategoria().toString());
    }

    @Transactional(readOnly = true)
    public List<VehiculoDTOResponse> obtenerMisVehiculos(DecodedJWT decodedJWT) {
        String roleName = jwtAuthenticationManager.getUserRole(decodedJWT);

        if (!roleName.equals("CLIENTE")) {
            throw new RoleNotAuthorized("El rol del usuario no es 'CLIENTE'");
        }

        String correoElectronico = decodedJWT.getSubject();

        Cuenta cuenta = cuentaRepository.findByCorreoElectronico(correoElectronico)
                .orElseThrow(() -> new EntidadNoEncontrada("No se encontró la cuenta del taller con correo electrónico: " + correoElectronico));

        List<Vehiculo> vehiculos = cuenta.getVehiculos();

        return vehiculos.stream()
                .map(vehiculo -> new VehiculoDTOResponse(vehiculo.getPlaca(), vehiculo.getMarca().getNombre(), vehiculo.getModelo().getNombre(), vehiculo.getCategoria().name()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Marca> obtenerTodasLasMarcas() {
        return marcaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Modelo> obtenerTodosLosModelos(Long marcaId) {
        return modeloRepository.findByMarcaId(marcaId);
    }

    @Transactional
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
