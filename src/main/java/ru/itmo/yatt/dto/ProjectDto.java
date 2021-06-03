package ru.itmo.yatt.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author Vladimir Goncharov
 * @created 08.05.2021
 */

@Getter
@Setter
public class ProjectDto {
    private String name;
    private String description;
}
