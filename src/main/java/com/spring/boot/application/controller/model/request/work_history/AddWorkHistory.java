package com.spring.boot.application.controller.model.request.work_history;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class AddWorkHistory {
    private String companyName;
    private Date fromDate;
    private Date toDate;
    private boolean isCurrent;
    private String position;
    private String description;
}
