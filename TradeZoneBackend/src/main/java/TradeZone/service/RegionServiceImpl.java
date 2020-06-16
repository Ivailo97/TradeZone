package TradeZone.service;

import TradeZone.data.model.service.RegionServiceModel;
import TradeZone.data.repository.RegionRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;

    private final SeedingService seedingService;

    private final ModelMapper mapper;

    @Override
    public List<RegionServiceModel> getAllRegions() {
        seedingService.seedIfNeeded();
        return regionRepository.findAll()
                .stream()
                .map(x -> mapper.map(x, RegionServiceModel.class))
                .collect(Collectors.toList());
    }
}
