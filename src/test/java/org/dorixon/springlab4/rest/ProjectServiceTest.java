package org.dorixon.springlab4.rest;

import org.dorixon.springlab4.repository.ProjektRepository;
import org.dorixon.springlab4.repository.ZadanieRepository;
import org.dorixon.springlab4.service.ProjektService;
import org.dorixon.springlab4.service.ProjektServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {
    @Mock
    private ProjektRepository mockProjektRepository;

    @Mock
    private ZadanieRepository mockZadanieRepository;

    @InjectMocks
    private ProjektServiceImpl projektService;

    //TODO
}
