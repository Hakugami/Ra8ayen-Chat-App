package dao;
import java.sql.SQLException;
import java.util.List;
public interface Dao<T> {
    List<T>  getAll();
    T get(int id);
    boolean save(T t) throws SQLException;
    boolean update(T t);
    boolean delete(T t);
}
