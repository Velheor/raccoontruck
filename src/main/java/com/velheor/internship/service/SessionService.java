package com.velheor.internship.service;

import com.velheor.internship.dto.AuthUserDto;
import com.velheor.internship.models.Session;
import com.velheor.internship.repository.SessionRepository;
import com.velheor.internship.security.JwtProvider;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final JwtProvider jwtProvider;
    private final UserService userService;

    public String createRefreshToken(HttpServletRequest request, AuthUserDto authUserDTO) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        Session session = new Session();
        session.setRefreshToken(jwtProvider.createRefreshToken());
        session.setIp(request.getRemoteAddr());
        session.setOs(userAgent.getOperatingSystem().getName());
        session.setBrowser(userAgent.getBrowser().getName() + " " + userAgent.getBrowserVersion());
        session.setUser(userService.findByEmail(authUserDTO.getEmail()));
        sessionRepository.save(session);
        return session.getRefreshToken();
    }

    public Session findByRefreshToken(String refreshToken) {
       return sessionRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new EntityNotFoundException(
               "Session with refresh token: " + refreshToken + " was not found."));
    }
}