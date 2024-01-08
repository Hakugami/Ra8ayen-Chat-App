package dao;
import java.util.List;
public interface Dao<T> {
    List<T>  getAll();
    T get(int id);
    void save(T t);
    void update(T t);
    void delete(T t);
}
