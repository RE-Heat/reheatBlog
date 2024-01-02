package com.reheat.reheatlog.service;

import com.reheat.reheatlog.crypto.PasswordEncoder;
import com.reheat.reheatlog.domain.User;
import com.reheat.reheatlog.exception.AlreadyExistsEmailException;
import com.reheat.reheatlog.exception.InvalidSigninInformation;
import com.reheat.reheatlog.repository.UserRepository;
import com.reheat.reheatlog.request.Login;
import com.reheat.reheatlog.request.Signup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Transactional
    public Long signIn(Login login) {
        User user = userRepository.findByEmail(login.getEmail())
                .orElseThrow(InvalidSigninInformation::new);

        var matches = encoder.matches(login.getPassword(), user.getPassword());

        if (!matches) {
            throw new InvalidSigninInformation();
        }

        return user.getId();
    }

    public void signup(Signup signup) {
        Optional<User> userOptional = userRepository.findByEmail(signup.getEmail());

        if (userOptional.isPresent()) {
            throw new AlreadyExistsEmailException();
        }

        String encryptedPassword = encoder.encrypt(signup.getPassword());

        var user = User.builder()
                .name(signup.getName())
                .password(encryptedPassword)
                .email(signup.getEmail()).build();
        userRepository.save(user);
    }
}
