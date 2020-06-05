package com.backend.director;

import com.backend.Error;
import com.backend.director.dto.CreateDirectorDTO;
import com.backend.movie.MovieEntity;
import com.backend.root.CRUD;
import com.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DirectorSevice implements CRUD< DirectorEntity,CreateDirectorDTO,CreateDirectorDTO > {
    @Autowired
    private DirectorRepository directorRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public List<DirectorEntity> getAll() { return directorRepository.findAll();}

    @Override
    public DirectorEntity create(CreateDirectorDTO input) {
        DirectorEntity existedDirector = directorRepository.findByName(input.getName());

        if(existedDirector != null){
            throw Error.DuplicatedError("Director");
        }

        DirectorEntity CreateToCreate = new DirectorEntity(input.getName());

        return directorRepository.save(CreateToCreate);
    }

    @Override
    public DirectorEntity update(String id, CreateDirectorDTO input) {
        DirectorEntity existedDirector = directorRepository.findById(id).orElse(null);

        if(existedDirector == null){
            throw Error.NotFoundError("Director");
        }

        if(input.getName() != null) {
            DirectorEntity existedDirectorName = directorRepository.findByName(input.getName());
            if (existedDirectorName != null && !existedDirectorName.getId().equals(existedDirector.getId())) {
                throw Error.DuplicatedError("Director");
            }
            existedDirector.setName(input.getName());
        }
        return directorRepository.save(existedDirector);
    }

    @Override
    public DirectorEntity delete(String id) {
        DirectorEntity existedDirector = directorRepository.findById(id).orElse(null);

        if(existedDirector == null){
            throw Error.NotFoundError("Director");
        }

        Query query = new Query(Criteria.where("directors").is(existedDirector.getId()));
        List<MovieEntity> moviesOfCate = mongoTemplate.find(query, MovieEntity.class);
        if(moviesOfCate.size() > 0){
            throw Error.UsedError("Director");
        }
        directorRepository.deleteById(id);

        return existedDirector;
    }
}
