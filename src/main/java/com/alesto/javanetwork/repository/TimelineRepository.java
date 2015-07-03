package com.alesto.javanetwork.repository;

import com.alesto.javanetwork.domain.entity.Timeline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimelineRepository extends JpaRepository<Timeline, String> {

    public List<Timeline> findAllByUsername(String username);

}
