package com.velheor.internship.controllers;

import com.velheor.internship.dto.LoadViewDto;
import com.velheor.internship.mappers.LoadMapper;
import com.velheor.internship.service.LoadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/loads")
@RequiredArgsConstructor
public class LoadController {

    private final LoadService loadService;
    private final LoadMapper loadMapper;

    @GetMapping("/{id}")
    public LoadViewDto findById(@PathVariable("id") UUID id) {
        return loadMapper.toLoadDto(loadService.findById(id));
    }

    @PutMapping
    public LoadViewDto update(@Valid @RequestBody LoadViewDto loadViewDTO) {
        return loadMapper.toLoadDto(loadService.save(loadMapper.toLoad(loadViewDTO)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoadViewDto save(@Valid @RequestBody LoadViewDto loadViewDTO) {
        return loadMapper.toLoadDto(loadService.save(loadMapper.toLoad(loadViewDTO)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("id") UUID id) {
        loadService.deleteById(id);
    }
}
