package controllers.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import configs.TestWebConfig;
import org.example.enums.TrainingType;
import org.example.models.trainee.TraineeDto;
import org.example.models.trainee.TraineeRegistrationDto;
import org.example.models.trainer.*;
import org.example.models.training.TrainingAddDto;
import org.example.models.training.TrainingDto;
import org.example.models.training.TrainingListDto;
import org.example.models.training.TrainingViewDto;
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

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    public TraineeDto buildTraineeDto() {
        return TraineeDto.builder()
                .firstName("Trainee")
                .lastName("Doe")
                .password("JohnDoe")
                .dateOfBirth(LocalDate.of(1998, 11, 23))
                .address("Test Street")
                .isActive(true)
                .build();
    }

    public TrainingDto buildTrainingDto() {
        return TrainingDto.builder()
                .traineeDto(buildTraineeDto())
                .trainerDto(buildTrainerDto())
                .trainingType(TrainingType.STRENGTH)
                .trainingDate(LocalDateTime.of(2024, 11, 23, 0, 0))
                .trainingDuration(Duration.ofHours(1))
                .trainingName("Training")
                .build();
    }

    @Test
    public void testCreateTrainer() throws Exception {
        TrainerDto trainerDto = buildTrainerDto();

        LoginUserDto loginUserDto = registerTrainer(trainerDto);
        assertNotNull(loginUserDto);
        assertEquals(loginUserDto.getUsername(), trainerDto.getFirstName() + "." + trainerDto.getLastName());
    }

    @Test
    public void testFindTrainerByUsername() throws Exception {
        TrainerDto trainerDto = buildTrainerDto();

        LoginUserDto responseLoginDto = registerTrainer(trainerDto);

        MvcResult mvcResult = mockMvc.perform(get("/trainers/trainer/" + responseLoginDto.getUsername()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        TrainerViewDto responseDto = mapper.readValue(mvcResult.getResponse().getContentAsString(), TrainerViewDto.class);

        assertEquals(trainerDto.getFirstName(), responseDto.getFirstName());
        assertEquals(trainerDto.getLastName(), responseDto.getLastName());
    }

    @Test
    public void testUpdateTrainer() throws Exception {
        TrainerDto trainerDto = buildTrainerDto();

        LoginUserDto responseLoginDto = registerTrainer(trainerDto);

        TrainerUpdateDto trainerUpdateDto = TrainerUpdateDto.builder()
                .firstName("NewName")
                .lastName(trainerDto.getLastName())
                .username(responseLoginDto.getUsername())
                .specialization(trainerDto.getSpecialization())
                .isActive(true)
                .build();

        String updateJson = mapper.writeValueAsString(trainerUpdateDto);

        MvcResult mvcResult = mockMvc.perform(put("/trainers/trainer/1")
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

        LoginUserDto responseLoginDto = registerTrainer(trainerDto);

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
    public void testActivateTrainee() throws Exception {
        TrainerDto trainerDto = buildTrainerDto();

        LoginUserDto responseLoginDto = registerTrainer(trainerDto);

        mockMvc.perform(patch("/trainers/trainer/" + responseLoginDto.getUsername() + "/activate?activate=false"))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    public void testGetNotAssignedTrainerByTraineeUsername() throws Exception {
        TrainerDto trainerDto = buildTrainerDto();
        registerTrainer(trainerDto);

        TraineeDto traineeDto = buildTraineeDto();
        LoginUserDto traineeLoginDto = registerTrainee(traineeDto);

        MvcResult mvcResult = mockMvc.perform(get("/trainers/not-assigned-on-trainee/" + traineeLoginDto.getUsername()))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        TrainerListDto responseDto = mapper.readValue(response, TrainerListDto.class);

        assertFalse(responseDto.getTrainers().isEmpty());
    }

    @Test
    public void testGetTrainerTrainings() throws Exception {
        TraineeDto traineeDto = buildTraineeDto();
        TrainerDto trainerDto = buildTrainerDto();

        LoginUserDto traineeLoginDto = registerTrainee(traineeDto);
        LoginUserDto trainerLoginDto = registerTrainer(trainerDto);

        TrainingDto trainingDto = buildTrainingDto();
        trainingDto.getTrainerDto().setUsername(trainerLoginDto.getUsername());
        trainingDto.getTraineeDto().setUsername(traineeLoginDto.getUsername());

        createTraining(trainingDto);

        MvcResult mvcResult = mockMvc.perform(get("/trainers/trainer/" + trainerLoginDto.getUsername() +
                        "/trainings?periodFrom=1999-12-12T12:12" +
                        "&periodTo=2026-12-12T12:12" +
                        "&traineeUsername=" + traineeLoginDto.getUsername()))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = mvcResult.getResponse().getContentAsString();
        TrainingListDto trainingListViewDto = mapper.readValue(responseJson, TrainingListDto.class);
        assertNotNull(trainingListViewDto);
        assertTrue(trainingListViewDto.getTrainings().size() > 0);
    }


    private LoginUserDto registerTrainee(TraineeDto traineeDto) throws Exception {
        TraineeRegistrationDto registrationDto = conversionService.convert(traineeDto, TraineeRegistrationDto.class);
        String jsonNewTrainee = mapper.writeValueAsString(registrationDto);

        MvcResult mvcResult = mockMvc.perform(post("/trainees/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonNewTrainee))
                .andReturn();

        String responseNewTraineeJson = mvcResult.getResponse().getContentAsString();

        return mapper.readValue(responseNewTraineeJson, LoginUserDto.class);
    }

    private LoginUserDto registerTrainer(TrainerDto trainerDto) throws Exception {
        TrainerRegistrationDto registrationTrainerDto = conversionService.convert(trainerDto, TrainerRegistrationDto.class);

        String trainerJson = mapper.writeValueAsString(registrationTrainerDto);
        MvcResult mvcResult = mockMvc.perform(post("/trainers/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(trainerJson))
                .andExpect(status().isCreated())
                .andReturn();

        String responseTrainerJson = mvcResult.getResponse().getContentAsString();
        return mapper.readValue(responseTrainerJson, LoginUserDto.class);
    }

    private TrainingViewDto createTraining(TrainingDto trainingDto) throws Exception {
        TrainingAddDto trainingAddDto = conversionService.convert(trainingDto, TrainingAddDto.class);

        String trainingAddDtoDtoJson = mapper.writeValueAsString(trainingAddDto);

        MvcResult mvcResult = mockMvc.perform(post("/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(trainingAddDtoDtoJson))
                .andExpect(status().isCreated())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        return mapper.readValue(response, TrainingViewDto.class);
    }

}
