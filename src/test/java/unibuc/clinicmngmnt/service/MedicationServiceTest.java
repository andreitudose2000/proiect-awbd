package unibuc.clinicmngmnt.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import unibuc.clinicmngmnt.dto.MedicationDto;
import unibuc.clinicmngmnt.mapper.MedicationMapper;
import unibuc.clinicmngmnt.domain.Medication;
import unibuc.clinicmngmnt.repository.MedicationRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MedicationServiceTest {
    @Mock
    private MedicationRepository medicationRepository;
    @InjectMocks
    private MedicationService medicationService;
    @Mock
    private MedicationMapper medicationMapper;

    @Test
    @DisplayName("Create medication - happy flow")
    void createMedicationHappy() {
        // Arrange
        MedicationDto medicationDto = new MedicationDto("Medication Name", "Medication Brand");
        Medication medication = new Medication("Medication Name", "Medication Brand");

        when(medicationMapper.medicationDtoToMedication(medicationDto)).thenReturn(medication);

        Medication createdMedication = new Medication("Medication Name", "Medication Brand");
        createdMedication.setId(1);
        when(medicationRepository.save(medication)).thenReturn(createdMedication);

        // Act
        Medication result = medicationService.createMedication(medicationDto);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(createdMedication.getId(), result.getId());
        assertEquals(createdMedication.getName(), result.getName());
        assertEquals(createdMedication.getBrand(), result.getBrand());

    }
    @Test
    @DisplayName("Get all medications - happy flow no brand")
    void getAllMedicationsHappyNoBrand() {
        // Arrange
        Medication medication1 = new Medication("Medication Nam1", "Medication Brand1");
        Medication medication2 = new Medication("Medication Name2", "Medication Brand2");
        List<Medication> medications = new ArrayList<Medication>();
        medications.add(medication1);
        medications.add(medication2);

        Medication medication = new Medication();
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("id");
        when(medicationRepository.findAll(Example.of(medication, matcher))).thenReturn(medications);

        // Act
        List <Medication> retrievedMedications = medicationService.getAllMedications(null);

        // Assert
        assertNotNull(retrievedMedications);
        assertEquals(retrievedMedications.size(), medications.size());

    }
    @Test
    @DisplayName("Get all medications - happy flow with brand")
    void getAllMedicationsHappyWithBrand() {

        // Arrange
        Medication medication1 = new Medication("Medication Nam1", "Medication Brand1");
        Medication medication2 = new Medication("Medication Name2", "Medication Brand2");
        List<Medication> medications = new ArrayList<Medication>();
        medications.add(medication1);
        medications.add(medication2);

        List<Medication> filteredMedications = new ArrayList<Medication>();
        filteredMedications.add(medication2);

        Medication medication = new Medication();
        medication.setBrand("Medication Brand2");
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("id");
        when(medicationRepository.findAll(Example.of(medication, matcher))).thenReturn(filteredMedications);

        // Act
        List <Medication> retrievedMedications = medicationService.getAllMedications("Medication Brand2");

        // Assert
        assertNotNull(retrievedMedications);
        assertEquals(retrievedMedications.size(), filteredMedications.size());
        assertEquals(retrievedMedications.get(0).getBrand(), retrievedMedications.get(0).getBrand());

    }
}
