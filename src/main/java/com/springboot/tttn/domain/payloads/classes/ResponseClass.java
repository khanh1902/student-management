package com.springbot.tttn.domain.payloads.classes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ResponseClass {
    Long classId;
    String className;
    Long schoolYear;
    Long totalStudent;
}
