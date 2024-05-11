package unibuc.clinicmngmnt.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.ui.Model;
import unibuc.clinicmngmnt.domain.Doctor;
import unibuc.clinicmngmnt.service.DoctorService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DoctorControllerTest {
    @Mock
    Model model;
    @Mock
    DoctorService doctorService;
    DoctorController doctorController;

    @BeforeEach
    public void setUp() throws Exception {
        doctorController = new DoctorController(doctorService);
    }

    @Test
    @DisplayName("Get doctor info - happy flow")
    public void getDoctorInfo() {
        Long id = 1L;
        Doctor doctor = new Doctor();
        doctor.setId(id);

        when(doctorService.getDoctor(id)).thenReturn(doctor);

        String viewName = doctorController.getDoctor(model, id);
        assertEquals("doctors/doctorsInfo.html", viewName);
        verify(doctorService, times(1)).getDoctor(id);
        ArgumentCaptor<Doctor> argumentCaptor =
                ArgumentCaptor.forClass(Doctor.class);
        verify(model, times(1))
                .addAttribute(eq("object"), argumentCaptor.capture());
        Doctor doctorArg = argumentCaptor.getValue();
        assertEquals(doctorArg.getId(), doctorArg.getId());
    }
}