package unibuc.carServiceManagement.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import unibuc.carServiceManagement.domain.CarService;
import unibuc.carServiceManagement.dto.CarServiceDto;
import unibuc.carServiceManagement.exception.DuplicateCarServiceException;
import unibuc.carServiceManagement.exception.NotFoundException;
import unibuc.carServiceManagement.mapper.CarServiceMapper;
import unibuc.carServiceManagement.repository.CarServiceRepository;
import unibuc.carServiceManagement.repository.MechanicRepository;

import java.util.Optional;

@Service
public class CarServiceService {
    private final CarServiceRepository carServiceRepository;
    private final MechanicRepository mechanicRepository;
    private final CarServiceMapper carServiceMapper;


    public CarServiceService(CarServiceRepository carServiceRepository, MechanicRepository mechanicRepository, CarServiceMapper carServiceMapper) {
        this.carServiceRepository = carServiceRepository;
        this.mechanicRepository = mechanicRepository;
        this.carServiceMapper = carServiceMapper;
    }

    public CarService createCarService(CarServiceDto carServiceDto) {
        CarService carService = carServiceMapper.carServiceDtoToCarService(carServiceDto);
        Optional<CarService> existingCarServiceSameName = carServiceRepository.findByName(carService.getName());
        existingCarServiceSameName.ifPresent(e -> {
            throw new DuplicateCarServiceException();
        });
        return carServiceRepository.save(carService);
    }

    public CarService getCarService(Long id) {
        return carServiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Mecanicul cu ID " + id + " nu a fost gasit."));
    }

    public Page<CarService> getAllCarServices(PageRequest pageRequest) {
        return carServiceRepository.findAll(pageRequest);
    }


    public void deleteCarService(Long id) {
        CarService carService = carServiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Service-ul cu ID " + id + " nu a fost gasit."));
        carServiceRepository.delete(carService);
    }


}
