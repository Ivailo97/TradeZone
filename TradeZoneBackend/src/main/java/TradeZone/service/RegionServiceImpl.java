package TradeZone.service;

import TradeZone.data.model.entity.Region;
import TradeZone.data.model.entity.Town;
import TradeZone.data.model.rest.TownBindingModel;
import TradeZone.data.model.service.RegionServiceModel;
import TradeZone.data.model.service.TownServiceModel;
import TradeZone.data.repository.RegionRepository;
import TradeZone.data.repository.TownRepository;
import TradeZone.utils.FileUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RegionServiceImpl implements RegionService {

    private static final String TOWNS_JSON_PATH = "C:\\Users\\HP\\Desktop\\TradeZoneBackend\\src\\main\\resources\\static\\json\\towns";

    private static final String REGIONS_JSON_PATH = "C:\\Users\\HP\\Desktop\\TradeZoneBackend\\src\\main\\resources\\static\\json\\regions.json";

    private static final Type CITY_LIST_TYPE = new TypeToken<List<TownBindingModel>>() {}.getType();

    private static final Type REGION_LIST_TYPE = new TypeToken<List<Region>>() {}.getType();

    private final TownRepository townRepository;

    private final RegionRepository regionRepository;

    private final FileUtil fileUtil;

    private final Gson gson;

    private final ModelMapper mapper;

    private void seedTowns() {

        String townsAsString = fileUtil.fileContent(TOWNS_JSON_PATH);

        List<TownBindingModel> towns = gson.fromJson(townsAsString, CITY_LIST_TYPE);

        List<Town> entities = new ArrayList<>();

        for (TownBindingModel town : towns) {
            Town entity = mapper.map(town, Town.class);
            Region region = regionRepository.findByName(town.getRegion()).orElse(null);
            entity.setRegion(region);
            entities.add(entity);
        }

        townRepository.saveAll(entities);
    }

    @Override
    public List<TownServiceModel> getAllTowns() {
        seedIfNeeded();
        return townRepository.findAll()
                .stream()
                .map(x -> mapper.map(x, TownServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<RegionServiceModel> getAllRegions() {
        seedIfNeeded();
        return regionRepository.findAll()
                .stream()
                .map(x -> mapper.map(x, RegionServiceModel.class))
                .collect(Collectors.toList());
    }

    private void seedIfNeeded() {
        if (regionRepository.count() == 0) {
            seedRegions();
        }
        if (townRepository.count() == 0) {
            seedTowns();
        }
    }

    private void seedRegions() {
        String regionsAsString = fileUtil.fileContent(REGIONS_JSON_PATH);
        List<Region> regions = gson.fromJson(regionsAsString, REGION_LIST_TYPE);
        regionRepository.saveAll(regions);
    }
}
