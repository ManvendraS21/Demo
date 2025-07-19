package com.example.demo.controller;

import com.example.demo.repository.WriterRepository;
import com.example.demo.model.Writer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegistrationController {
    @Autowired
    private WriterRepository writerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/writer")
    public Writer registerUser(@RequestBody Writer writer){
        writer.setPassword(passwordEncoder.encode(writer.getPassword()));
        return writerRepository.save(writer);
    }
}
