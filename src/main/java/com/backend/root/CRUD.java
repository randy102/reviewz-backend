package com.backend.root;

import java.util.List;

public interface CRUD<E, CreateDTO, UpdateDTO> {
    List<E> getAll();
    E create(CreateDTO input);
    public E update(String id, UpdateDTO input);
    public E delete(String id);
}
