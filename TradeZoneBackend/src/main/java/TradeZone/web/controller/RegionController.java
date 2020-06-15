package TradeZone.web.controller;

import TradeZone.data.model.view.RegionViewModel;
import TradeZone.service.MappingService;
import TradeZone.service.RegionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@AllArgsConstructor
@RequestMapping("/api/region")
public class RegionController {

    private final RegionService regionService;

    private final MappingService mappingService;

    @GetMapping("/all")
    public ResponseEntity<List<RegionViewModel>> all() {
        return ResponseEntity.ok(mappingService.mapServiceRegionToView(regionService.getAllRegions()));
    }
}
