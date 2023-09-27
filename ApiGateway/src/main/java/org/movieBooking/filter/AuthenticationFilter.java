package org.movieBooking.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.movieBooking.entities.ConnValidationResponse;
import org.movieBooking.entities.ExceptionResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.movieBooking.entities.Authorities;

@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {


//    List<String> excludedUrls = new ArrayList<>(List.of("/auth/add", "/auth/login", "/customer/addCustomer", "/actuator"));
    @Autowired
    PropertyConfig propertyConfig;
    List<String> excludedUrls;
    @Autowired private WebClient.Builder webClientBuilder;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Autowired
    private ObjectMapper objectMapper;



    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (excludedUrls == null) {
                excludedUrls = propertyConfig.getPropertyList("api.routes.excluded");
            }
            ServerHttpRequest request = exchange.getRequest();
            log.info("**************************************************************************");
            log.info("Excluded URLS: " + propertyConfig.getPropertyList("api.routes.excluded"));
            log.info("ABC: " + propertyConfig.getProperty("abc"));
            log.info("URL is - " + request.getURI().getPath());
            String bearerToken = request.getHeaders().getFirst("Authorization");
            log.info("Bearer Token: "+ bearerToken);

            if(isSecured.test(request)) {
                log.info("secured");
                var client = webClientBuilder.build();
                return client.get()
                        .uri("http://AUTHENTICATION-SERVER/api/v1/movie-booking/auth/validate")
                        .header("Authorization", bearerToken)
                        .retrieve().bodyToMono(ConnValidationResponse.class)
                        .map(response -> {
                            log.info("Response : " + response);
                            exchange.getRequest().mutate().header("username", response.getUsername());
                            exchange.getRequest().mutate().header("userId", String.valueOf(response.getUserId()));
                            exchange.getRequest().mutate().header("authorities", response.getAuthorities().stream().map(Authorities::getAuthority).reduce("", (a, b) -> a + "," + b));

                            return exchange;
                        }).flatMap(chain::filter).onErrorResume(error -> {
                            log.info("Error : " + error);
                            log.info("Error Happened " + error.getMessage());
                            HttpStatusCode errorCode = null;
                            String errorMsg = "";
                            if (error instanceof WebClientResponseException webClientException) {
                                log.info("Error Code: " + webClientException.getStatusCode());
                                errorCode = webClientException.getStatusCode();
                                errorMsg = webClientException.getStatusText();
                            } else {
                                errorCode = HttpStatus.BAD_GATEWAY;
                                errorMsg = HttpStatus.BAD_GATEWAY.getReasonPhrase();
                            }
//                            AuthorizationFilter.AUTH_FAILED_CODE
                            return onError(exchange, String.valueOf(errorCode.value()) ,errorMsg, "JWT Authentication Failed", (HttpStatus) errorCode);
                        });
            }

            return chain.filter(exchange);
        };
    }

    public Predicate<ServerHttpRequest> isSecured = request -> excludedUrls.stream().noneMatch(uri -> request.getURI().getPath().contains(uri));
    private Mono<Void> onError(ServerWebExchange exchange, String errCode, String err, String errDetails, HttpStatus httpStatus) {
        DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        try {
            response.getHeaders().add("Content-Type", "application/json");
            ExceptionResponseModel data = new ExceptionResponseModel(errCode, err, errDetails, LocalDateTime.now());
            byte[] byteData = objectMapper.writeValueAsBytes(data);
            return response.writeWith(Mono.just(byteData).map(dataBufferFactory::wrap));

        } catch (JsonProcessingException e) {
            e.printStackTrace();

        }
        return response.setComplete();
    }

    @NoArgsConstructor
    public static class Config {


    }
}