package education.currencyexchange.repositories;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public abstract class CrudRepository <T> {
    Properties dataSourceProps;
    abstract List<T> findAll() throws SQLException;
    abstract Optional<T> findById(Long id);
    abstract void save (T entity);
    abstract void update (T entity);
    abstract void delete (Long id);
}