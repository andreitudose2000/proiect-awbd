package unibuc.carServiceManagement.service;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import unibuc.carServiceManagement.domain.CarService;
import unibuc.carServiceManagement.domain.Mechanic;
import unibuc.carServiceManagement.dto.MechanicDto;
import unibuc.carServiceManagement.exception.NotFoundException;
import unibuc.carServiceManagement.exception.SpecialityException;
import unibuc.carServiceManagement.mapper.MechanicMapper;
import unibuc.carServiceManagement.domain.Speciality;
import unibuc.carServiceManagement.repository.CarServiceRepository;
import unibuc.carServiceManagement.repository.MechanicRepository;
import unibuc.carServiceManagement.utility.Utils;

import java.util.Arrays;
import java.util.List;

@Service
public class MechanicService {
    private final MechanicRepository mechanicRepository;
    private final CarServiceRepository carServiceRepository;
    private final MechanicMapper mechanicMapper;

    public MechanicService(MechanicRepository mechanicRepository, CarServiceRepository carServiceRepository, MechanicMapper mechanicMapper) {
        this.mechanicRepository = mechanicRepository;
        this.carServiceRepository = carServiceRepository;
        this.mechanicMapper = mechanicMapper;
    }

    public Mechanic createMechanic(MechanicDto mechanicDto) {
        Long carServiceId = mechanicDto.getCarServiceId();

        Mechanic mechanic = mechanicMapper.mechanicDtoToMechanic(mechanicDto);

        // if CarService is sent, check if it exists
        if (carServiceId != null) {
            CarService carService = carServiceRepository.findById(carServiceId)
                    .orElseThrow(() -> new NotFoundException("Service-ul cu ID " + carServiceId + " nu a fost gasit."));
            mechanic.setCarService(carService);
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
