package com.velheor.internship.controllers;

import com.velheor.internship.BaseWebTest;
import com.velheor.internship.dto.UserViewDto;
import com.velheor.internship.exception.ErrorMessage;
import com.velheor.internship.mappers.UserMapper;
import com.velheor.internship.models.User;
import com.velheor.internship.models.enums.EUserStatus;
import com.velheor.internship.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;

import static com.velheor.internship.utils.TestUtils.TEST_UUID;
import static com.velheor.internship.utils.TestUtils.USER1;
import static com.velheor.internship.utils.TestUtils.USER2;
import static com.velheor.internship.utils.TestWebUtils.USER_PROFILE_UPDATE_DTO;
import static com.velheor.internship.utils.TestWebUtils.USER_URL;
import static com.velheor.internship.utils.TestWebUtils.USER_VIEW_DTO1;
import static com.velheor.internship.utils.TestWebUtils.USER_VIEW_DTO2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends BaseWebTest {

    private UserService userService;
    private UserController userController;

    @Autowired
    public UserControllerTest(UserMapper userMapper) {
        setUp(() -> {
            userService = mock(UserService.class);
            userController = new UserController(userService, userMapper);
            return userController;
        });
    }

    @Test
    void findById() throws Exception {
        when(userService.findById(USER1.getId())).thenReturn(USER1);
        mockMvc.perform(get(USER_URL + USER1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(USER_VIEW_DTO1)));
    }

    @Test
    void findByIdBadRequest() throws Exception {
        mockMvc.perform(get(USER_URL + "notValidText")).andExpect(status().isBadRequest());
    }

    @Test
    void findByIdNotExistsUser() throws Exception {
        when(userService.findById(TEST_UUID)).thenThrow(new EntityNotFoundException());
        mockMvc.perform(get(USER_URL + TEST_UUID))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getAll() throws Exception {
        when(userService.getAll()).thenReturn(Arrays.asList(USER1, USER2));
        mockMvc.perform(get(USER_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(USER_VIEW_DTO1, USER_VIEW_DTO2))));
    }

    @Test
    @WithMockUser(authorities = "CARRIER")
    void getAllWithBadAuthorities() throws Exception {
        mockMvc.perform(get(USER_URL)).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void update() throws Exception {
        when(userService.save(USER1)).thenReturn(USER1);

        mockMvc.perform(put(USER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(USER_VIEW_DTO1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(USER_VIEW_DTO1)));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateThrowHandleMethodArgumentNotValid() throws Exception {
        UserViewDto testUser = new UserViewDto(USER_VIEW_DTO1);
        testUser.setPassword("test");
        testUser.setPhoneNumber("+1234");

        String responseBody = mockMvc.perform(put(USER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andReturn().getResponse().getContentAsString();
        ErrorMessage actual = objectMapper.readValue(responseBody, ErrorMessage.class);
        int countOfErrors = 2;
        assertThat(actual.getErrors().size()).isEqualTo(countOfErrors);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void save() throws Exception {
        when(userService.save(USER1)).thenReturn(USER1);
        mockMvc.perform(post(USER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(USER_VIEW_DTO1)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(USER_VIEW_DTO1)));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteById() throws Exception {
        mockMvc.perform(delete(USER_URL + USER1.getId())).andExpect(status().isNoContent());

        verify(userService).deleteById(USER1.getId());
    }

    @Test
    @WithMockUser(username = "test1@gmail.com")
    void updateProfile() throws Exception {
        User expected = new User(USER1);
        //because USER_PROFILE_UPDATE_DTO does not have id
        expected.setId(null);
        expected.setFirstName(USER_PROFILE_UPDATE_DTO.getFirstName());
        expected.setEmail(USER_PROFILE_UPDATE_DTO.getEmail());
        expected.setStatus(EUserStatus.INACTIVE);

        when(userService.updateCurrentUser("test1@gmail.com", expected)).thenReturn(expected);

        mockMvc.perform(put(USER_URL + "profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(USER_PROFILE_UPDATE_DTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(USER_PROFILE_UPDATE_DTO)));
    }
}