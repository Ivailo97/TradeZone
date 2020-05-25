package TradeZone.web.controller;

import TradeZone.data.model.view.TownViewModel;
import TradeZone.service.TownService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<List<TownViewModel>> allRegions() {
        return ResponseEntity.ok(townService.getAll().stream()
                .map(x -> mapper.map(x, TownViewModel.class))
                .collect(Collectors.toList()));
    }
}
