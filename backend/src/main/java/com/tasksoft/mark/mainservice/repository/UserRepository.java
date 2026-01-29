package com.tasksoft.mark.mainservice.repository;

import com.tasksoft.mark.mainservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
