package unibuc.clinicmngmnt.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.ui.Model;
import unibuc.clinicmngmnt.domain.Mechanic;
import unibuc.clinicmngmnt.service.MechanicService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MechanicControllerTest {
    @Mock
    Model model;
    @Mock
    MechanicService mechanicService;
    MechanicController mechanicController;

    @BeforeEach
    public void setUp() throws Exception {
        mechanicController = new MechanicController(mechanicService);
    }

    @Test
    @DisplayName("Get mechanic info - happy flow")
    public void getMechanicInfo() {
        Long id = 1L;
        Mechanic mechanic = new Mechanic();
        mechanic.setId(id);

        when(mechanicService.getMechanic(id)).thenReturn(mechanic);

        String viewName = mechanicController.getMechanic(model, id);
        assertEquals("mechanics/mechanicsInfo.html", viewName);
        verify(mechanicService, times(1)).getMechanic(id);
        ArgumentCaptor<Mechanic> argumentCaptor =
                ArgumentCaptor.forClass(Mechanic.class);
        verify(model, times(1))
                .addAttribute(eq("object"), argumentCaptor.capture());
        Mechanic mechanicArg = argumentCaptor.getValue();
        assertEquals(mechanicArg.getId(), mechanicArg.getId());
    }
}