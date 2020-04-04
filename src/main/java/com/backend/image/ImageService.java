package com.backend.image;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Optional;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    public byte[] getImage(String id){
        Optional<ImageEntity> existed = imageRepository.findById(id);
        if(!existed.isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not found: Image");

        return existed.get().getData().getData();
    }

    /**
     * @param upload Multipart file
     * @return Image's id
     */
    public String saveImage(MultipartFile upload)throws IOException {

        // TODO: Check file upload must be type png or jpg...

        Binary imgBinary = new Binary(BsonBinarySubType.BINARY, upload.getBytes());
        ImageEntity result = imageRepository.insert(new ImageEntity(imgBinary));

        return result.getId();
    }
}
