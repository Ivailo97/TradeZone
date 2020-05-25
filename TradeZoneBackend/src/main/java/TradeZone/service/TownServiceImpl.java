package TradeZone.service;

import TradeZone.data.model.entity.Town;
import TradeZone.data.model.service.TownServiceModel;
import TradeZone.data.repository.TownRepository;
import TradeZone.utils.FileUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TownServiceImpl implements TownService {

    private static final String TOWNS_JSON_PATH = "C:\\Users\\HP\\Desktop\\TradeZoneBackend\\src\\main\\resources\\static\\json\\towns";

    private static final Type CITY_LIST_TYPE = new TypeToken<List<Town>>() {}.getType();

    private final TownRepository townRepository;

    private final FileUtil fileUtil;

    private final Gson gson;

    private final ModelMapper mapper;

    private void seedTowns() {

        String townsAsString = fileUtil.fileContent(TOWNS_JSON_PATH);

        List<Town> towns = gson.fromJson(townsAsString, CITY_LIST_TYPE);

        townRepository.saveAll(towns);
    }

    @Override
    public List<TownServiceModel> getAll() {

        if (townRepository.count() == 0) {
            seedTowns();
        }

        return townRepository.findAll()
                .stream()
                .map(x -> mapper.map(x, TownServiceModel.class))
                .collect(Collectors.toList());
    }
}
