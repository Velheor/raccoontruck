package com.velheor.internship.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StatusDTO extends BaseDTO {

    @NotNull(message = "notEmpty")
    private String name;

    @NotNull(message = "notEmpty")
    private String statusDate;
}
