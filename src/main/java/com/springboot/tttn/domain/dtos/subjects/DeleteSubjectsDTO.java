package com.springbot.tttn.domain.dtos.subjects;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Getter
@NoArgsConstructor
public class DeleteSubjectsDTO {
    @Size(min = 1, message = "classIds at least 1 element")
    private List<Long> subjectIds;

    public List<Long> toSubjectIds () {
        return subjectIds;
    }
}
