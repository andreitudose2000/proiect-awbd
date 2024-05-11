package unibuc.clinicmngmnt.mapper;

import org.springframework.stereotype.Component;
import unibuc.clinicmngmnt.dto.PartDto;
import unibuc.clinicmngmnt.domain.Part;

@Component
public class PartMapper {
    public Part partDtoToPart(PartDto partDto) {
        return new Part(partDto.getName(), partDto.getBrand());
    }
}
