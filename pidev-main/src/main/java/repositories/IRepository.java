package repositories;

import java.util.List;

public interface IRepository<T> {
    void create(T entity);
    List<T> getAll();
    T getById(Long id);
    void update(T entity);
    void delete(Long id);
}
