package br.com.ada.currencyapi.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ada.currencyapi.domain.CurrencyRequest;
import br.com.ada.currencyapi.domain.CurrencyResponse;
import br.com.ada.currencyapi.service.CurrencyService;
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class CurrencyControllerUnitTest {

    @Mock
    private CurrencyService currencyService;

    @InjectMocks
    private CurrencyController currencyController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(currencyController).build();
    }

    @Test
    void testGetCurrencies() throws Exception {
        Mockito.when(currencyService.get()).thenReturn(List.of(CurrencyResponse.builder()
                        .label("1 - USD")
                .build()));

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/currency")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].label").value("1 - USD"))
                .andDo(MockMvcResultHandlers.print());

    }


    @Test
    void testCreateCurrencyReturns200() throws Exception {
        Mockito.when(currencyService.create(Mockito.any(CurrencyRequest.class))).thenReturn(5L);
        CurrencyRequest request = CurrencyRequest.builder()
                .name("USD")
                .build();
        var content = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/currency")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$").value(5L))
                .andDo(MockMvcResultHandlers.print());

    }



    @Test
    void testCreateCurrencyReturns400() throws Exception {
        CurrencyRequest request = CurrencyRequest.builder()
                .description("Dollars")
                .build();

        var content = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/currency")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());

    }



}
