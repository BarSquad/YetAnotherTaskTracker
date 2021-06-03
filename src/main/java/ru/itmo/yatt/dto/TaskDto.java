package ru.itmo.yatt.dto;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class TaskDto {
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NotNull
    private Long projectId;
    private Long assigneeId;
    private String status;

}
