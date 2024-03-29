package com.spring.boot.application.controller.model.response.job;

import com.spring.boot.application.common.enums.Status;
import com.spring.boot.application.entity.Language;
import com.spring.boot.application.entity.LanguageJob;
import com.spring.boot.application.entity.Skill;
import com.spring.boot.application.entity.SkillJob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LangJobResponse {
    private String id;
    private String langId;
    private String name;
    private Status status;
    public LangJobResponse(LanguageJob lj, Language l) {
        this.id = lj.getId();
        this.langId = l.getId();
        this.name = l.getLanguage();
        this.status = lj.getStatus();
    }
}
