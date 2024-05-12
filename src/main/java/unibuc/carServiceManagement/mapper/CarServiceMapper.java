package unibuc.carServiceManagement.mapper;

import org.springframework.stereotype.Component;
import unibuc.carServiceManagement.dto.CarServiceDto;
import unibuc.carServiceManagement.domain.CarService;

@Component
public class CarServiceMapper {
    public CarService carServiceDtoToCarService(CarServiceDto carServiceDto) {
        return new CarService(carServiceDto.getName(), carServiceDto.getAddress());
    }
}
