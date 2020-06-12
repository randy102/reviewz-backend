package com.backend.director;

import com.backend.RouteConfig;
import com.backend.director.dto.CreateDirectorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = RouteConfig.DIRECTOR_BASE)
public class DirectorController {
    @Autowired
    private DirectorSevice directorSevice;

    @GetMapping()
    public List<DirectorEntity> allDirector() throws Exception{
        return directorSevice.getAll();
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    public DirectorEntity createDirector(@RequestBody() CreateDirectorDTO input) throws Exception{
        return directorSevice.create(input);
    }

    @PutMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public DirectorEntity updateDiretor(@PathVariable("id") String id, @RequestBody() CreateDirectorDTO input) throws Exception{
        return directorSevice.update(id, input);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public boolean deleteDirector(@PathVariable("id") String id ) throws Exception{
        return directorSevice.delete( id );
    }
}
