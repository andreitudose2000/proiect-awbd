package unibuc.clinicmngmnt.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import unibuc.clinicmngmnt.dto.ClinicDto;
import unibuc.clinicmngmnt.exception.DuplicateClinicException;
import unibuc.clinicmngmnt.exception.NotFoundException;
import unibuc.clinicmngmnt.mapper.ClinicMapper;
import unibuc.clinicmngmnt.domain.Clinic;
import unibuc.clinicmngmnt.repository.ClinicRepository;
import unibuc.clinicmngmnt.repository.DoctorRepository;

import java.util.Optional;

@Service
public class ClinicService {
    private final ClinicRepository clinicRepository;
    private final DoctorRepository doctorRepository;
    private final ClinicMapper clinicMapper;


    public ClinicService(ClinicRepository clinicRepository, DoctorRepository doctorRepository, ClinicMapper clinicMapper) {
        this.clinicRepository = clinicRepository;
        this.doctorRepository = doctorRepository;
        this.clinicMapper = clinicMapper;
    }

    public Clinic createClinic(ClinicDto clinicDto) {
        Clinic clinic = clinicMapper.clinicDtoToClinic(clinicDto);
        Optional<Clinic> existingClinicSameName = clinicRepository.findByName(clinic.getName());
        existingClinicSameName.ifPresent(e -> {
            throw new DuplicateClinicException();
        });
        return clinicRepository.save(clinic);
    }

    public Clinic getClinic(Long id) {
        return clinicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Clinic with ID " + id + " not found."));
    }

    public Page<Clinic> getAllClinics(PageRequest pageRequest) {
        return clinicRepository.findAll(pageRequest);
    }


    public void deleteClinic(Long id) {
        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Clinic with ID " + id + " not found."));
        clinicRepository.delete(clinic);
    }


}