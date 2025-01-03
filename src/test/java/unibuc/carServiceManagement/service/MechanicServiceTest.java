package unibuc.carServiceManagement.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import unibuc.carServiceManagement.domain.CarService;
import unibuc.carServiceManagement.domain.Mechanic;
import unibuc.carServiceManagement.dto.MechanicDto;
import unibuc.carServiceManagement.exception.NotFoundException;
import unibuc.carServiceManagement.exception.SpecialityException;
import unibuc.carServiceManagement.mapper.MechanicMapper;
import unibuc.carServiceManagement.domain.Speciality;
import unibuc.carServiceManagement.repository.CarServiceRepository;
import unibuc.carServiceManagement.repository.MechanicRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class MechanicServiceTest {

    @Mock
    private MechanicRepository mechanicRepository;
    @Mock
    private CarServiceRepository carServiceRepository;
    @InjectMocks
    private MechanicService mechanicService;
    @Mock
    private MechanicMapper mechanicMapper;

    @Test
    @DisplayName("Create mechanic - happy flow with car service")
    void createMechanicHappyWithCarService() {
        // Arrange
        MechanicDto mechanicDto = new MechanicDto("Mechanic First Name", "Mechanic Last Name", Speciality.SUSPENSION, 1L);
        Mechanic mechanic = new Mechanic("Mechanic First Name", "Mechanic Last Name", Speciality.SUSPENSION);
        CarService carService = new CarService("CarService Name", "CarService Address");
        carService.setId(1);

        when(mechanicMapper.mechanicDtoToMechanic(mechanicDto)).thenReturn(mechanic);
        when(carServiceRepository.findById(1L)).thenReturn(Optional.of(carService));

        Mechanic createdMechanic = new Mechanic("Mechanic First Name", "Mechanic Last Name", Speciality.SUSPENSION);
        createdMechanic.setId(1);
        createdMechanic.setCarService(carService);

        when(mechanicRepository.save(mechanic)).thenReturn(createdMechanic);

        // Act
        Mechanic result = mechanicService.createMechanic(mechanicDto);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(createdMechanic.getId(), result.getId());
        assertEquals(createdMechanic.getFirstName(), result.getFirstName());
        assertEquals(createdMechanic.getLastName(), result.getLastName());
        assertEquals(createdMechanic.getCarService().getId(), result.getCarService().getId());
        assertEquals(createdMechanic.getCarService().getName(), result.getCarService().getName());
        assertEquals(createdMechanic.getCarService().getAddress(), result.getCarService().getAddress());
    }
    @Test
    @DisplayName("Create mechanic - happy flow no car service")
    void createMechanicHappyNoCarService() {
        // Arrange
        MechanicDto mechanicDto = new MechanicDto("Mechanic First Name", "Mechanic Last Name", Speciality.SUSPENSION);
        Mechanic mechanic = new Mechanic("Mechanic First Name", "Mechanic Last Name", Speciality.SUSPENSION);

        when(mechanicMapper.mechanicDtoToMechanic(mechanicDto)).thenReturn(mechanic);

        Mechanic createdMechanic = new Mechanic("Mechanic First Name", "Mechanic Last Name", Speciality.SUSPENSION);
        createdMechanic.setId(1);

        when(mechanicRepository.save(mechanic)).thenReturn(createdMechanic);

        // Act
        Mechanic result = mechanicService.createMechanic(mechanicDto);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(createdMechanic.getId(), result.getId());
        assertEquals(createdMechanic.getFirstName(), result.getFirstName());
        assertEquals(createdMechanic.getLastName(), result.getLastName());


    }

    @Test
    @DisplayName("Get mechanic - happy flow")
    void getMechanicHappy() {
        // Arrange
        Mechanic mechanic = new Mechanic();
        mechanic.setId(1);
        when(mechanicRepository.findById(1L)).thenReturn(Optional.of(mechanic));

        // Act
        Mechanic result = mechanicService.getMechanic(1L);

        // Assert
        assertNotNull(result);
        assertEquals(mechanic.getId(), result.getId());
    }

    @Test
    @DisplayName("Get mechanic - throw mechanic not found exception")
    void getMechanicThrowMechanicNotFound() {
        // Arrange
        when(mechanicRepository.findById(1L)).thenReturn(Optional.empty());
        // Act
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> mechanicService.getMechanic(1L));

        // Assert
        assertEquals("Mecanicul cu ID " + "1" + " nu a fost gasit.", exception.getMessage());

    }

    @Test
    @DisplayName("Get all mechanics - throw invalid speciality exception")
    void getAllMechanicsThrowSpecialityNotValid() {
        // Arrange
        String speciality = "TEST";
        int page = 1;
        int size = 5;
        List<String> sortList = new ArrayList<>();
        sortList.add("id");
        Sort.Direction sortOrder = Sort.Direction.fromString("ASC");


        // Act
        SpecialityException exception = assertThrows(SpecialityException.class,
                () -> mechanicService.getAllMechanics(speciality, page, size, sortList, sortOrder));

        // Assert
        assertEquals("Specialitatea trebuie sa fie una dintre urmatoarele: ELECTRICS, AUTOBODY, ENGINE, SUSPENSION", exception.getMessage());

    }

    @Test
    @DisplayName("Delete mechanic - throw mechanic not found exception")
    void deleteMechanicHappy() {
        // Arrange
        Mechanic mechanic = new Mechanic();
        mechanic.setId(1);
        when(mechanicRepository.findById(1L)).thenReturn(Optional.of(mechanic));

        // Act
        mechanicService.deleteMechanic(1L);

        // Assert
        verify(mechanicRepository, times(1)).delete(mechanic);
    }
    @Test
    @DisplayName("Delete mechanic - throw mechanic not found exception")
    void deleteMechanicThrowMechanicNotFound() {
        // Arrange
        when(mechanicRepository.findById(1L)).thenReturn(Optional.empty());
        // Act
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> mechanicService.getMechanic(1L));

        // Assert
        assertEquals("Mecanicul cu ID " + "1" + " nu a fost gasit.", exception.getMessage());
    }



}
