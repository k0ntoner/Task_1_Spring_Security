package controllers.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import configs.TestWebConfig;

import org.example.enums.TrainingType;
import org.example.models.trainee.TraineeDto;
import org.example.models.trainee.TraineeRegistrationDto;
import org.example.models.trainer.TrainerDto;
import org.example.models.trainer.TrainerRegistrationDto;
import org.example.models.training.TrainingAddDto;
import org.example.models.training.TrainingDto;
import org.example.models.training.TrainingTypeListDto;
import org.example.models.training.TrainingViewDto;
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
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@SpringJUnitConfig(classes = {TestWebConfig.class})
public class TrainingControllerIntegrationTest {
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private ObjectMapper mapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
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
        //Trainee
        TraineeDto traineeDto = buildTraineeDto();
        LoginUserDto responseTraineeDto = registerTrainee(traineeDto);
        String traineeUsername = responseTraineeDto.getUsername();

        //Trainer
        TrainerDto trainerDto = buildTrainerDto();
        LoginUserDto responseTrainerDto = registerTrainer(trainerDto);
        String trainerUsername = responseTrainerDto.getUsername();

        //Training
        TrainingDto trainingDto = buildTrainingDto();
        trainingDto.getTrainerDto().setUsername(trainerUsername);
        trainingDto.getTraineeDto().setUsername(traineeUsername);
        TrainingAddDto trainingAddDto = conversionService.convert(trainingDto, TrainingAddDto.class);

        String trainingAddDtoDtoJson = mapper.writeValueAsString(trainingAddDto);

        MvcResult mvcResult = mockMvc.perform(post("/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(trainingAddDtoDtoJson))
                .andExpect(status().isCreated())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        TrainingViewDto responseDto = mapper.readValue(response, TrainingViewDto.class);
        assertNotNull(responseDto);
        assertNotNull(responseDto.getTraineeUsername());
        assertNotNull(responseDto.getTrainerUsername());
        assertNotNull(responseDto.getTrainingType());
        assertNotNull(responseDto.getTrainingDateTime());
        assertNotNull(responseDto.getTrainingDuration());
        assertNotNull(responseDto.getTrainingName());
    }

    @Test
    public void testGetTrainingTypes() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/trainings/types"))
                .andExpect(status().isOk())
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        TrainingTypeListDto responseDto = mapper.readValue(response, TrainingTypeListDto.class);
        assertTrue(responseDto.getTrainingTypes().size() > 0);
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
