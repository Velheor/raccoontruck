package com.velheor.internship.config;

import com.velheor.internship.mappers.RoleMapper;
import com.velheor.internship.models.enums.EUserStatus;
import com.velheor.internship.repository.UserRepository;
import com.velheor.internship.security.JwtUser;
import com.velheor.internship.security.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Collection;

import static com.velheor.internship.utils.TestUtils.USER1;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
@ComponentScan(basePackages = {
        "com.velheor.internship.security",
        "com.velheor.internship.mappers",
        "com.velheor.internship.validator"
}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtUserDetailsService.class),
})
public class WebTestConfig {

    @Autowired
    private RoleMapper roleMapper;

    @Bean
    public JwtUserDetailsService userDetailsService() {
        JwtUserDetailsService jwtUserDetailsService = mock(JwtUserDetailsService.class);
        Collection<? extends GrantedAuthority> authorities = roleMapper.toGrantedAuthorities(USER1.getRoles());
        JwtUser jwtUser = new JwtUser(
                USER1.getId(), USER1.getFirstName(),
                USER1.getLastName(), USER1.getEmail(),
                USER1.getPhoneNumber(), USER1.getPassword(),
                authorities, USER1.getStatus().equals(EUserStatus.ACTIVE));

        when(jwtUserDetailsService.loadUserByUsername(USER1.getEmail())).thenReturn(jwtUser);
        return jwtUserDetailsService;
    }

    @Bean
    public UserRepository userRepository() {
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.checkForUserHasThisPhoneNumber(USER1.getId(), USER1.getPhoneNumber())).thenReturn(true);
        when(userRepository.checkForUniqueEmail(USER1.getEmail())).thenReturn(true);
        when(userRepository.checkForUniquePhoneNumber(USER1.getPhoneNumber())).thenReturn(true);
        when(userRepository.checkForUniquePhoneNumber("+1234")).thenReturn(true);
        return userRepository;
    }

    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }
}
