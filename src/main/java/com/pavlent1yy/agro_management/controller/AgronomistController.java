package com.pavlent1yy.agro_management.controller;

import com.pavlent1yy.agro_management.dto.FieldPlanForm;
import com.pavlent1yy.agro_management.service.FieldPlanService;
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
@RequestMapping("/agronomist")
@RequiredArgsConstructor
public class AgronomistController {

    private final FieldPlanService fieldPlanService;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "agronomist/dashboard";
    }

    @GetMapping("/planning")
    public String planning(Model model) {
        model.addAttribute("fieldPlanForm", new FieldPlanForm());
        model.addAttribute("plans", fieldPlanService.findAll());
        return "agronomist/planning";
    }

    @PostMapping("/planning")
    public String savePlan(@Valid @ModelAttribute("fieldPlanForm") FieldPlanForm form,
                           BindingResult bindingResult,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("plans", fieldPlanService.findAll());
            return "agronomist/planning";
        }
        fieldPlanService.save(form);
        redirectAttributes.addFlashAttribute("successMessage", "Посевной план успешно сохранен");
        return "redirect:/agronomist/planning";
    }
}
