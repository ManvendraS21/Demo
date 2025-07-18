package com.example.demo;

import com.example.demo.model.Writer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


public interface WriterRepository extends JpaRepository<Writer,String> {
    Optional<Writer> findByUsername(String username);
}
