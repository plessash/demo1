package com.example.demo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;
import ru.liberty.employee.model.dto.response.CustomerResponseDto;

import java.net.URI;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomerServiceImplTest {

    @Mock
    private WebClient webClient;

    @Mock
    private RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetClients() {

        int page = 1;
        int size = 5;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer token");

        CustomerResponseDto expectedResponse = new CustomerResponseDto(1, 2, 100, List.of());

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.headers(any(Consumer.class))).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CustomerResponseDto.class)).thenReturn(Mono.just(expectedResponse));

        CustomerResponseDto actualResponse = customerService.getClients(page, size, headers);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);

        verify(webClient, times(1)).get();
        verify(requestHeadersUriSpec, times(1)).uri(any(Function.class));
        verify(requestHeadersUriSpec, times(1)).headers(any(Consumer.class));
        verify(requestHeadersUriSpec, times(1)).retrieve();
        verify(responseSpec, times(1)).bodyToMono(CustomerResponseDto.class);

        ArgumentCaptor<Function<UriBuilder, URI>> uriFunctionCaptor = ArgumentCaptor.forClass(Function.class);
        verify(requestHeadersUriSpec).uri(uriFunctionCaptor.capture());

        UriBuilder uriBuilder = mock(UriBuilder.class);
        when(uriBuilder.queryParam(anyString(), any(Object.class))).thenReturn(uriBuilder);

        uriFunctionCaptor.getValue().apply(uriBuilder);

        verify(uriBuilder).queryParam("page", page);
        verify(uriBuilder).queryParam("size", size);
        verify(uriBuilder).build();
    }
}