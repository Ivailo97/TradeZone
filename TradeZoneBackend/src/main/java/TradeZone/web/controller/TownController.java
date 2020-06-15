package TradeZone.web.controller;

import TradeZone.data.model.view.TownCompleteProfileViewModel;
import TradeZone.service.RegionService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping("api/towns")
@AllArgsConstructor
public class TownController {

    private final RegionService townService;

    private final ModelMapper mapper;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TownCompleteProfileViewModel>> allRegions() {
        return ResponseEntity.ok(townService.getAllTowns().stream()
                .map(x -> mapper.map(x, TownCompleteProfileViewModel.class))
                .collect(Collectors.toList()));
    }

}
