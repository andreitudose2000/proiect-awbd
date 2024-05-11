package unibuc.clinicmngmnt.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import unibuc.clinicmngmnt.domain.Clinic;
import unibuc.clinicmngmnt.dto.ClinicDto;
import unibuc.clinicmngmnt.exception.DuplicateClinicException;
import unibuc.clinicmngmnt.exception.NotFoundException;
import unibuc.clinicmngmnt.mapper.ClinicMapper;
import unibuc.clinicmngmnt.repository.ClinicRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ClinicServiceTest {
    @Mock
    private ClinicRepository clinicRepository;
    @InjectMocks
    private ClinicService clinicService;
    @Mock
    private ClinicMapper clinicMapper;

    @Test
    @DisplayName("Create clinic - happy flow")

    void createClinicHappy() {
        // Arrange
        ClinicDto clinicDto = new ClinicDto();
        clinicDto.setName("Clinic Name");
        clinicDto.setAddress("Clinic Address");

        Clinic clinic = new Clinic();
        clinic.setName("Clinic Name");
        clinic.setAddress("Clinic Address");

        when(clinicMapper.clinicDtoToClinic(clinicDto)).thenReturn(clinic);

        when(clinicRepository.findByName(clinic.getName()))
                .thenReturn(Optional.empty());
        

        Clinic createdClinic = new Clinic();
        createdClinic.setName("Clinic Name");
        createdClinic.setAddress("Clinic Address");
        createdClinic.setId(1);
        when(clinicRepository.save(clinic)).thenReturn(createdClinic);

        // Act
        Clinic result = clinicService.createClinic(clinicDto);


        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(createdClinic.getId(), result.getId());
        assertEquals(createdClinic.getName(), result.getName());
        assertEquals(createdClinic.getAddress(), result.getAddress());

    }

    @Test
    @DisplayName("Create clinic - throw clinic already exists error")
    void createClinicThrowClinicAlreadyExists() {
        // Arrange
        ClinicDto clinicDto = new ClinicDto();
        clinicDto.setName("Clinic Name");
        clinicDto.setAddress("Clinic Address");

        Clinic clinic = new Clinic();
        clinic.setName("Clinic Name");
        clinic.setAddress("Clinic Address");

        when(clinicMapper.clinicDtoToClinic(clinicDto)).thenReturn(clinic);
        when(clinicRepository.findByName(clinic.getName()))
                .thenReturn(Optional.of(clinic));

        // Act
        DuplicateClinicException exception = assertThrows(DuplicateClinicException.class,
                () -> clinicService.createClinic(clinicDto));

        // Assert
        assertEquals("A clinic with the same name already exists.", exception.getMessage());
    }
    @Test
    @DisplayName("Get clinic - happy flow")
    void getClinicHappy() {
        // Arrange
        Clinic clinic = new Clinic();
        clinic.setId(1);
        when(clinicRepository.findById(1L)).thenReturn(Optional.of(clinic));

        // Act
        Clinic result = clinicService.getClinic(1L);

        // Assert
        assertNotNull(result);
        assertEquals(clinic.getId(), result.getId());
    }

    @Test
    @DisplayName("Get clinic - throw clinic not found exception")
    void getClinicThrowClinicNotFound() {
        // Arrange
        when(clinicRepository.findById(1L)).thenReturn(Optional.empty());
        // Act
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> clinicService.getClinic(1L));

        // Assert
        assertEquals("Clinic with ID " + "1" + " not found.", exception.getMessage());

    }

    @Test
    @DisplayName("Delete clinic - throw clinic not found exception")
    void deleteClinicHappy() {
        // Arrange
        Clinic clinic = new Clinic();
        clinic.setId(1);
        when(clinicRepository.findById(1L)).thenReturn(Optional.of(clinic));

        // Act
        clinicService.deleteClinic(1L);

        // Assert
        verify(clinicRepository, times(1)).delete(clinic);
    }
    @Test
    @DisplayName("Delete clinic - throw clinic not found exception")
    void deleteClinicThrowClinicNotFound() {
        // Arrange
        when(clinicRepository.findById(1L)).thenReturn(Optional.empty());
        // Act
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> clinicService.getClinic(1L));

        // Assert
        assertEquals("Clinic with ID " + "1" + " not found.", exception.getMessage());
    }
}
