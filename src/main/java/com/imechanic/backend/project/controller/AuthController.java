package com.imechanic.backend.project.controller;

import com.imechanic.backend.project.controller.dto.AuthenticationSignUpDTORequest;
import com.imechanic.backend.project.controller.dto.SignUpDTOResponse;
import com.imechanic.backend.project.security.service.IUserDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cuenta")
@RequiredArgsConstructor
public class AuthController {
    private final IUserDetailService userDetailService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpDTOResponse> signUp(@Valid @RequestBody AuthenticationSignUpDTORequest signUpDTORequest) {
        return new ResponseEntity<>(userDetailService.createUser(signUpDTORequest), HttpStatus.CREATED);
    }

    @GetMapping("/confirmation/{token}")
    public String confirmarCuenta(@PathVariable String token) {
        return userDetailService.confirmarCuenta(token);
    }

}