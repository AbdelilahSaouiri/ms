package net.ensah.gateway;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

//    @Bean
//    DiscoveryClientRouteDefinitionLocator discoveryClientRouteDefinitionLocator(
//            ReactiveDiscoveryClient rdc,
//            DiscoveryLocatorProperties dlp) {
//
//        return new DiscoveryClientRouteDefinitionLocator(rdc, dlp);
//    }


}
