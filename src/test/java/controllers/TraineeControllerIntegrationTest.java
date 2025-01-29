package controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import configs.TestWebConfig;
import org.example.models.user.LoginUserDto;
import org.example.models.trainee.TraineeDto;
import org.example.utils.UserUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@SpringJUnitConfig(classes = {TestWebConfig.class})
public class TraineeControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    public TraineeDto buildTraineeDto() {
        return TraineeDto.builder()
                .firstName("John")
                .lastName("Doe")
                .password("JohnDoe")
                .dateOfBirth(LocalDate.of(1998, 11, 23))
                .address("Test Street")
                .isActive(true)
                .build();
    }

    @Test
    public void testCreateTraining() throws Exception {
        TraineeDto traineeDto = buildTraineeDto();

        String traineeJson = mapper.writeValueAsString(traineeDto);

        MvcResult mvcResult=mockMvc.perform(post("/trainees/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(traineeJson))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost"))
                .andReturn();

        String responseJson=mvcResult.getResponse().getContentAsString();
        LoginUserDto responseDto=mapper.readValue(responseJson, LoginUserDto.class);

        assertEquals(responseDto.getUsername(), traineeDto.getFirstName()+"."+traineeDto.getLastName());
        assertTrue(UserUtils.passwordMatch(traineeDto.getPassword(), responseDto.getPassword()));
    }
    @Test
    public void testUpdateTraining() throws Exception {
        TraineeDto traineeDto = buildTraineeDto();
        String traineeJson = mapper.writeValueAsString(traineeDto);
        mockMvc.perform(post("/trainees/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(traineeJson));
    }
}
