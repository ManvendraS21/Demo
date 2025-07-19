package com.example.demo.repository;

import com.example.demo.model.Quotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuotesRepository extends JpaRepository<Quotes,String>{}
