package TradeZone.config;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
//import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class BeanConfig {

    private static ModelMapper mapper;

    private static Gson gson;

    static {
        gson = new Gson();
        mapper = new ModelMapper();
        mapper.getConfiguration().setAmbiguityIgnored(true);
    }

    @Bean
    public ModelMapper modelMapper() {
        return mapper;
    }

    @Bean
    public Gson gson() {
        return gson;
    }

//    @Bean
//    @LoadBalanced
//    public WebClient.Builder webClientBuilder() {
//        return WebClient.builder();
//    }

}