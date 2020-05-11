package TradeZone.config;

import org.modelmapper.ModelMapper;
//import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.web.reactive.function.client.WebClient;

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

//    @Bean
//    @LoadBalanced
//    public WebClient.Builder webClientBuilder() {
//        return WebClient.builder();
//    }

}