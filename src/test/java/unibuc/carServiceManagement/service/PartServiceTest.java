package unibuc.carServiceManagement.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import unibuc.carServiceManagement.domain.Part;
import unibuc.carServiceManagement.dto.PartDto;
import unibuc.carServiceManagement.mapper.PartMapper;
import unibuc.carServiceManagement.repository.PartRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PartServiceTest {
    @Mock
    private PartRepository partRepository;
    @InjectMocks
    private PartService partService;
    @Mock
    private PartMapper partMapper;

    @Test
    @DisplayName("Create part - happy flow")
    void createPartHappy() {
        // Arrange
        PartDto partDto = new PartDto("Part Name", "Part Brand");
        Part part = new Part("Part Name", "Part Brand");

        when(partMapper.partDtoToPart(partDto)).thenReturn(part);

        Part createdPart = new Part("Part Name", "Part Brand");
        createdPart.setId(1);
        when(partRepository.save(part)).thenReturn(createdPart);

        // Act
        Part result = partService.createPart(partDto);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(createdPart.getId(), result.getId());
        assertEquals(createdPart.getName(), result.getName());
        assertEquals(createdPart.getBrand(), result.getBrand());

    }
    @Test
    @DisplayName("Get all parts - happy flow no brand")
    void getAllPartsHappyNoBrand() {
        // Arrange
        Part part1 = new Part("Part Nam1", "Part Brand1");
        Part part2 = new Part("Part Name2", "Part Brand2");
        List<Part> parts = new ArrayList<Part>();
        parts.add(part1);
        parts.add(part2);

        Part part = new Part();
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("id");
        when(partRepository.findAll(Example.of(part, matcher))).thenReturn(parts);

        // Act
        List <Part> retrievedParts = partService.getAllParts(null);

        // Assert
        assertNotNull(retrievedParts);
        assertEquals(retrievedParts.size(), parts.size());

    }
    @Test
    @DisplayName("Get all parts - happy flow with brand")
    void getAllPartsHappyWithBrand() {

        // Arrange
        Part part1 = new Part("Part Nam1", "Part Brand1");
        Part part2 = new Part("Part Name2", "Part Brand2");
        List<Part> parts = new ArrayList<Part>();
        parts.add(part1);
        parts.add(part2);

        List<Part> filteredParts = new ArrayList<Part>();
        filteredParts.add(part2);

        Part part = new Part();
        part.setBrand("Part Brand2");
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("id");
        when(partRepository.findAll(Example.of(part, matcher))).thenReturn(filteredParts);

        // Act
        List <Part> retrievedParts = partService.getAllParts("Part Brand2");

        // Assert
        assertNotNull(retrievedParts);
        assertEquals(retrievedParts.size(), filteredParts.size());
        assertEquals(retrievedParts.get(0).getBrand(), retrievedParts.get(0).getBrand());

    }
}
