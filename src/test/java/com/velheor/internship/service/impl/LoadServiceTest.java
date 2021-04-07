package com.velheor.internship.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.velheor.internship.config.H2JpaConfig;
import com.velheor.internship.models.Load;
import com.velheor.internship.service.api.ILoadService;
import com.velheor.internship.service.api.IOrderService;
import java.math.BigDecimal;
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
    "classpath:beforeTest.sql"})
class LoadServiceTest {

    @Autowired
    private ILoadService loadService;

    @Autowired
    private IOrderService orderService;
    private Load expected;

    private UUID id;

    @BeforeEach
    void setUp() {
        expected = new Load();
        id = UUID.fromString("5942070a-957b-11eb-a8b3-0242ac130003");
        expected.setId(id);
        expected.setName("FURNITURE");
        expected.setWeight(new BigDecimal("0.5"));
        expected.setDetails("Just furniture");
        expected.setOrder(
            orderService.findById(UUID.fromString("377514cc-958b-11eb-a8b3-0242ac130003")));
    }

    @Test
    void findByIdReturnsLoad() {
        Load actual = loadService.findById(id);
        assertEquals(expected, actual);
    }

    @Test
    void findByThrowsEntityNotFoundException() {
        UUID notExistsId = UUID.fromString("12345678-958b-11eb-a8b3-0242ac130003");
        assertThrows(EntityNotFoundException.class, () -> loadService.findById(notExistsId));
    }

    @Test
    void create() {
        Load expected = new Load();
        expected.setName("CEMENT");
        expected.setWeight(new BigDecimal("1.5"));
        expected.setOrder(orderService.findById(UUID.fromString("3a424170-958b-11eb-a8b3-0242ac130003")));
        expected.setDetails("FOR BUILDING");
        Load actual = loadService.create(expected);

        assertEquals(expected, actual);
    }

    @Test
    void update() {
        expected.setName("VEGETABLES");

        Load actual = loadService.update(expected);

        assertEquals(expected, actual);
    }

    @Test
    void getAll() {
        Load load = new Load();
        load.setId(UUID.fromString("60b523b4-957b-11eb-a8b3-0242ac130003"));
        load.setName("BEER");
        load.setWeight(new BigDecimal(23));
        load.setDetails("HEINEKEN");
        load.setOrder(
            orderService.findById(UUID.fromString("3a424170-958b-11eb-a8b3-0242ac130003")));

        List<Load> expectedAll = List.of(expected, load);
        List<Load> actualAll = loadService.getAll();

        assertEquals(expectedAll, actualAll);
    }

    @Test
    void delete() {
        int expectedCount = loadService.getAll().size() - 1;
        loadService.delete(expected);
        int actualCount = loadService.getAll().size();

        assertThrows(EntityNotFoundException.class, () -> loadService.findById(id));
        assertEquals(expectedCount, actualCount);
    }
}