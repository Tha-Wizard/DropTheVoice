package com.ssafy.a505.repository;

import com.ssafy.a505.domain.entity.Voice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoiceRepository extends JpaRepository<Voice, Long> {
    List<Voice> findByTitleContaining(String userNam, Pageable pageable);
    List<Voice> findByTitleContaining(String userNam);
    List<Voice> findAllByUserId(Long userId, Pageable pageable);
}
