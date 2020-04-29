package TradeZone.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    private static ModelMapper mapper;

    static {
        mapper = new ModelMapper();
        mapper.getConfiguration().setAmbiguityIgnored(true);
    }

    @Bean
    public ModelMapper modelMapper() {
        return mapper;
    }
}