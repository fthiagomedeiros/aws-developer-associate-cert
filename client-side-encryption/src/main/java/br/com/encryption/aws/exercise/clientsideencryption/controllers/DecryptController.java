package br.com.encryption.aws.exercise.clientsideencryption.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/decrypt")
public class DecryptController {

    @GetMapping
    public ResponseEntity<String> getEncryptData() {
        return new ResponseEntity<>("{'decrypt':'ok'}", HttpStatus.OK);
    }

}
