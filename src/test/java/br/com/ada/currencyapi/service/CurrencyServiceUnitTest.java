package br.com.ada.currencyapi.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.ada.currencyapi.domain.Currency;
import br.com.ada.currencyapi.domain.CurrencyResponse;
import br.com.ada.currencyapi.repository.CurrencyRepository;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceUnitTest {

    @InjectMocks
    private CurrencyService currencyService;

    @Mock
    private CurrencyRepository currencyRepository;

    @Test
    void testGet() {
        List<Currency> list = new ArrayList<>();
        list.add(Currency.builder()
                .id(1L)
                .name("LCS")
                .description("Moeda do lucas")
                .build());
        list.add(Currency.builder()
                .id(2L)
                .name("YAS")
                .description("Moeda da yasmin")
                .build());

        Mockito.when(currencyRepository.findAll()).thenReturn(list);

        List<CurrencyResponse> responses = currencyService.get();
        Assertions.assertNotNull(responses);
        Assertions.assertEquals(2, responses.size());
        Assertions.assertEquals("1 - LCS", responses.get(0).getLabel());
        Assertions.assertEquals("2 - YAS", responses.get(1).getLabel());


    }
}