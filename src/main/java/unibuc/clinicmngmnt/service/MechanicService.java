package unibuc.clinicmngmnt.service;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import unibuc.clinicmngmnt.domain.Mechanic;
import unibuc.clinicmngmnt.dto.MechanicDto;
import unibuc.clinicmngmnt.exception.NotFoundException;
import unibuc.clinicmngmnt.exception.SpecialityException;
import unibuc.clinicmngmnt.mapper.MechanicMapper;
import unibuc.clinicmngmnt.domain.Clinic;
import unibuc.clinicmngmnt.domain.Speciality;
import unibuc.clinicmngmnt.repository.ClinicRepository;
import unibuc.clinicmngmnt.repository.MechanicRepository;
import unibuc.clinicmngmnt.utility.Utils;

import java.util.Arrays;
import java.util.List;

@Service
public class MechanicService {
    private final MechanicRepository mechanicRepository;
    private final ClinicRepository clinicRepository;
    private final MechanicMapper mechanicMapper;

    public MechanicService(MechanicRepository mechanicRepository, ClinicRepository clinicRepository, MechanicMapper mechanicMapper) {
        this.mechanicRepository = mechanicRepository;
        this.clinicRepository = clinicRepository;
        this.mechanicMapper = mechanicMapper;
    }

    public Mechanic createMechanic(MechanicDto mechanicDto) {
        Long clinicId = mechanicDto.getClinicId();

        Mechanic mechanic = mechanicMapper.mechanicDtoToMechanic(mechanicDto);

        // if clinic is sent, check if it exists
        if (clinicId != null) {
            Clinic clinic = clinicRepository.findById(clinicId)
                    .orElseThrow(() -> new NotFoundException("Clinic with ID " + clinicId + " not found."));
            mechanic.setClinic(clinic);
        }

        return mechanicRepository.save(mechanic);
    }

    public Mechanic getMechanic(Long id) {
        return mechanicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Mecanicul cu ID " + id + " nu a fost gasit."));
    }

    public Page<Mechanic> getAllMechanics(String speciality, int page, int size, List<String> sortList, Sort.Direction sortOrder) {
        List<Sort.Order> orders = Utils.createSortOrder(sortList, sortOrder.toString());
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(orders));

        // validate speciality
        if (speciality != null) {
            boolean isSpecialityValid = Arrays.stream(Speciality.values()).anyMatch((t) -> t.name().equals(speciality));

            if (!isSpecialityValid) {
                throw new SpecialityException();
            }

            return mechanicRepository.findBySpeciality(speciality, pageRequest);

        }
        return mechanicRepository.findAll(pageRequest);
    }

    public void deleteMechanic(Long id) {
        Mechanic mechanic = mechanicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Mecanicul cu ID " + id + " nu a fost gasit."));
        mechanicRepository.delete(mechanic);
    }

}
