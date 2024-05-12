package unibuc.carServiceManagement.service;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import unibuc.carServiceManagement.dto.PartDto;
import unibuc.carServiceManagement.mapper.PartMapper;
import unibuc.carServiceManagement.domain.Part;
import unibuc.carServiceManagement.repository.PartRepository;

import java.util.List;

@Service
public class PartService {
    private final PartRepository partRepository;
    private final PartMapper partMapper;

    public PartService(PartRepository partRepository, PartMapper partMapper) {
        this.partRepository = partRepository;
        this.partMapper = partMapper;
    }

    public Part createPart(PartDto partDto) {
        Part part = partMapper.partDtoToPart(partDto);
        return partRepository.save(part);
    }

    public List<Part> getAllParts(String brand) {
        Part part = new Part();

        if (brand != null) {
            part.setBrand(brand);
        }
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("id");

        return partRepository.findAll(Example.of(part, matcher));
    }
}
