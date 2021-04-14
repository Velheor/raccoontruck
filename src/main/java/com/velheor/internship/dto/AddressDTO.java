package com.velheor.internship.dto;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDTO extends BaseDTO {

    @NotNull(message = "{country.notEmpty}")
    private String country;

    @NotNull(message = "{city.notEmpty}")
    private String city;

    @NotNull(message = "{streetName.notEmpty}")
    private String streetName;

    @NotNull(message = "{streetNumber.notEmpty}")
    private String streetNumber;
}
