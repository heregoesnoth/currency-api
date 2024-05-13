package br.com.ada.currencyapi.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.ada.currencyapi.domain.ConvertCurrencyRequest;
import br.com.ada.currencyapi.domain.ConvertCurrencyResponse;
import br.com.ada.currencyapi.domain.Currency;
import br.com.ada.currencyapi.domain.CurrencyRequest;
import br.com.ada.currencyapi.domain.CurrencyResponse;
import br.com.ada.currencyapi.exception.CoinNotFoundException;
import br.com.ada.currencyapi.exception.CurrencyException;
import br.com.ada.currencyapi.repository.CurrencyRepository;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceUnitTest {

    @InjectMocks
    private CurrencyService currencyService;

    @Mock
    private CurrencyRepository currencyRepository;

    @Test
    void testGetCurrencies() {
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

        when(currencyRepository.findAll()).thenReturn(list);

        List<CurrencyResponse> responses = currencyService.get();
        Assertions.assertNotNull(responses);
        Assertions.assertEquals(2, responses.size());
        Assertions.assertEquals("1 - LCS", responses.get(0).getLabel());
        Assertions.assertEquals("2 - YAS", responses.get(1).getLabel());

    }

    @Test
    void testCreateCurrency() {
        Mockito.when(currencyRepository.findByName(anyString())).thenReturn(null);
        Mockito.when(currencyRepository.save(any(Currency.class))).thenReturn(Currency.builder().id(3L).build());

        Long id = currencyService.create(CurrencyRequest.builder().name("name").build());
        Assertions.assertNotNull(id);

    }

    @Test
    void testCreateCurrencyThrowsCurrencyException() {
        Mockito.when(currencyRepository.findByName(any())).thenReturn(Currency.builder().build());

        CurrencyException exception = Assertions.assertThrows(CurrencyException.class, () -> currencyService.create(CurrencyRequest.builder().build()));

        Assertions.assertEquals("Coin already exists", exception.getMessage());

    }

    @Test
    void testDeleteCurrency() {
        doNothing().when(currencyRepository).deleteById(anyLong());
        currencyService.delete(1L);
        verify(currencyRepository, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(currencyRepository);
    }

    @Test
    void testConvertCurrency() {
        Mockito.when(currencyRepository.findByName(any())).thenReturn(
                Currency.builder()
                        .exchanges(Map.of("EUR", new BigDecimal("2")))
                        .build()
        );

        ConvertCurrencyRequest request = ConvertCurrencyRequest
                .builder()
                .to("EUR")
                .amount(BigDecimal.TEN)
                .build();

        ConvertCurrencyResponse response = currencyService.convert(request);
        Assertions.assertEquals(new BigDecimal("20"), response.getAmount());

    }

    @Test
    void textConvertCurrencyThrowsCoinNotFoundException() {
        Mockito.when(currencyRepository.findByName(any())).thenReturn(null);
        ConvertCurrencyRequest request = ConvertCurrencyRequest
                .builder()
                .from("USD")
                .to("EUR")
                .amount(BigDecimal.TEN)
                .build();

        CoinNotFoundException exception = Assertions.assertThrows(CoinNotFoundException.class, () -> currencyService.convert(request));

        Assertions.assertEquals("Coin not found: USD", exception.getMessage());

    }

    @Test
    void textConvertCurrencyThrowsCoinNotFoundExceptionForExchange() {
        Mockito.when(currencyRepository.findByName(any())).thenReturn(Currency.builder()
                .exchanges(Map.of("BRL", new BigDecimal("2")))
                .build());
        ConvertCurrencyRequest request = ConvertCurrencyRequest
                .builder()
                .from("USD")
                .to("EUR")
                .amount(BigDecimal.TEN)
                .build();

        CoinNotFoundException exception = Assertions.assertThrows(CoinNotFoundException.class, () -> currencyService.convert(request));

        Assertions.assertEquals("Exchange EUR not found for USD", exception.getMessage());

    }
}