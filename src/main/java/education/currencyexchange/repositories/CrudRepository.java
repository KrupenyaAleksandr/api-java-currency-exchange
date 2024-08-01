package education.currencyexchange.repositories;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public abstract class CrudRepository <T> {
    Properties dataSourceProps;
    abstract List<T> findAll() throws SQLException;
    //maybe ne nuzhen voobsche?
    abstract Optional<T> findById(Long id) throws SQLException;
    abstract void save (T entity) throws SQLException;
    abstract void update (Long id, T entity) throws SQLException;
    abstract void delete (Long id) throws SQLException;
}