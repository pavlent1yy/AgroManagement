package com.pavlent1yy.agro_management.controller;

import com.pavlent1yy.agro_management.dto.WorkRecordForm;
import com.pavlent1yy.agro_management.service.WorkRecordService;
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
@RequestMapping("/mechanic")
@RequiredArgsConstructor
public class MechanicController {

    private final WorkRecordService workRecordService;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "mechanic/dashboard";
    }

    @GetMapping("/work")
    public String work(Model model) {
        model.addAttribute("workRecordForm", new WorkRecordForm());
        model.addAttribute("equipment", workRecordService.findAllEquipment());
        model.addAttribute("workRecords", workRecordService.findAll());
        return "mechanic/work";
    }

    @PostMapping("/work")
    public String saveWorkRecord(@Valid @ModelAttribute("workRecordForm") WorkRecordForm form,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("equipment", workRecordService.findAllEquipment());
            model.addAttribute("workRecords", workRecordService.findAll());
            return "mechanic/work";
        }

        try {
            workRecordService.save(form);
            redirectAttributes.addFlashAttribute("successMessage", "Запись о работе техники сохранена");
        } catch (IllegalArgumentException e) {
            model.addAttribute("equipment", workRecordService.findAllEquipment());
            model.addAttribute("workRecords", workRecordService.findAll());
            model.addAttribute("errorMessage", e.getMessage());
            return "mechanic/work";
        }
        return "redirect:/mechanic/work";
    }
}
