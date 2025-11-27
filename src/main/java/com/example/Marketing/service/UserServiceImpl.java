// En: src/main/java/com/example/Marketing/service/UserServiceImpl.java

package com.example.Marketing.service;

import com.example.Marketing.dto.UserLoginRequest;
import com.example.Marketing.dto.UserLoginResponse;
import com.example.Marketing.model.User;
import com.example.Marketing.repository.UserRepository;
import lombok.RequiredArgsConstructor;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    // private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        
        // System.out.println("=========================================");
        // System.out.println("INTENTO DE LOGIN PARA: " + request.getEmail());
        // System.out.println("PASSWORD RECIBIDO: " + request.getPassword());
        
        // try {
        //     authenticationManager.authenticate(
        //             new UsernamePasswordAuthenticationToken(
        //                     request.getEmail(),
        //                     request.getPassword()
        //             )
        //     );
        
        // } catch (Exception e) {
        //     System.out.println("¡ERROR DE AUTENTICACIÓN!: " + e.getMessage());
        //     System.out.println("=========================================");
        //     throw e; 
        // }
        
        // System.out.println("¡AUTENTICACIÓN EXITOSA!");
        // System.out.println("=========================================");

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        
        String token = jwtService.generateToken(user);

        return UserLoginResponse.builder()
                .token(token)
                .fullName(user.getFullName())
                .build();
    }
}