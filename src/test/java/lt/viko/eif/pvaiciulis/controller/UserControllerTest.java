package lt.viko.eif.pvaiciulis.controller;

import lt.viko.eif.pvaiciulis.model.UserModel.User;
import lt.viko.eif.pvaiciulis.modelassembler.UserModelAssembler;
import lt.viko.eif.pvaiciulis.repository.UserRepository;
import lt.viko.eif.pvaiciulis.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @MockBean
    private UserModelAssembler userModelAssembler;

    @Test
    public void testGetUserByIdSuccess() throws Exception {
        User user = User.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userModelAssembler.toModel(user)).thenReturn(EntityModel.of(user));

        mockMvc.perform(get("/api/v1/auth/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    public void testGetUserByIdNotFound() throws Exception {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/auth/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetUserSuccess() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("1");

        User user = User.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userModelAssembler.toModel(user)).thenReturn(EntityModel.of(user));

        mockMvc.perform(get("/api/v1/auth/users").principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testUpdateUserSuccess() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("1");

        User user = User.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        User updatedUser = User.builder()
                .id(1)
                .firstName("Johnny")
                .lastName("Doer")
                .email("johnny.doer@example.com")
                .build();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userService.updateUser(1, user, user)).thenReturn(updatedUser);

        mockMvc.perform(put("/api/v1/auth/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "firstName": "Johnny",
                            "lastName": "Doer",
                            "email": "johnny.doer@example.com"
                        }
                        """)
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Johnny"));
    }
}
