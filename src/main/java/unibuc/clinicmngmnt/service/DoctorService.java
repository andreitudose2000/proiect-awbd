package unibuc.clinicmngmnt.service;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import unibuc.clinicmngmnt.dto.DoctorDto;
import unibuc.clinicmngmnt.exception.NotFoundException;
import unibuc.clinicmngmnt.exception.SpecialityException;
import unibuc.clinicmngmnt.mapper.DoctorMapper;
import unibuc.clinicmngmnt.domain.Clinic;
import unibuc.clinicmngmnt.domain.Doctor;
import unibuc.clinicmngmnt.domain.Speciality;
import unibuc.clinicmngmnt.repository.ClinicRepository;
import unibuc.clinicmngmnt.repository.DoctorRepository;
import unibuc.clinicmngmnt.utility.Utils;

import java.util.Arrays;
import java.util.List;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final ClinicRepository clinicRepository;
    private final DoctorMapper doctorMapper;

    public DoctorService(DoctorRepository doctorRepository, ClinicRepository clinicRepository, DoctorMapper doctorMapper) {
        this.doctorRepository = doctorRepository;
        this.clinicRepository = clinicRepository;
        this.doctorMapper = doctorMapper;
    }

    public Doctor createDoctor(DoctorDto doctorDto) {
        Long clinicId = doctorDto.getClinicId();

        Doctor doctor = doctorMapper.doctorDtoToDoctor(doctorDto);

        // if clinic is sent, check if it exists
        if (clinicId != null) {
            Clinic clinic = clinicRepository.findById(clinicId)
                    .orElseThrow(() -> new NotFoundException("Clinic with ID " + clinicId + " not found."));
            doctor.setClinic(clinic);
        }

        return doctorRepository.save(doctor);
    }

    public Doctor getDoctor(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Doctor with ID " + id + " not found."));
    }

    public Page<Doctor> getAllDoctors(String speciality, int page, int size, List<String> sortList, Sort.Direction sortOrder) {
        List<Sort.Order> orders = Utils.createSortOrder(sortList, sortOrder.toString());
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(orders));

        // validate speciality
        if (speciality != null) {
            boolean isSpecialityValid = Arrays.stream(Speciality.values()).anyMatch((t) -> t.name().equals(speciality));

            if (!isSpecialityValid) {
                throw new SpecialityException();
            }

            return doctorRepository.findBySpeciality(speciality, pageRequest);

        }
        return doctorRepository.findAll(pageRequest);
    }

    public void deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Doctor with ID " + id + " not found."));
        doctorRepository.delete(doctor);
    }

}
