package com.company.auth_service.service;

import com.company.auth_service.document.VerificationToken;
import com.company.auth_service.document.User;
import com.company.auth_service.dto.UserDTO;
import com.company.auth_service.dto.UserLoginDTO;
import com.company.auth_service.dto.UserLoginResponse;
import com.company.auth_service.repository.RoleRepository;
import com.company.auth_service.repository.VerificationTokenRepository;
import com.company.auth_service.repository.UserRepository;
import com.company.auth_service.security.JWTService;
import com.company.auth_service.service.email.EmailService;
import com.company.auth_service.service.email.EmailTemplateName;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final EmailService emailService;
// Below interface coming from spring security, we will create bean in AuthProviderConfig class and inject.
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    public void registerUser(UserDTO userDTO) throws MessagingException {
        var userRole = roleRepository.findByName("USER").orElseThrow(() -> new IllegalArgumentException("role not exists"));
       var user = User.builder()
                 .firstname(userDTO.getFirstname())
                       .lastname(userDTO.getLastname())
                            .email(userDTO.getEmail())
                               .password(passwordEncoder.encode(userDTO.getPassword()))
                                  .accountLocked(false)
                                       .enabled(false)
                                          .roles(List.of(userRole))
                                             .build();
        userRepository.save(user);
        sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        String newPasscode = generateCodeAndSaveCodeInVerificationTokenRepository(user);
        // TODO: send email
        emailService.sendEmail(
                user.getEmail(),
                user.getFullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newPasscode,
                "account activation"
        );

    }

    private String generateCodeAndSaveCodeInVerificationTokenRepository(User user) {
        // generate passcode
        String generatedPasscode = generateActivationPasscode(6);
        VerificationToken verificationToken = VerificationToken.builder()
                .token(generatedPasscode)
                .user(user)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(20))
                .build();
        verificationTokenRepository.save(verificationToken);
        return generatedPasscode;
    }

    private String generateActivationPasscode(int length) {
       String characters = "0123456789";
        SecureRandom secureRandom = new SecureRandom();
       StringBuilder passCodeBuilder = new StringBuilder();
        for (int j = 0; j < length; j++) {
            passCodeBuilder.append(characters.charAt(secureRandom.nextInt(characters.length())));
        }
        return passCodeBuilder.toString();
    }


    public void activateAccount(String passedEmailToken) throws MessagingException {
      VerificationToken verifiedToken = verificationTokenRepository.findByToken(passedEmailToken).orElseThrow(() -> new RuntimeException("No verification token found!"));
     if(LocalDateTime.now().isAfter(verifiedToken.getExpiredAt())){
         sendValidationEmail(verifiedToken.getUser());
         throw new RuntimeException("activation token has expired. A new token is sent to your email address.");
     }
     User user = userRepository.findById(verifiedToken.getUser().getId()).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
     user.setEnabled(true);
     userRepository.save(user);
     verifiedToken.setValidatedAt(LocalDateTime.now());
     verificationTokenRepository.save(verifiedToken);
    }

    public UserLoginResponse login(UserLoginDTO userLoginDTO) {
       Authentication auth = authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail(),userLoginDTO.getPassword())
       );
      Map<String, Object> claims = new HashMap<>();
      User user = (User) auth.getPrincipal();
      String jwtToken = jwtService.generateToken(claims,user);

       return UserLoginResponse
               .builder().JwtToken(jwtToken).build();
    }
}
