package education.currencyexchange.repositories;

import education.currencyexchange.models.Currency;
import education.currencyexchange.models.ExchangeRate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class ExchangeRateRepository extends CrudRepository<ExchangeRate> {

    private final CurrencyRepository currencyRepository;

    public ExchangeRateRepository(Properties props, CurrencyRepository currencyRepository) {
        this.dataSourceProps = props;
        this.currencyRepository = currencyRepository;
    }

    @Override
    public List<ExchangeRate> findAll() throws SQLException {
        final String query = "SELECT * FROM exchange_rates;";
        List<ExchangeRate> exchangeRateList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(this.dataSourceProps.getProperty("url"), this.dataSourceProps)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.execute();
                try (ResultSet resultSet = preparedStatement.getResultSet()) {
                    while (resultSet.next()) {
                        Optional<Currency> optBaseCurrency = currencyRepository.findById(resultSet.getLong("base_currency_id"));
                        if (optBaseCurrency.isPresent()) {
                            Optional<Currency> optTargetCurrency = currencyRepository.findById(resultSet.getLong("target_currency_id"));
                            if (optTargetCurrency.isPresent()) {
                                exchangeRateList.add(new ExchangeRate(resultSet, optBaseCurrency.get(), optTargetCurrency.get()));
                            }
                        }
                    }
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        }

        return exchangeRateList;
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