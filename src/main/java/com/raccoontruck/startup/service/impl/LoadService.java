package com.raccoontruck.startup.service.impl;

import com.raccoontruck.startup.dto.LoadDTO;
import com.raccoontruck.startup.models.Load;
import com.raccoontruck.startup.repository.api.LoadRepository;
import com.raccoontruck.startup.service.api.ILoadService;
import com.raccoontruck.startup.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoadService implements ILoadService {

    private final LoadRepository loadRepository;

    @Autowired
    LoadService(LoadRepository loadRepository) {
        this.loadRepository = loadRepository;
    }

    @Override
    public List<LoadDTO> findAll() {
        return convertToDTO(loadRepository.findAll());
    }

    @Override
    public LoadDTO findById(Long id) {
        if (!loadRepository.findById(id).isPresent()) {
            return null;
        }
        return convertToDTO(loadRepository.findById(id).orElse(null));
    }

    @Override
    public LoadDTO update(LoadDTO loadDTO) {
        return convertToDTO(loadRepository.save(convertFromDTO(loadDTO)));
    }

    @Override
    public void delete(Long id) {
        loadRepository.delete(convertFromDTO(findById(id)));
    }

    @Override
    public LoadDTO convertToDTO(Load load) {
        return ObjectMapperUtils.map(load, LoadDTO.class);
    }

    @Override
    public Load convertFromDTO(LoadDTO loadDTO) {
        return ObjectMapperUtils.map(loadDTO, Load.class);
    }

    @Override
    public List<LoadDTO> convertToDTO(List<Load> loads) {
        return ObjectMapperUtils.mapAll(loads, LoadDTO.class);
    }

    @Override
    public List<Load> convertFromDTO(List<LoadDTO> loadDTOS) {
        return ObjectMapperUtils.mapAll(loadDTOS, Load.class);
    }
}