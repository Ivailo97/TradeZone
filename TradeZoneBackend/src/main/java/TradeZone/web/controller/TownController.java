package TradeZone.web.controller;

import TradeZone.data.model.service.TownServiceModel;
import TradeZone.data.model.view.TownCompleteProfileViewModel;
import TradeZone.data.model.view.TownViewModel;
import TradeZone.service.TownService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping("api/towns")
@AllArgsConstructor
public class TownController {

    private final TownService townService;

    private final ModelMapper mapper;

    @GetMapping("/all")
    public ResponseEntity<List<TownCompleteProfileViewModel>> all() {
        return ResponseEntity.ok(townService.getAllTowns().stream()
                .map(x -> mapper.map(x, TownCompleteProfileViewModel.class))
                .collect(Collectors.toList()));
    }

    @GetMapping("/region")
    public ResponseEntity<List<TownViewModel>> inRegion(@RequestParam String regionName) {
        List<TownServiceModel> serviceModels = townService.getAllInRegion(regionName);
        List<TownViewModel> viewModels = serviceModels.stream()
                .map(x -> {
                    TownViewModel viewModel = mapper.map(x, TownViewModel.class);
                    viewModel.setAdvertisementsCount(x.getCitizen().stream().map(c -> c.getCreatedAdvertisements().size()).mapToInt(Integer::intValue).sum());
                    return viewModel;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(viewModels);
    }
}
