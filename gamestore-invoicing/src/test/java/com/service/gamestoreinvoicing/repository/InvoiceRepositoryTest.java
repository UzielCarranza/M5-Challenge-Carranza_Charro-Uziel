package com.service.gamestoreinvoicing.repository;

import com.service.gamestoreinvoicing.feign.GameStoreCatalogClient;
import com.service.gamestoreinvoicing.model.Invoice;
import com.service.gamestoreinvoicing.model.ProcessingFee;
import com.service.gamestoreinvoicing.model.Tax;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest

public class InvoiceRepositoryTest {

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    TaxRepository taxRepository;

    @Autowired
    ProcessingFeeRepository processingFeeRepository;

    @MockBean
    GameStoreCatalogClient catalogClient;

    private Invoice invoice;

    @Before
    public void setUp() throws Exception {
        invoiceRepository.deleteAll();
        processingFeeRepository.deleteAll();

        ProcessingFee tShirtProcessingFee = new ProcessingFee();
        tShirtProcessingFee.setProductType("T-Shirts");
        tShirtProcessingFee.setFee(new BigDecimal("1.98"));

        ProcessingFee consoleProcessingFee = new ProcessingFee();
        consoleProcessingFee.setProductType("Consoles");
        consoleProcessingFee.setFee(new BigDecimal("14.99"));

        ProcessingFee gameProcessingFee = new ProcessingFee();
        gameProcessingFee.setProductType("Games");
        gameProcessingFee.setFee(new BigDecimal("1.49"));

        processingFeeRepository.save(tShirtProcessingFee);
        processingFeeRepository.save(consoleProcessingFee);
        processingFeeRepository.save(gameProcessingFee);

//        run method to save invoices in the database
        saveInvoicesInDatabase();
    }

    //Testing Invoice Operations...
//    CODE that Dan Mueller wrote during class on NOV, 03, 2022
    @Test
    public void shouldAddFindDeleteInvoice() {

        // get it back out of the database
        Invoice invoice2 = invoiceRepository.findById(invoice.getId()).get();

        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(invoice, invoice2);

        // delete it
        invoiceRepository.deleteById(invoice.getId());

        // go try to get it again
        Optional<Invoice> invoice3 = invoiceRepository.findById(invoice.getId());

        // confirm that it's gone
        assertEquals(false, invoice3.isPresent());
    }

    @Test
    public void shouldFindByName() {

        Optional<Tax> tax = taxRepository.findById(invoice.getState());
        assertTrue(tax.isPresent());
        invoice.setTax(invoice.getSubtotal().multiply(tax.get().getRate()));

        Optional<ProcessingFee> processingFee = processingFeeRepository.findById(invoice.getItemType());
        assertTrue(processingFee.isPresent());
        invoice.setProcessingFee(processingFee.get().getFee());

        invoice.setTotal(invoice.getSubtotal().add(invoice.getTax()).add(invoice.getProcessingFee()));

        //Act
        invoice = invoiceRepository.save(invoice);

        List<Invoice> foundNoinvoice = invoiceRepository.findByName("invalidValue");

        List<Invoice> foundOneinvoice = invoiceRepository.findByName(invoice.getName());

        //Assert
        assertEquals(foundOneinvoice.size(), 1);

        //Assert
        assertEquals(foundNoinvoice.size(), 0);
    }

    //    data set up
    public void saveInvoicesInDatabase() {
        // Build an invoice
        invoice = new Invoice();
        invoice.setUnitPrice(new BigDecimal("5.01"));
        invoice.setCity("Houston");
        invoice.setState("WA");
        invoice.setProcessingFee(new BigDecimal("1000"));
        invoice.setItemType("T-Shirts");
        invoice.setSubtotal(new BigDecimal("44.41"));
        invoice.setQuantity(3);
        invoice.setTax(new BigDecimal("1.11"));
        invoice.setTotal(new BigDecimal("22.22"));
        invoice.setZipcode("44334");
        invoice.setName("adafasfasdfasdf");
        invoice.setItemId(1);
        invoice.setStreet("some stree");

        // save to database
        invoice = invoiceRepository.save(invoice);

    }
}