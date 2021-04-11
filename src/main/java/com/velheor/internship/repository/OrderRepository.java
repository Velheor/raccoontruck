package com.velheor.internship.repository;

import com.velheor.internship.models.Order;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, UUID> {

}
