package com.nguyenminh.microservices.zwallet.controller;

import com.nguyenminh.microservices.zwallet.service.CloudinaryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

@RestController
@RequestMapping("/pic")
@CrossOrigin("*")
public class FileUploadController {

    private final CloudinaryService cloudinaryService;

    public FileUploadController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }


    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.OK)
    public HashMap<String, String> uploadImage(@RequestParam("image") MultipartFile file) {
        String data = this.cloudinaryService.upload(file);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("url", data);
        return map;
    }
}
