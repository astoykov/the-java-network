package com.alesto.javanetwork.repository;

import com.alesto.javanetwork.domain.entity.Follows;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowsRepository extends JpaRepository<Follows, String> {

    public List<Follows> findAllByUsername(String username);

}
