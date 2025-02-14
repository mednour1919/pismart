package services;

import java.util.List;

public interface IService<T> {
    void create(T entity);
    List<T> getAll();
    T getById(Long id);
    void update(T entity);
    void delete(Long id);
}
