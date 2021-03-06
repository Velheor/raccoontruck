package com.velheor.internship.repository;

import com.velheor.internship.models.Load;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface LoadRepository extends CrudRepository<Load, UUID> {

}
