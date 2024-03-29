package com.spring.boot.application.repositories;

import com.spring.boot.application.controller.model.response.skill.UserSkillResponse;
import com.spring.boot.application.entity.Skill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, String> {
    Skill getById(String id);
    @Query(value = " SELECT s FROM Skill s WHERE s.skillName LIKE %?1%")
    Page<Skill> getPageSkill(String name, Pageable pageable);
    @Query(value = " SELECT s FROM Skill s ORDER BY s.skillName ASC")
    Page<Skill> getPageSkill(Pageable pageable);
}
