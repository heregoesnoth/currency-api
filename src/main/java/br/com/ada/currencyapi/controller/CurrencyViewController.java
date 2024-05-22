package br.com.ada.currencyapi.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.ada.currencyapi.domain.CurrencyRequest;
import br.com.ada.currencyapi.domain.CurrencyResponse;
import br.com.ada.currencyapi.service.CurrencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CurrencyViewController {

    private final CurrencyService currencyService;

    @GetMapping("/new-currency")
    public String newCurrency(Model model) {
        model.addAttribute("currencyRequest", new CurrencyRequest());
        return "new-currency";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute @Valid CurrencyRequest currencyRequest, RedirectAttributes redirectAttributes) {
        Long id = this.currencyService.create(currencyRequest);
        redirectAttributes.addFlashAttribute("successMessage", "Criado com sucesso!");
        return "redirect:/currencies";
    }

    @GetMapping("/currencies")
    public String getCurrencies(Model model) {
        List<CurrencyResponse> currencies = currencyService.get();
        model.addAttribute("currencies", currencies);
        return "list-currencies";
    }
}
