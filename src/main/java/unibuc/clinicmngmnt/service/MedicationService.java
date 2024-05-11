package unibuc.clinicmngmnt.service;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import unibuc.clinicmngmnt.dto.MedicationDto;
import unibuc.clinicmngmnt.mapper.MedicationMapper;
import unibuc.clinicmngmnt.domain.Medication;
import unibuc.clinicmngmnt.repository.MedicationRepository;

import java.util.List;

@Service
public class MedicationService {
    private final MedicationRepository medicationRepository;
    private final MedicationMapper medicationMapper;

    public MedicationService(MedicationRepository medicationRepository, MedicationMapper medicationMapper) {
        this.medicationRepository = medicationRepository;
        this.medicationMapper = medicationMapper;
    }

    public Medication createMedication(MedicationDto medicationDto) {
        Medication medication = medicationMapper.medicationDtoToMedication(medicationDto);
        return medicationRepository.save(medication);
    }

    public List<Medication> getAllMedications(String brand) {
        Medication medication = new Medication();

        if (brand != null) {
            medication.setBrand(brand);
        }
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("id");

        return medicationRepository.findAll(Example.of(medication, matcher));
    }
}
