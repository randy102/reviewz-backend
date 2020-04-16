package com.backend.image;

import com.backend.RouteConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = RouteConfig.IMAGE_BASE)
public class ImageController {

   @Autowired
   private ImageService imageService;

    @GetMapping(value = "/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] downloadImg(@PathVariable String id) {
        return imageService.getImage(id);
    }

    @PostMapping()
    public String uploadImg(@RequestParam("file") MultipartFile upload) throws IOException {
       return imageService.saveImage(upload);
    }

    //6.3
    @DeleteMapping("/{id}")
    public ImageEntity deleteImage(@PathVariable("id") String id) throws Exception{
        return imageService.deleteImage(id);
    }

}
