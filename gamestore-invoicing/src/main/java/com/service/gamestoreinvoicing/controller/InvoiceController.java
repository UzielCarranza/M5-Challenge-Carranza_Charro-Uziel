package com.service.gamestoreinvoicing.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/invoice")
@CrossOrigin(origins = {"http://localhost:3000"})
public class InvoiceController {

    @GetMapping
    public void test(){
        System.out.println("this is a test");
    }
//
//    @Autowired
//    GameStoreServiceLayer service;
//
//    // Assumption: All orders are final and data privacy is not top priority. Therefore, the Update & Delete EndPoints
//    // are left out by design due to its potential danger. The getAllInvoices is a questionable one since it could
//    // overwhelm the system and infringes on data privacy; however, it does not damage data as with the Update and Delete
//
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public InvoiceViewModel purchaseItem(@RequestBody @Valid InvoiceViewModel invoiceViewModel) {
//        invoiceViewModel = service.createInvoice(invoiceViewModel);
//        return invoiceViewModel;
//    }
//
//    @GetMapping("/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public InvoiceViewModel findInvoice(@PathVariable("id") long invoiceId) {
//        InvoiceViewModel invoiceViewModel = service.getInvoice(invoiceId);
//        if (invoiceViewModel == null) {
//            throw new IllegalArgumentException("Invoice could not be retrieved for id " + invoiceId);
//        } else {
//            return invoiceViewModel;
//        }
//    }
//
//    @GetMapping()
//    @ResponseStatus(HttpStatus.OK)
//    public List<InvoiceViewModel> findAllInvoices() {
//        List<InvoiceViewModel> invoiceViewModelList = service.getAllInvoices();
//
//        if (invoiceViewModelList == null || invoiceViewModelList.isEmpty()) {
//            throw new IllegalArgumentException("No invoices were found.");
//        } else {
//            return invoiceViewModelList;
//        }
//    }
//
//    @GetMapping("/cname/{name}")
//    @ResponseStatus(HttpStatus.OK)
//    public List<InvoiceViewModel> findInvoicesByCustomerName(@PathVariable String name) {
//        List<InvoiceViewModel> invoiceViewModelList = service.getInvoicesByCustomerName(name);
//
//        if (invoiceViewModelList == null || invoiceViewModelList.isEmpty()) {
//            throw new IllegalArgumentException("No invoices were found for: "+name);
//        } else {
//            return invoiceViewModelList;
//        }
//    }
}
