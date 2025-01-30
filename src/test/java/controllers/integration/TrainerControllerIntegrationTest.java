package controllers.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import configs.TestWebConfig;
import org.example.enums.TrainingType;
import org.example.models.trainer.TrainerDto;
import org.example.models.trainer.TrainerRegistrationDto;
import org.example.models.trainer.TrainerUpdateDto;
import org.example.models.trainer.TrainerViewDto;
import org.example.models.user.ChangeUserPasswordDto;
import org.example.models.user.LoginUserDto;
import org.junit.jupiter.api.BeforeEach;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@SpringJUnitConfig(classes = {TestWebConfig.class})
public class TrainerControllerIntegrationTest {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ConversionService conversionService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    public TrainerDto buildTrainerDto() {
        return TrainerDto.builder()
                .firstName("John")
                .lastName("Doe")
                .password("JohnDoe")
                .specialization(TrainingType.STRENGTH)
                .isActive(true)
                .build();
    }

    @Test
    public void testCreateTrainer() throws Exception {
        TrainerDto trainerDto = buildTrainerDto();

        TrainerRegistrationDto registrationDto = conversionService.convert(trainerDto, TrainerRegistrationDto.class);

        String trainerJson = mapper.writeValueAsString(registrationDto);

        MvcResult mvcResult = mockMvc.perform(post("/trainers/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(trainerJson))
                .andExpect(status().isCreated())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        LoginUserDto loginUserDto = mapper.readValue(response, LoginUserDto.class);
        assertNotNull(loginUserDto);
        assertEquals(loginUserDto.getUsername(), trainerDto.getFirstName() + "." + trainerDto.getLastName());
    }

    @Test
    public void testFindTrainerByUsername() throws Exception {
        TrainerDto trainerDto = buildTrainerDto();

        TrainerRegistrationDto registrationDto = conversionService.convert(trainerDto, TrainerRegistrationDto.class);
        String trainerJson = mapper.writeValueAsString(registrationDto);

        MvcResult mvcResult = mockMvc.perform(post("/trainers/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(trainerJson))
                .andReturn();

        String responseJson = mvcResult.getResponse().getContentAsString();
        LoginUserDto responseLoginDto = mapper.readValue(responseJson, LoginUserDto.class);

        mvcResult = mockMvc.perform(get("/trainers/trainer/" + responseLoginDto.getUsername()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();


        TrainerViewDto responseDto = mapper.readValue(mvcResult.getResponse().getContentAsString(), TrainerViewDto.class);

        assertEquals(trainerDto.getFirstName(), responseDto.getFirstName());
        assertEquals(trainerDto.getLastName(), responseDto.getLastName());
    }

    @Test
    public void testUpdateTrainee() throws Exception {
        TrainerDto trainerDto = buildTrainerDto();
        TrainerRegistrationDto registrationDto = conversionService.convert(trainerDto, TrainerRegistrationDto.class);

        String trainerJson = mapper.writeValueAsString(registrationDto);

        MvcResult mvcResult = mockMvc.perform(post("/trainers/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(trainerJson))
                .andReturn();

        String responseJson = mvcResult.getResponse().getContentAsString();

        LoginUserDto responseLoginDto = mapper.readValue(responseJson, LoginUserDto.class);

        TrainerUpdateDto trainerUpdateDto = TrainerUpdateDto.builder()
                .firstName("NewName")
                .lastName(trainerDto.getLastName())
                .username(responseLoginDto.getUsername())
                .specialization(trainerDto.getSpecialization())
                .isActive(true)
                .build();

        String updateJson = mapper.writeValueAsString(trainerUpdateDto);

        mvcResult = mockMvc.perform(put("/trainers/trainer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andReturn();

        TrainerViewDto updatedTraineeResponse = mapper.readValue(mvcResult.getResponse().getContentAsString(), TrainerViewDto.class);
        assertEquals("NewName", updatedTraineeResponse.getFirstName());
        assertEquals(trainerDto.getLastName(), updatedTraineeResponse.getLastName());
        assertTrue(updatedTraineeResponse.isActive());

    }

    @Test
    public void testChangePassword() throws Exception {
        TrainerDto trainerDto = buildTrainerDto();
        TrainerRegistrationDto registrationDto = conversionService.convert(trainerDto, TrainerRegistrationDto.class);
        String trainerJson = mapper.writeValueAsString(registrationDto);

        MvcResult mvcResult = mockMvc.perform(post("/trainers/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(trainerJson))
                .andReturn();

        String responseJson = mvcResult.getResponse().getContentAsString();
        LoginUserDto responseLoginDto = mapper.readValue(responseJson, LoginUserDto.class);

        ChangeUserPasswordDto changeUserPasswordDto = ChangeUserPasswordDto.builder()
                .username(responseLoginDto.getUsername())
                .oldPassword(trainerDto.getPassword())
                .newPassword("updated")
                .build();

        String changeJson = mapper.writeValueAsString(changeUserPasswordDto);

        mockMvc.perform(put("/trainers/trainer/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(changeJson))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    public void testDe_activateTrainee() throws Exception {
        TrainerDto trainerDto = buildTrainerDto();

        TrainerRegistrationDto registrationDto = conversionService.convert(trainerDto, TrainerRegistrationDto.class);
        String jsonNewTrainer = mapper.writeValueAsString(registrationDto);

        MvcResult mvcResult = mockMvc.perform(post("/trainers/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonNewTrainer))
                .andReturn();

        String responseNewTrainerJson = mvcResult.getResponse().getContentAsString();

        LoginUserDto responseLoginDto = mapper.readValue(responseNewTrainerJson, LoginUserDto.class);

        mockMvc.perform(patch("/trainers/trainer/" + responseLoginDto.getUsername() + "/de-activate?activate=false"))
                .andExpect(status().isNoContent())
                .andReturn();
    }

}
