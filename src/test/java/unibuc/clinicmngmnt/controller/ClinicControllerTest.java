package unibuc.clinicmngmnt.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import unibuc.clinicmngmnt.domain.Clinic;
import unibuc.clinicmngmnt.exception.NotFoundException;
import unibuc.clinicmngmnt.service.ClinicService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
public class ClinicControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    ClinicService clinicService;
    @MockBean
    Model model;

    @Test
    @DisplayName("Get clinic info - happy flow")
    public void getClinicInfoHappy() throws Exception {
        Long id = 1L;
        Clinic clinic = new Clinic();
        clinic.setId(id);
        clinic.setName("TestClinic");
        when(clinicService.getClinic(id)).thenReturn(clinic);
        mockMvc.perform(get("/clinics/info/{id}", id.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("clinics/clinicsInfo.html"))
                .andExpect(model().attribute("object", clinic))

                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

}