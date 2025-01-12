package com.nguyenminh.microservices.zwallet.service;

import com.nguyenminh.microservices.zwallet.model.Error;
import com.nguyenminh.microservices.zwallet.repository.ErrorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class ErrorService {
    private final ErrorRepository errorRepository;

    public List<Error> getAllList(){
        return errorRepository.findAll();
    }
    public ResponseEntity<Error> createErrorSample(Error error){

        Error error1 = Error.builder()
                .description(error.getDescription())
                .type(error.getType())
                .solved(false)
                .build();

        errorRepository.save(error1);
        return ResponseEntity.ok(error1);
    }

    public ResponseEntity<?> updateError(String id) {
        Optional<Error> error = errorRepository.findById(id);
        if(error.isEmpty()){
           return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        error.ifPresent(error1 -> {
            error1.setSolved(true);
            errorRepository.save(error1);
        });
        return ResponseEntity.ok(error);
    }

    public String deleteErrorById(String id){
        Optional<Error> error = errorRepository.findById(id);
        if(error.isEmpty()){
            ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        errorRepository.deleteById(id);
        return "Deleted";
    }



}
