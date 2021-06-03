package ru.itmo.yatt.dto;

import lombok.Data;

/**
 * @author Vladimir Goncharov
 * @created 08.05.2021
 */

@Data
public class SignUpRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
