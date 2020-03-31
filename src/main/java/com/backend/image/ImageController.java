package com.backend.image;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/image")
public class ImageController {

    @Autowired
    private ImageRepository imageRepository;

    @GetMapping(value = "/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] downloadImg(@PathVariable String id) throws IOException {
        Optional<ImageEntity> existed = imageRepository.findById(id);

        if(!existed.isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not found: Image");

        return existed.get().getData().getData();
    }

    @PutMapping("/")
    public String uploadImg(@RequestParam("file") MultipartFile upload) throws IOException {
        Binary imgBinary = new Binary(BsonBinarySubType.BINARY, upload.getBytes());
        ImageEntity result = imageRepository.insert(new ImageEntity(imgBinary));
        return result.getId();
    }

}
