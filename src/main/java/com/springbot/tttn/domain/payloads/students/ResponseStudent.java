package com.springbot.tttn.domain.payloads.students;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ResponseStudent {
    String mssv;
    String name;
    String address;
    String className;
    Long classId;
    Long schoolYear;
}
