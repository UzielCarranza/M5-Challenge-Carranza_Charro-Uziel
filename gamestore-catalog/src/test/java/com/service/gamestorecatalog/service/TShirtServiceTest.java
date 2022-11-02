package com.service.gamestorecatalog.service;

import com.service.gamestorecatalog.model.TShirt;
import com.service.gamestorecatalog.repository.TShirtRepository;
import com.service.gamestorecatalog.viewModel.TShirtViewModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class TShirtServiceTest {


    private TShirtRepository tShirtRepository;

    private TShirtService service;

    @Before
    public void setUp() throws Exception {

        setUpTShirtRepositoryMock();

        service = new TShirtService(tShirtRepository);
    }


    //Testing TShirt operations...
    @Test
    public void shouldCreateFindTShirt() {
        TShirtViewModel tShirt = new TShirtViewModel();
        tShirt.setSize("Medium");
        tShirt.setColor("Blue");
        tShirt.setDescription("V-Neck");
        tShirt.setPrice(new BigDecimal("19.99"));
        tShirt.setQuantity(5);
        tShirt = service.createTShirt(tShirt);

        TShirtViewModel tShirtFromService = service.getTShirt(tShirt.getId());

        assertEquals(tShirt, tShirtFromService);

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFaileWhenCreateTShirtWithNullViewModel() {
        TShirtViewModel tShirt = null;
        tShirt = service.createTShirt(tShirt);
    }

    @Test
    public void shouldUpdateTShirt() {

        TShirtViewModel tShirt = new TShirtViewModel();
        tShirt.setSize("Medium");
        tShirt.setColor("Blue");
        tShirt.setDescription("V-Neck");
        tShirt.setPrice(new BigDecimal("19.99"));
        tShirt.setQuantity(5);
        tShirt = service.createTShirt(tShirt);

        tShirt.setQuantity(3);
        tShirt.setPrice(new BigDecimal("18.99"));

        service.updateTShirt(tShirt);

        verify(tShirtRepository, times(2)).save(any(TShirt.class));

    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldFailUpdateTShirtWithNullViewModel() {

        TShirtViewModel tShirt = null;
        service.updateTShirt(tShirt);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailUpdateTShirtWithBadId() {

        TShirtViewModel tShirt = new TShirtViewModel();
        tShirt.setSize("Medium");
        tShirt.setColor("Blue");
        tShirt.setDescription("V-Neck");
        tShirt.setPrice(new BigDecimal("19.99"));
        tShirt.setQuantity(5);
        tShirt = service.createTShirt(tShirt);

        tShirt.setQuantity(3);
        tShirt.setPrice(new BigDecimal("18.99"));

        tShirt.setId(tShirt.getId()+1);
        service.updateTShirt(tShirt);
    }

    @Test
    public void shouldDeleteTShirt() {

        TShirtViewModel tShirt = new TShirtViewModel();
        tShirt.setSize("Medium");
        tShirt.setColor("Blue");
        tShirt.setDescription("V-Neck");
        tShirt.setPrice(new BigDecimal("19.99"));
        tShirt.setQuantity(5);
        tShirt = service.createTShirt(tShirt);

        service.deleteTShirt(tShirt.getId());

        verify(tShirtRepository).deleteById(any(Long.class));

    }

    @Test
    public void shouldFindTShirtByColor() {
        List<TShirtViewModel> tvmList = new ArrayList<>();

        TShirtViewModel tShirt = new TShirtViewModel();
        tShirt.setSize("Medium");
        tShirt.setColor("Blue");
        tShirt.setDescription("V-Neck");
        tShirt.setPrice(new BigDecimal("19.99"));
        tShirt.setQuantity(5);
        tShirt = service.createTShirt(tShirt);
        tvmList.add(tShirt);

        TShirtViewModel tShirtExtra2 = new TShirtViewModel();
        tShirtExtra2.setSize("Large");
        tShirtExtra2.setColor("Blue");
        tShirtExtra2.setDescription("long sleeve");
        tShirtExtra2.setPrice(new BigDecimal("30.99"));
        tShirtExtra2.setQuantity(8);
        tShirtExtra2 = service.createTShirt(tShirtExtra2);
        tvmList.add(tShirtExtra2);

        List<TShirtViewModel> tvmFromService = service.getTShirtByColor("Blue");

        assertEquals(tvmList, tvmFromService);

    }

    @Test
    public void shouldFindTShirtBySize() {
        List<TShirtViewModel> tvmList = new ArrayList<>();

        TShirtViewModel tShirt = new TShirtViewModel();
        tShirt.setSize("Medium");
        tShirt.setColor("Blue");
        tShirt.setDescription("V-Neck");
        tShirt.setPrice(new BigDecimal("19.99"));
        tShirt.setQuantity(5);
        tShirt = service.createTShirt(tShirt);
        tvmList.add(tShirt);

        TShirtViewModel tShirtExtra3 = new TShirtViewModel();
        tShirtExtra3.setSize("Medium");
        tShirtExtra3.setColor("orange");
        tShirtExtra3.setDescription("sleeveless");
        tShirtExtra3.setPrice(new BigDecimal("15.99"));
        tShirtExtra3.setQuantity(3);
        tShirtExtra3 = service.createTShirt(tShirtExtra3);
        tvmList.add(tShirtExtra3);

        List<TShirtViewModel> tvmFromService = service.getTShirtBySize("Medium");

        assertEquals(tvmList, tvmFromService);

    }

    @Test
    public void shouldFindAllTShirts() {
        List<TShirtViewModel> tvmList = new ArrayList<>();

        TShirtViewModel newTShirt1 = new TShirtViewModel();
        newTShirt1.setSize("Medium");
        newTShirt1.setColor("Blue");
        newTShirt1.setDescription("V-Neck");
        newTShirt1.setPrice(new BigDecimal("19.99"));
        newTShirt1.setQuantity(5);

        newTShirt1 = service.createTShirt(newTShirt1);
        tvmList.add(newTShirt1);

        TShirtViewModel newTShirt2 = new TShirtViewModel();
        newTShirt2.setSize("Large");
        newTShirt2.setColor("Blue");
        newTShirt2.setDescription("long sleeve");
        newTShirt2.setPrice(new BigDecimal("30.99"));
        newTShirt2.setQuantity(8);

        newTShirt2 = service.createTShirt(newTShirt2);
        tvmList.add(newTShirt2);

        TShirtViewModel newTShirt3 = new TShirtViewModel();
        newTShirt3.setSize("Medium");
        newTShirt3.setColor("orange");
        newTShirt3.setDescription("sleeveless");
        newTShirt3.setPrice(new BigDecimal("15.99"));
        newTShirt3.setQuantity(3);

        newTShirt3 = service.createTShirt(newTShirt3);
        tvmList.add(newTShirt3);

        List<TShirtViewModel> tvmFromService = service.getAllTShirts();

        assertEquals(tvmList, tvmFromService);
    }


    private void setUpTShirtRepositoryMock() {
        tShirtRepository = mock(TShirtRepository.class);

        List<TShirt> tShirtsByColor = new ArrayList<>();
        List<TShirt> tShirtsBySize = new ArrayList<>();
        List<TShirt> allTtShirts = new ArrayList<>();

        TShirt newTShirt1 = new TShirt();
        newTShirt1.setSize("Medium");
        newTShirt1.setColor("Blue");
        newTShirt1.setDescription("V-Neck");
        newTShirt1.setPrice(new BigDecimal("19.99"));
        newTShirt1.setQuantity(5);

        TShirt savedTShirt1 = new TShirt();
        savedTShirt1.setId(54);
        savedTShirt1.setSize("Medium");
        savedTShirt1.setColor("Blue");
        savedTShirt1.setDescription("V-Neck");
        savedTShirt1.setPrice(new BigDecimal("19.99"));
        savedTShirt1.setQuantity(5);

        tShirtsByColor.add(savedTShirt1);
        tShirtsBySize.add(savedTShirt1);
        allTtShirts.add(savedTShirt1);

        TShirt newTShirt2 = new TShirt();
        newTShirt2.setSize("Large");
        newTShirt2.setColor("Blue");
        newTShirt2.setDescription("long sleeve");
        newTShirt2.setPrice(new BigDecimal("30.99"));
        newTShirt2.setQuantity(8);

        TShirt savedTShirt2 = new TShirt();
        savedTShirt2.setId(60);
        savedTShirt2.setSize("Large");
        savedTShirt2.setColor("Blue");
        savedTShirt2.setDescription("long sleeve");
        savedTShirt2.setPrice(new BigDecimal("30.99"));
        savedTShirt2.setQuantity(8);

        allTtShirts.add(savedTShirt2);
        tShirtsByColor.add(savedTShirt2);

        TShirt newTShirt3 = new TShirt();
        newTShirt3.setSize("Medium");
        newTShirt3.setColor("orange");
        newTShirt3.setDescription("sleeveless");
        newTShirt3.setPrice(new BigDecimal("15.99"));
        newTShirt3.setQuantity(3);

        TShirt savedTShirt3 = new TShirt();
        savedTShirt3.setId(72);
        savedTShirt3.setSize("Medium");
        savedTShirt3.setColor("orange");
        savedTShirt3.setDescription("sleeveless");
        savedTShirt3.setPrice(new BigDecimal("15.99"));
        savedTShirt3.setQuantity(3);

        allTtShirts.add(savedTShirt3);
        tShirtsBySize.add(savedTShirt3);

        TShirt newTShirt4 = new TShirt();
        newTShirt4.setSize("Small");
        newTShirt4.setColor("Red");
        newTShirt4.setDescription("sleeveless");
        newTShirt4.setPrice(new BigDecimal("400"));
        newTShirt4.setQuantity(30);

        TShirt savedTShirt4 = new TShirt();
        savedTShirt4.setId(99);
        savedTShirt4.setSize("Small");
        savedTShirt4.setColor("Red");
        savedTShirt4.setDescription("sleeveless");
        savedTShirt4.setPrice(new BigDecimal("400"));
        savedTShirt4.setQuantity(30);

        doReturn(savedTShirt1).when(tShirtRepository).save(newTShirt1);
        doReturn(savedTShirt2).when(tShirtRepository).save(newTShirt2);
        doReturn(savedTShirt3).when(tShirtRepository).save(newTShirt3);
        doReturn(Optional.of(savedTShirt3)).when(tShirtRepository).findById(72L);
        doReturn(Optional.of(savedTShirt1)).when(tShirtRepository).findById(54L);
        doReturn(Optional.of(savedTShirt4)).when(tShirtRepository).findById(99L);

        doReturn(tShirtsByColor).when(tShirtRepository).findAllByColor("Blue");
        doReturn(tShirtsBySize).when(tShirtRepository).findAllBySize("Medium");
        doReturn(allTtShirts).when(tShirtRepository).findAll();

    }

}