package TradeZone.service;

import TradeZone.data.model.service.TownServiceModel;
import TradeZone.data.repository.TownRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TownServiceImpl implements TownService {

    private final TownRepository townRepository;

    private final SeedingService seedingService;

    private final ModelMapper mapper;

    @Override
    public List<TownServiceModel> getAllTowns() {
        seedingService.seedIfNeeded();
        return townRepository.findAll()
                .stream()
                .map(x -> mapper.map(x, TownServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<TownServiceModel> getAllInRegion(String regionName) {
        seedingService.seedIfNeeded();
        return townRepository.findAllByRegionName(regionName).stream()
                .map(x -> mapper.map(x, TownServiceModel.class))
                .collect(Collectors.toList());
    }
}
