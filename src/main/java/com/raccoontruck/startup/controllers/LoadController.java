package com.raccoontruck.startup.controllers;

import com.raccoontruck.startup.dto.LoadDTO;
import com.raccoontruck.startup.service.api.ILoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoadController {
    private final ILoadService loadService;

    @Autowired
    LoadController(ILoadService loadService) {
        this.loadService = loadService;
    }

    @GetMapping("/loads")
    public List<LoadDTO> findAll() {
        return loadService.findAll();
    }
}
