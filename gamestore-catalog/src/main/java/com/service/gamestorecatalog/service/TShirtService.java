package com.service.gamestorecatalog.service;

import com.service.gamestorecatalog.model.TShirt;
import com.service.gamestorecatalog.repository.TShirtRepository;
import com.service.gamestorecatalog.viewModel.TShirtViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Component
public class TShirtService {

    @Autowired
    TShirtRepository tShirtRepo;

    public TShirtService(TShirtRepository tShirtRepo) {
        this.tShirtRepo = tShirtRepo;
    }

    //TSHIRT SERVICE LAYER

    public TShirtViewModel createTShirt(TShirtViewModel tShirtViewModel) {

        // Remember model view has already been validated through JSR 303
        // Validate incoming TShirt Data in the view model
        if (tShirtViewModel == null) throw new IllegalArgumentException("No TShirt is passed! TShirt object is null!");

        TShirt tShirt = new TShirt();
        tShirt.setSize(tShirtViewModel.getSize());
        tShirt.setColor(tShirtViewModel.getColor());
        tShirt.setDescription(tShirtViewModel.getDescription());
        tShirt.setPrice(tShirtViewModel.getPrice());
        tShirt.setQuantity(tShirtViewModel.getQuantity());

        tShirt = tShirtRepo.save(tShirt);

        return buildTShirtViewModel(tShirt);
    }

    public TShirtViewModel getTShirt(long id) {
        Optional<TShirt> tShirt = tShirtRepo.findById(id);
        if (tShirt == null)
            return null;
        else
            return buildTShirtViewModel(tShirt.get());
    }

    public void updateTShirt(TShirtViewModel tShirtViewModel) {

        // Remember model view has already been validated through JSR 303
        // Validate incoming TShirt Data in the view model
        if (tShirtViewModel == null) throw new IllegalArgumentException("No TShirt is passed! TShirt object is null!");

        //make sure the Console exists. and if not, throw exception...
        if (this.getTShirt(tShirtViewModel.getId()) == null)
            throw new IllegalArgumentException("No such TShirt to update.");

        TShirt tShirt = new TShirt();
        tShirt.setId(tShirtViewModel.getId());
        tShirt.setSize(tShirtViewModel.getSize());
        tShirt.setColor(tShirtViewModel.getColor());
        tShirt.setDescription(tShirtViewModel.getDescription());
        tShirt.setPrice(tShirtViewModel.getPrice());
        tShirt.setQuantity(tShirtViewModel.getQuantity());

        tShirtRepo.save(tShirt);
    }

    public void deleteTShirt(long id) {

        tShirtRepo.deleteById(id);
    }

    public List<TShirtViewModel> getTShirtByColor(String color) {
        List<TShirt> tShirtList = tShirtRepo.findAllByColor(color);
        List<TShirtViewModel> tvmList = new ArrayList<>();

        if (tShirtList == null)
            return null;
        else
            tShirtList.stream().forEach(t -> tvmList.add(buildTShirtViewModel(t)));
        return tvmList;
    }

    public List<TShirtViewModel> getTShirtBySize(String size) {
        List<TShirt> tShirtList = tShirtRepo.findAllBySize(size);
        List<TShirtViewModel> tvmList = new ArrayList<>();

        if (tShirtList == null)
            return null;
        else
            tShirtList.stream().forEach(t -> tvmList.add(buildTShirtViewModel(t)));
        return tvmList;
    }

    public List<TShirtViewModel> getAllTShirts() {
        List<TShirt> tShirtList = tShirtRepo.findAll();
        List<TShirtViewModel> tvmList = new ArrayList<>();

        if (tShirtList == null)
            return null;
        else
            tShirtList.stream().forEach(t -> tvmList.add(buildTShirtViewModel(t)));
        return tvmList;
    }


    //Helper Methods...

    public TShirtViewModel buildTShirtViewModel(TShirt tShirt) {
        TShirtViewModel tShirtViewModel = new TShirtViewModel();
        tShirtViewModel.setId(tShirt.getId());
        tShirtViewModel.setSize(tShirt.getSize());
        tShirtViewModel.setColor(tShirt.getColor());
        tShirtViewModel.setDescription(tShirt.getDescription());
        tShirtViewModel.setPrice(tShirt.getPrice());
        tShirtViewModel.setQuantity(tShirt.getQuantity());

        return tShirtViewModel;
    }
}
