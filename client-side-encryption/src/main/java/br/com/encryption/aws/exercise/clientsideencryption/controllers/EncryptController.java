package br.com.encryption.aws.exercise.clientsideencryption.controllers;

import br.com.encryption.aws.exercise.clientsideencryption.encryption.EncryptionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;


@RestController
@RequestMapping("/encrypt")
public class EncryptController {

    private final EncryptionManager encryptionManager;

    @Autowired
    public EncryptController(EncryptionManager encryptionManager) {
        this.encryptionManager = encryptionManager;
    }

    @GetMapping
    public ResponseEntity<String> getEncryptData() {
        return new ResponseEntity<>(encryptionManager.encrypt("message"), HttpStatus.OK);
    }

}
