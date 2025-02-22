package controllers.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Application;
import org.example.enums.TrainingType;
import org.example.models.trainee.TraineeRegistrationDto;
import org.example.models.trainee.TraineeUpdateDto;
import org.example.models.trainee.TraineeViewDto;
import org.example.models.trainer.TrainerDto;
import org.example.models.trainer.TrainerRegistrationDto;
import org.example.models.training.*;
import org.example.models.user.ChangeUserPasswordDto;
import org.example.models.user.AuthUserDto;
import org.example.models.trainee.TraineeDto;
import org.example.utils.UserUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
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
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("test")
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

    public TrainerDto buildTrainerDto() {
        return TrainerDto.builder()
                .firstName("Trainer")
                .lastName("Doe")
                .password("JohnDoe")
                .specialization(TrainingType.STRENGTH)
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
        AuthUserDto responseDto = mapper.readValue(responseJson, AuthUserDto.class);

        assertEquals(responseDto.getUsername(), traineeDto.getFirstName() + "." + traineeDto.getLastName());
        assertTrue(UserUtils.passwordMatch(traineeDto.getPassword(), responseDto.getPassword()));
    }

    @Test
    public void testFindTraineeByUsername() throws Exception {
        TraineeDto traineeDto = buildTraineeDto();

        AuthUserDto responseLoginDto = registerTrainee(traineeDto);

        MvcResult mvcResult = mockMvc.perform(get("/trainees/trainee/" + responseLoginDto.getUsername()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
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

        registerTrainee(traineeDto);

        mockMvc.perform(delete("/trainees/trainee/" + username))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    public void testUpdateTrainee() throws Exception {
        TraineeDto traineeDto = buildTraineeDto();

        AuthUserDto responseLoginDto = registerTrainee(traineeDto);

        TraineeUpdateDto traineeUpdateDto = TraineeUpdateDto.builder()
                .firstName(traineeDto.getFirstName())
                .lastName(traineeDto.getLastName())
                .address("new address")
                .dateOfBirth(traineeDto.getDateOfBirth())
                .isActive(true)
                .build();

        String updateJson = mapper.writeValueAsString(traineeUpdateDto);

        MvcResult mvcResult = mockMvc.perform(put("/trainees/trainee/"+responseLoginDto.getUsername())
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

        AuthUserDto responseLoginDto = registerTrainee(traineeDto);

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
    public void testActivateTrainee() throws Exception {
        TraineeDto traineeDto = buildTraineeDto();

        AuthUserDto responseLoginDto = registerTrainee(traineeDto);

        mockMvc.perform(patch("/trainees/trainee/" + responseLoginDto.getUsername() + "/activate?activate=false"))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    public void testGetTraineeTrainings() throws Exception {
        TraineeDto traineeDto = buildTraineeDto();
        TrainerDto trainerDto = buildTrainerDto();

        AuthUserDto traineeLoginDto = registerTrainee(traineeDto);
        AuthUserDto trainerLoginDto = registerTrainer(trainerDto);

        TrainingDto trainingDto = buildTrainingDto();
        trainingDto.getTrainerDto().setUsername(trainerLoginDto.getUsername());
        trainingDto.getTraineeDto().setUsername(traineeLoginDto.getUsername());

        createTraining(trainingDto);

        MvcResult mvcResult = mockMvc.perform(get("/trainees/trainee/" + traineeLoginDto.getUsername() +
                        "/trainings?periodFrom=1999-12-12T12:12" +
                        "&periodTo=2026-12-12T12:12" +
                        "&trainerUsername=" + trainerLoginDto.getUsername() +
                        "&trainingType=STRENGTH"))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = mvcResult.getResponse().getContentAsString();
        TrainingListDto trainingListViewDto = mapper.readValue(responseJson, TrainingListDto.class);
        assertNotNull(trainingListViewDto);
        assertTrue(trainingListViewDto.getTrainings().size() > 0);
    }

    @Test
    public void testUpdateTraineeTrainings() throws Exception {
        TraineeDto traineeDto = buildTraineeDto();
        TrainerDto trainerDto = buildTrainerDto();

        AuthUserDto traineeLoginDto = registerTrainee(traineeDto);
        AuthUserDto trainerLoginDto = registerTrainer(trainerDto);

        TrainingDto trainingDto = buildTrainingDto();
        trainingDto.getTrainerDto().setUsername(trainerLoginDto.getUsername());
        trainingDto.getTraineeDto().setUsername(traineeLoginDto.getUsername());

        createTraining(trainingDto);

        MvcResult mvcResult = mockMvc.perform(get("/trainees/trainee/" + traineeLoginDto.getUsername() +
                        "/trainings?periodFrom=1999-12-12T12:12" +
                        "&periodTo=2026-12-12T12:12" +
                        "&trainerUsername=" + trainerLoginDto.getUsername() +
                        "&trainingType=STRENGTH"))
                .andReturn();

        String responseJson = mvcResult.getResponse().getContentAsString();
        TrainingListDto trainingListViewDto = mapper.readValue(responseJson, TrainingListDto.class);

        trainingListViewDto.getTrainings().clear();

        TrainingListToUpdateDto trainingListToUpdateDto = new TrainingListToUpdateDto();

        String updatedTrainingListJson = mapper.writeValueAsString(trainingListToUpdateDto);

        mvcResult = mockMvc.perform(put("/trainees/trainee/" + traineeLoginDto.getUsername() + "/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedTrainingListJson))
                .andExpect(status().isOk())
                .andReturn();

        responseJson = mvcResult.getResponse().getContentAsString();

        trainingListViewDto = mapper.readValue(responseJson, TrainingListDto.class);
        assertNotNull(trainingListViewDto);
        assertTrue(trainingListViewDto.getTrainings().size() == 0);
    }

    private AuthUserDto registerTrainee(TraineeDto traineeDto) throws Exception {
        TraineeRegistrationDto registrationDto = conversionService.convert(traineeDto, TraineeRegistrationDto.class);
        String jsonNewTrainee = mapper.writeValueAsString(registrationDto);

        MvcResult mvcResult = mockMvc.perform(post("/trainees/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonNewTrainee))
                .andReturn();

        String responseNewTraineeJson = mvcResult.getResponse().getContentAsString();

        return mapper.readValue(responseNewTraineeJson, AuthUserDto.class);
    }

    private AuthUserDto registerTrainer(TrainerDto trainerDto) throws Exception {
        TrainerRegistrationDto registrationTrainerDto = conversionService.convert(trainerDto, TrainerRegistrationDto.class);

        String trainerJson = mapper.writeValueAsString(registrationTrainerDto);
        MvcResult mvcResult = mockMvc.perform(post("/trainers/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(trainerJson))
                .andExpect(status().isCreated())
                .andReturn();

        String responseTrainerJson = mvcResult.getResponse().getContentAsString();
        return mapper.readValue(responseTrainerJson, AuthUserDto.class);
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
