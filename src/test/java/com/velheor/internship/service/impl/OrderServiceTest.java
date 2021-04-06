package com.velheor.internship.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.velheor.internship.config.H2JpaConfig;
import com.velheor.internship.models.Order;
import com.velheor.internship.service.api.IOrderService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(classes = {H2JpaConfig.class})
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = {
    "classpath:beforeOrderTest.sql"})
class OrderServiceTest {

    @Autowired
    private IOrderService orderService;

    private Order expected;

    private UUID id;

    @BeforeEach
    void setUp() {
        expected = new Order();
        id = UUID.fromString("377514cc-958b-11eb-a8b3-0242ac130003");
        expected.setId(id);
        expected.setDatePickup(LocalDateTime.of(2021, 1, 3, 11, 30));
        expected.setDateDelivery(LocalDateTime.of(2021, 1, 10, 10, 0));
        expected.setPrice(new BigDecimal(4000));
    }

    @Test
    void findById() {
        Order actual = orderService.findById(id);
        assertEquals(expected, actual);

        UUID notExistsId = UUID.fromString("74a07384-93b8-11eb-a8b3-0242ac130003");
        assertThrows(EntityNotFoundException.class,
            () -> orderService.findById(notExistsId));
    }

    @Test
    void create() {
        Order expected = new Order();
        expected.setDatePickup(LocalDateTime.of(2021, 2, 3, 11, 30));
        expected.setDateDelivery(LocalDateTime.of(2021, 2, 10, 10, 0));
        expected.setPrice(new BigDecimal(1337));
        Order actual = orderService.create(expected);
        assertEquals(expected, actual);
    }

    @Test
    void update() {
        Order expected = orderService.findById(id);
        expected.setDatePickup(LocalDateTime.of(2020, 2, 3, 11, 30));
        expected.setDateDelivery(LocalDateTime.of(2021, 3, 3, 11, 30));
        Order actual = orderService.update(expected);
        assertEquals(expected, actual);
    }

    @Test
    void getAll() {
        Order order = new Order();
        order.setId(UUID.fromString("3a424170.958b.11eb.a8b3.0242ac130003"));
        order.setDateDelivery(LocalDateTime.of(2021, 2, 10, 12, 0));
        order.setDatePickup(LocalDateTime.of(2021, 2, 12, 6, 0));
        order.setPrice(new BigDecimal(5000));

        Order order1 = new Order();
        order.setId(UUID.fromString("3d19295e.958b.11eb.a8b3.0242ac130003"));
        order.setDatePickup(LocalDateTime.of(2021, 3, 5, 15, 0));
        order.setDateDelivery(LocalDateTime.of(2021, 3, 6, 19, 30));
        order.setPrice(new BigDecimal(1400));

        List<Order> expectedOrders = Arrays.asList(expected, order, order1);

        List<Order> actualOrders = orderService.getAll();

        assertEquals(expectedOrders, actualOrders);
    }

    @Test
    void delete() {
        orderService.delete(expected);

        assertThrows(EntityNotFoundException.class, () -> orderService.findById(id));
    }
}