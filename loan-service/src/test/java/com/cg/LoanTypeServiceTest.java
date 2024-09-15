package com.cg;

import com.cg.entity.Loantype;
import com.cg.repository.LoantypeRepository;
import com.cg.service.LoantypeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoanTypeServiceTest {

    @InjectMocks
    private LoantypeService loantypeService;

    @Mock
    private LoantypeRepository loantypeRepository;

    @Mock
    private Loantype loantype;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test case for savetype method
    @Test
    public void testSavetype() {
        // Mocking the behavior of the repository
        when(loantypeRepository.save(loantype)).thenReturn(loantype);

        Loantype result = loantypeService.savetype(loantype);


        assertNotNull(result);
        assertEquals(loantype, result);
        verify(loantypeRepository).save(loantype);
    }

    // Test case for getAllTypes method
    @Test
    public void testGetAllTypes() {
        // Mocking the behavior of the repository
        List<Loantype> loanTypes = new ArrayList<>();
        loanTypes.add(new Loantype());
        loanTypes.add(new Loantype());
        when(loantypeRepository.findAll()).thenReturn(loanTypes);

        List<Loantype> result = loantypeService.getAllTypes();

        // Assertions
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(loantypeRepository).findAll();
    }
}