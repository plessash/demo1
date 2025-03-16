package com.example.demo.service;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.liberty.employee.model.dto.response.CustomerResponseDto;
import ru.liberty.employee.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final WebClient webClient;

    public CustomerServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public CustomerResponseDto getClients(int page, int size, HttpHeaders headers) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .queryParam("page", page)
                .queryParam("size", size)
                .build())
            .headers(httpHeaders -> httpHeaders.addAll(headers))
            .retrieve()
            .bodyToMono(CustomerResponseDto.class)
            .block();
    }
}
