package com.service.gamestoreinvoicing.repository;

import com.service.gamestoreinvoicing.model.ProcessingFee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ProcessingFeeRepositoryTest {

    @Autowired
    ProcessingFeeRepository processingFeeRepository;

    private ProcessingFee tShirtProcessingFee;
    private ProcessingFee consoleProcessingFee;
    private ProcessingFee gameProcessingFee;

    @org.junit.Before
    public void setUp() throws Exception {
        processingFeeRepository.deleteAll();

        // Arrange
        tShirtProcessingFee = new ProcessingFee();
        tShirtProcessingFee.setProductType("T-Shirts");
        tShirtProcessingFee.setFee(new BigDecimal("1.98"));

        consoleProcessingFee = new ProcessingFee();
        consoleProcessingFee.setProductType("Consoles");
        consoleProcessingFee.setFee(new BigDecimal("14.99"));

        gameProcessingFee = new ProcessingFee();
        gameProcessingFee.setProductType("Games");
        gameProcessingFee.setFee(new BigDecimal("1.49"));

        // Act
        processingFeeRepository.save(tShirtProcessingFee);
        processingFeeRepository.save(consoleProcessingFee);
        processingFeeRepository.save(gameProcessingFee);
    }

    @Test
    public void getProcessingFee() {

        // Assert
        Optional<ProcessingFee> foundFee;

        foundFee = processingFeeRepository.findById("T-Shirts");
        assertTrue(foundFee.isPresent());
        assertEquals(foundFee.get().getFee(), new BigDecimal("1.98"));

        foundFee = processingFeeRepository.findById("Consoles");
        assertTrue(foundFee.isPresent());
        assertEquals(foundFee.get().getFee(), new BigDecimal("14.99"));

        foundFee = processingFeeRepository.findById("Games");
        assertTrue(foundFee.isPresent());
        assertEquals(foundFee.get().getFee(), new BigDecimal("1.49"));
    }

    @Test
    public void getProcessingFeeObject() {

        // Assert
        Optional<ProcessingFee> foundProcessingFee = processingFeeRepository.findById(tShirtProcessingFee.getProductType());
        assertTrue(foundProcessingFee.isPresent());
        assertEquals(tShirtProcessingFee, foundProcessingFee.get());

        foundProcessingFee = processingFeeRepository.findById(consoleProcessingFee.getProductType());
        assertTrue(foundProcessingFee.isPresent());
        assertEquals(consoleProcessingFee, foundProcessingFee.get());

        foundProcessingFee = processingFeeRepository.findById(gameProcessingFee.getProductType());
        assertTrue(foundProcessingFee.isPresent());
        assertEquals(gameProcessingFee, foundProcessingFee.get());
    }
}
