package com.springbot.tttn.domain.payloads.courses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CourseInfoResponse {

   Long courseId;
   String subjectName;
   Long subjectId;
   String className;
   Long classId; 
}
