package com.example.testtask.repos;

import java.io.Serializable;
import java.util.List;

public interface Repository<T, ID extends Serializable> {

    T save (T item);

    void delete(T item);

    void deleteAll();

    List<T> findAll();

    T findById(ID id);

    T findByName(String name);
}
