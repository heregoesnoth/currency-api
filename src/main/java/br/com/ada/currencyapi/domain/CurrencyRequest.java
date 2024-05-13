package br.com.ada.currencyapi.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyRequest implements Serializable {

    @NotBlank
    private String name;
    private String description;
    private HashMap<String, BigDecimal> exchanges;

}