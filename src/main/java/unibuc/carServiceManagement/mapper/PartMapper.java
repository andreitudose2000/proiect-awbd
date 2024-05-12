package unibuc.carServiceManagement.mapper;

import org.springframework.stereotype.Component;
import unibuc.carServiceManagement.dto.PartDto;
import unibuc.carServiceManagement.domain.Part;

@Component
public class PartMapper {
    public Part partDtoToPart(PartDto partDto) {
        return new Part(partDto.getName(), partDto.getBrand());
    }
}
