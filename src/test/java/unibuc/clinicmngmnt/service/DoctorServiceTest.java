package unibuc.clinicmngmnt.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import unibuc.clinicmngmnt.dto.DoctorDto;
import unibuc.clinicmngmnt.exception.NotFoundException;
import unibuc.clinicmngmnt.exception.SpecialityException;
import unibuc.clinicmngmnt.mapper.DoctorMapper;
import unibuc.clinicmngmnt.domain.Clinic;
import unibuc.clinicmngmnt.domain.Doctor;
import unibuc.clinicmngmnt.domain.Speciality;
import unibuc.clinicmngmnt.repository.ClinicRepository;
import unibuc.clinicmngmnt.repository.DoctorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private ClinicRepository clinicRepository;
    @InjectMocks
    private DoctorService doctorService;
    @Mock
    private DoctorMapper doctorMapper;

    @Test
    @DisplayName("Create doctor - happy flow no clinic")
    void createDoctorHappyNoClinic() {
        // Arrange
        DoctorDto doctorDto = new DoctorDto("Doctor First Name", "Doctor Last Name", Speciality.SURGEON);
        Doctor doctor = new Doctor("Doctor First Name", "Doctor Last Name", Speciality.SURGEON);

        when(doctorMapper.doctorDtoToDoctor(doctorDto)).thenReturn(doctor);

        Doctor createdDoctor = new Doctor("Doctor First Name", "Doctor Last Name", Speciality.SURGEON);
        createdDoctor.setId(1);

        when(doctorRepository.save(doctor)).thenReturn(createdDoctor);

        // Act
        Doctor result = doctorService.createDoctor(doctorDto);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(createdDoctor.getId(), result.getId());
        assertEquals(createdDoctor.getFirstName(), result.getFirstName());
        assertEquals(createdDoctor.getLastName(), result.getLastName());


    }
    @Test
    @DisplayName("Create doctor - happy flow with clinic")
    void createDoctorHappyWithClinic() {
        // Arrange
        DoctorDto doctorDto = new DoctorDto("Doctor First Name", "Doctor Last Name", Speciality.SURGEON, 1L);
        Doctor doctor = new Doctor("Doctor First Name", "Doctor Last Name", Speciality.SURGEON);
        Clinic clinic = new Clinic("Clinic Name", "Clinic Address");
        clinic.setId(1);

        when(doctorMapper.doctorDtoToDoctor(doctorDto)).thenReturn(doctor);
        when(clinicRepository.findById(1L)).thenReturn(Optional.of(clinic));

        Doctor createdDoctor = new Doctor("Doctor First Name", "Doctor Last Name", Speciality.SURGEON);
        createdDoctor.setId(1);
        createdDoctor.setClinic(clinic);

        when(doctorRepository.save(doctor)).thenReturn(createdDoctor);

        // Act
        Doctor result = doctorService.createDoctor(doctorDto);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(createdDoctor.getId(), result.getId());
        assertEquals(createdDoctor.getFirstName(), result.getFirstName());
        assertEquals(createdDoctor.getLastName(), result.getLastName());
        assertEquals(createdDoctor.getClinic().getId(), result.getClinic().getId());
        assertEquals(createdDoctor.getClinic().getName(), result.getClinic().getName());
        assertEquals(createdDoctor.getClinic().getAddress(), result.getClinic().getAddress());
    }

    @Test
    @DisplayName("Get doctor - happy flow")
    void getDoctorHappy() {
        // Arrange
        Doctor doctor = new Doctor();
        doctor.setId(1);
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        // Act
        Doctor result = doctorService.getDoctor(1L);

        // Assert
        assertNotNull(result);
        assertEquals(doctor.getId(), result.getId());
    }

    @Test
    @DisplayName("Get doctor - throw doctor not found exception")
    void getDoctorThrowDoctorNotFound() {
        // Arrange
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());
        // Act
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> doctorService.getDoctor(1L));

        // Assert
        assertEquals("Doctor with ID " + "1" + " not found.", exception.getMessage());

    }

    @Test
    @DisplayName("Get all doctors - throw invalid speciality exception")
    void getAllDoctorsThrowSpecialityNotValid() {
        // Arrange
        String speciality = "TEST";
        int page = 1;
        int size = 5;
        List<String> sortList = new ArrayList<>();
        sortList.add("id");
        Sort.Direction sortOrder = Sort.Direction.fromString("ASC");


        // Act
        SpecialityException exception = assertThrows(SpecialityException.class,
                () -> doctorService.getAllDoctors(speciality, page, size, sortList, sortOrder));

        // Assert
        assertEquals("Speciality must be one of the following: ENDODONTIST, ORTHODONTIST, PERIODONTIST, SURGEON", exception.getMessage());

    }

    @Test
    @DisplayName("Delete doctor - throw doctor not found exception")
    void deleteDoctorHappy() {
        // Arrange
        Doctor doctor = new Doctor();
        doctor.setId(1);
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        // Act
        doctorService.deleteDoctor(1L);

        // Assert
        verify(doctorRepository, times(1)).delete(doctor);
    }
    @Test
    @DisplayName("Delete doctor - throw doctor not found exception")
    void deleteDoctorThrowDoctorNotFound() {
        // Arrange
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());
        // Act
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> doctorService.getDoctor(1L));

        // Assert
        assertEquals("Doctor with ID " + "1" + " not found.", exception.getMessage());
    }



}
