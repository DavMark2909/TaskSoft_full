package com.tasksoft.mark.mainservice.repository;

import com.tasksoft.mark.mainservice.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
