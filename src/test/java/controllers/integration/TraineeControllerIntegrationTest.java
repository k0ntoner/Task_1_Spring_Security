package controllers.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import configs.TestWebConfig;
import org.example.models.trainee.TraineeRegistrationDto;
import org.example.models.trainee.TraineeUpdateDto;
import org.example.models.trainee.TraineeViewDto;
import org.example.models.user.ChangeUserPasswordDto;
import org.example.models.user.LoginUserDto;
import org.example.models.trainee.TraineeDto;
import org.example.utils.UserUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
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
    @Autowired
    private ConversionService conversionService;

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

        TraineeRegistrationDto registrationDto = conversionService.convert(traineeDto, TraineeRegistrationDto.class);
        String traineeJson = mapper.writeValueAsString(registrationDto);

        MvcResult mvcResult = mockMvc.perform(post("/trainees/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(traineeJson))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost"))
                .andReturn();

        String responseJson = mvcResult.getResponse().getContentAsString();
        LoginUserDto responseDto = mapper.readValue(responseJson, LoginUserDto.class);

        assertEquals(responseDto.getUsername(), traineeDto.getFirstName() + "." + traineeDto.getLastName());
        assertTrue(UserUtils.passwordMatch(traineeDto.getPassword(), responseDto.getPassword()));
    }

    @Test
    public void testFindTraineeByUsername() throws Exception {
        TraineeDto traineeDto = buildTraineeDto();

        TraineeRegistrationDto registrationDto = conversionService.convert(traineeDto, TraineeRegistrationDto.class);
        String traineeJson = mapper.writeValueAsString(registrationDto);

        MvcResult mvcResult = mockMvc.perform(post("/trainees/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(traineeJson))
                .andReturn();

        String responseJson = mvcResult.getResponse().getContentAsString();
        LoginUserDto responseLoginDto = mapper.readValue(responseJson, LoginUserDto.class);

        mvcResult = mockMvc.perform(get("/trainees/trainee/" + responseLoginDto.getUsername()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();


        TraineeViewDto responseDto = mapper.readValue(mvcResult.getResponse().getContentAsString(), TraineeViewDto.class);
        assertEquals(traineeDto.getFirstName(), responseDto.getFirstName());
        assertEquals(traineeDto.getLastName(), responseDto.getLastName());
    }

    @Test
    public void testDeleteTrainee() throws Exception {
        TraineeDto traineeDto = buildTraineeDto();
        String username = traineeDto.getFirstName() + "." + traineeDto.getLastName();

        MvcResult mvcResult = mockMvc.perform(get("/trainees/trainee/" + username))
                .andExpect(status().isBadRequest())
                .andReturn();

        TraineeRegistrationDto registrationDto = conversionService.convert(traineeDto, TraineeRegistrationDto.class);
        String traineeJson = mapper.writeValueAsString(registrationDto);
        mockMvc.perform(post("/trainees/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(traineeJson));

        mockMvc.perform(delete("/trainees/trainee/" + username))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    public void testUpdateTrainee() throws Exception {
        TraineeDto traineeDto = buildTraineeDto();
        TraineeRegistrationDto registrationDto = conversionService.convert(traineeDto, TraineeRegistrationDto.class);
        String traineeJson = mapper.writeValueAsString(registrationDto);

        MvcResult mvcResult = mockMvc.perform(post("/trainees/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(traineeJson))
                .andReturn();

        String responseJson = mvcResult.getResponse().getContentAsString();
        LoginUserDto responseLoginDto = mapper.readValue(responseJson, LoginUserDto.class);

        TraineeUpdateDto traineeUpdateDto = TraineeUpdateDto.builder()
                .firstName(traineeDto.getFirstName())
                .lastName(traineeDto.getLastName())
                .username(responseLoginDto.getUsername())
                .address("new address")
                .dateOfBirth(traineeDto.getDateOfBirth())
                .isActive(true)
                .build();

        String updateJson = mapper.writeValueAsString(traineeUpdateDto);

        mvcResult = mockMvc.perform(put("/trainees/trainee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andReturn();

        TraineeViewDto updatedTraineeResponse = mapper.readValue(mvcResult.getResponse().getContentAsString(), TraineeViewDto.class);
        assertEquals(traineeDto.getFirstName(), updatedTraineeResponse.getFirstName());
        assertEquals(traineeDto.getLastName(), updatedTraineeResponse.getLastName());
        assertEquals("new address", updatedTraineeResponse.getAddress());
        assertTrue(updatedTraineeResponse.isActive());

    }

    @Test
    public void testChangePassword() throws Exception {
        TraineeDto traineeDto = buildTraineeDto();
        TraineeRegistrationDto registrationDto = conversionService.convert(traineeDto, TraineeRegistrationDto.class);
        String traineeJson = mapper.writeValueAsString(registrationDto);

        MvcResult mvcResult = mockMvc.perform(post("/trainees/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(traineeJson))
                .andReturn();

        String responseJson = mvcResult.getResponse().getContentAsString();
        LoginUserDto responseLoginDto = mapper.readValue(responseJson, LoginUserDto.class);

        ChangeUserPasswordDto changeUserPasswordDto = ChangeUserPasswordDto.builder()
                .username(responseLoginDto.getUsername())
                .oldPassword(traineeDto.getPassword())
                .newPassword("updated")
                .build();

        String changeJson = mapper.writeValueAsString(changeUserPasswordDto);

        mockMvc.perform(put("/trainees/trainee/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(changeJson))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    public void testDe_activateTrainee() throws Exception {
        TraineeDto traineeDto = buildTraineeDto();

        TraineeRegistrationDto registrationDto = conversionService.convert(traineeDto, TraineeRegistrationDto.class);
        String jsonNewTrainee = mapper.writeValueAsString(registrationDto);

        MvcResult mvcResult = mockMvc.perform(post("/trainees/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonNewTrainee))
                .andReturn();

        String responseNewTraineeJson = mvcResult.getResponse().getContentAsString();

        LoginUserDto responseLoginDto = mapper.readValue(responseNewTraineeJson, LoginUserDto.class);

        mockMvc.perform(patch("/trainees/trainee/" + responseLoginDto.getUsername() + "/de-activate?activate=false"))
                .andExpect(status().isNoContent())
                .andReturn();

    }

}
