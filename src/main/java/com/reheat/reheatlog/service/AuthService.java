package com.reheat.reheatlog.service;

import com.reheat.reheatlog.domain.Session;
import com.reheat.reheatlog.domain.User;
import com.reheat.reheatlog.exception.InvalidSigninInformation;
import com.reheat.reheatlog.repository.UserRepository;
import com.reheat.reheatlog.request.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    @Transactional
    public String signIn(Login login) {
        User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow(InvalidSigninInformation::new);
        Session session = user.addSession();

        return session.getAccessToken();
    }
}
