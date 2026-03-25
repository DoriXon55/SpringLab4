package org.dorixon.springlab4.rest;


import lombok.extern.slf4j.Slf4j;
import org.dorixon.springlab4.auth.AuthController;
import org.dorixon.springlab4.auth.AuthService;
import org.dorixon.springlab4.auth.Credentials;
import org.dorixon.springlab4.model.Role;
import org.dorixon.springlab4.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Optional;
import java.util.Set;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
@WebMvcTest(AuthController.class)
@WithMockUser(username = "admin", roles = {"ADMIN"})
@AutoConfigureJsonTesters
@Slf4j
class AuthControllerIntegrationTest {
    private String apiPath = "/api";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService mockAuthService;

    private AuthController authController;

    @Autowired
    private JacksonTester<Student> jacksonTester;

    @Autowired
    private JacksonTester<Credentials> credentialsJacksonTester;




    @Test
    void registerUser() throws Exception {


        String jsonStudent = jacksonTester.write(student).getJson();

        mockMvc.perform(post(apiPath + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonStudent))
                .andExpect(status().isOk());


    }

    @Test
    void loginUser() throws Exception {
        Role adminRole = Role.builder()
                .role_id(1)
                .name("ADMIN")
                .build();
        Student student = new Student();
        student.setStudentId(1);
        student.setImie("Andrzej");
        student.setNazwisko("Kowalski");
        student.setNrIndeksu("122278");
        student.setEmail("andrzej@test.pl");
        student.setPassword("witam123");
        student.setStacjonarny(true);
        student.setRoles(Set.of(adminRole));

        Credentials credentials = new Credentials("elo@gmail.com", "witam123");

        String jsonCredentials = credentialsJacksonTester.write(credentials).getJson();

        mockMvc.perform(post(apiPath + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonCredentials))
                .andExpect(status().isOk());

    }


}
