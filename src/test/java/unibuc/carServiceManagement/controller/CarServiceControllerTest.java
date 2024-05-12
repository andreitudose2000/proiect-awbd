package unibuc.carServiceManagement.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import unibuc.carServiceManagement.domain.CarService;
import unibuc.carServiceManagement.service.CarServiceService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
public class CarServiceControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    CarServiceService carServiceService;
    @MockBean
    Model model;

    @Test
    @DisplayName("Get car service info - happy flow")
    public void getCarServiceInfoHappy() throws Exception {
        Long id = 1L;
        CarService carService = new CarService();
        carService.setId(id);
        carService.setName("TestService");
        when(carServiceService.getCarService(id)).thenReturn(carService);
        mockMvc.perform(get("/car-services/info/{id}", id.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("carService/carServiceInfo.html"))
                .andExpect(model().attribute("object", carService))

                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

}