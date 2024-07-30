package education.currencyexchange.repositories;

import education.currencyexchange.models.ExchangeRate;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ExchangeRateRepository extends CrudRepository<ExchangeRate> {

    @Override
    List<ExchangeRate> findAll() throws SQLException {
        return null;
    }

    @Override
    Optional<ExchangeRate> findById(Long id) throws SQLException {
        return Optional.empty();
    }

    @Override
    void save(ExchangeRate entity) throws SQLException {

    }

    @Override
    void update(Long id, ExchangeRate entity) {

    }

    @Override
    void delete(Long id) {

    }
}
