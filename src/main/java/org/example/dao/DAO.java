package org.example.dao;

import java.util.List;

public interface DAO<T> {
    T get(long id);
    List<T> getAll();
    boolean save(T t);
    boolean update(T t);
    boolean delete(T t);
    boolean deleteById(long id);
    List<Integer> getAllIds();
    void deleteAll();
}
