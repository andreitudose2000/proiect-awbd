package unibuc.carServiceManagement.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import unibuc.carServiceManagement.domain.CarService;
import unibuc.carServiceManagement.dto.CarServiceDto;
import unibuc.carServiceManagement.exception.DuplicateCarServiceException;
import unibuc.carServiceManagement.exception.NotFoundException;
import unibuc.carServiceManagement.mapper.CarServiceMapper;
import unibuc.carServiceManagement.repository.CarServiceRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CarServiceServiceTest {
    @Mock
    private CarServiceRepository carServiceRepository;
    @InjectMocks
    private CarServiceService carServiceService;
    @Mock
    private CarServiceMapper carServiceMapper;

    @Test
    @DisplayName("Create car service - happy flow")

    void createCarServiceHappy() {
        // Arrange
        CarServiceDto carServiceDto = new CarServiceDto();
        carServiceDto.setName("CarService Name");
        carServiceDto.setAddress("CarService Address");

        CarService carService = new CarService();
        carService.setName("CarService Name");
        carService.setAddress("CarService Address");

        when(carServiceMapper.carServiceDtoToCarService(carServiceDto)).thenReturn(carService);

        when(carServiceRepository.findByName(carService.getName()))
                .thenReturn(Optional.empty());


        CarService createdCarService = new CarService();
        createdCarService.setName("CarService Name");
        createdCarService.setAddress("CarService Address");
        createdCarService.setId(1);
        when(carServiceRepository.save(carService)).thenReturn(createdCarService);

        // Act
        CarService result = carServiceService.createCarService(carServiceDto);


        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(createdCarService.getId(), result.getId());
        assertEquals(createdCarService.getName(), result.getName());
        assertEquals(createdCarService.getAddress(), result.getAddress());

    }

    @Test
    @DisplayName("Create car service - throw already exists error")
    void createCarServiceThrowCarServiceAlreadyExists() {
        // Arrange
        CarServiceDto carServiceDto = new CarServiceDto();
        carServiceDto.setName("CarService Name");
        carServiceDto.setAddress("CarService Address");

        CarService carService = new CarService();
        carService.setName("CarService Name");
        carService.setAddress("CarService Address");

        when(carServiceMapper.carServiceDtoToCarService(carServiceDto)).thenReturn(carService);
        when(carServiceRepository.findByName(carService.getName()))
                .thenReturn(Optional.of(carService));

        // Act
        DuplicateCarServiceException exception = assertThrows(DuplicateCarServiceException.class,
                () -> carServiceService.createCarService(carServiceDto));

        // Assert
        assertEquals("Un service cu acelasi nume deja exista.", exception.getMessage());
    }
    @Test
    @DisplayName("Get car service - happy flow")
    void getCarServiceHappy() {
        // Arrange
        CarService carService = new CarService();
        carService.setId(1);
        when(carServiceRepository.findById(1L)).thenReturn(Optional.of(carService));

        // Act
        CarService result = carServiceService.getCarService(1L);

        // Assert
        assertNotNull(result);
        assertEquals(carService.getId(), result.getId());
    }

    @Test
    @DisplayName("Get car service - throw not found exception")
    void getCarServiceThrowCarServiceNotFound() {
        // Arrange
        when(carServiceRepository.findById(1L)).thenReturn(Optional.empty());
        // Act
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> carServiceService.getCarService(1L));

        // Assert
        assertEquals("CarService with ID " + "1" + " not found.", exception.getMessage());

    }

    @Test
    @DisplayName("Delete car service - throw not found exception")
    void deleteCarServiceHappy() {
        // Arrange
        CarService carService = new CarService();
        carService.setId(1);
        when(carServiceRepository.findById(1L)).thenReturn(Optional.of(carService));

        // Act
        carServiceService.deleteCarService(1L);

        // Assert
        verify(carServiceRepository, times(1)).delete(carService);
    }
    @Test
    @DisplayName("Delete car service - throw not found exception")
    void deleteCarServiceThrowCarServiceNotFound() {
        // Arrange
        when(carServiceRepository.findById(1L)).thenReturn(Optional.empty());
        // Act
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> carServiceService.getCarService(1L));

        // Assert
        assertEquals("CarService with ID " + "1" + " not found.", exception.getMessage());
    }
}
