package com.agro.controller;

import com.agro.dto.WarehouseOperationForm;
import com.agro.entity.OperationType;
import com.agro.service.WarehouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/storekeeper")
@RequiredArgsConstructor
public class StorekeeperController {

    private final WarehouseService warehouseService;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "storekeeper/dashboard";
    }

    @GetMapping("/warehouse")
    public String warehouse(Model model) {
        model.addAttribute("operationForm", new WarehouseOperationForm());
        model.addAttribute("items", warehouseService.findAllItems());
        model.addAttribute("operations", warehouseService.findAllOperations());
        model.addAttribute("operationTypes", OperationType.values());
        return "storekeeper/warehouse";
    }

    @PostMapping("/warehouse")
    public String saveOperation(@Valid @ModelAttribute("operationForm") WarehouseOperationForm form,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("items", warehouseService.findAllItems());
            model.addAttribute("operations", warehouseService.findAllOperations());
            model.addAttribute("operationTypes", OperationType.values());
            return "storekeeper/warehouse";
        }

        try {
            warehouseService.saveOperation(form);
            redirectAttributes.addFlashAttribute("successMessage", "Складская операция успешно проведена");
        } catch (IllegalArgumentException e) {
            model.addAttribute("items", warehouseService.findAllItems());
            model.addAttribute("operations", warehouseService.findAllOperations());
            model.addAttribute("operationTypes", OperationType.values());
            model.addAttribute("errorMessage", e.getMessage());
            return "storekeeper/warehouse";
        }
        return "redirect:/storekeeper/warehouse";
    }
}
