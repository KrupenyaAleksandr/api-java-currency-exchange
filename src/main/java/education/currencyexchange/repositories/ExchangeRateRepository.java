package education.currencyexchange.repositories;

import education.currencyexchange.models.Currency;
import education.currencyexchange.models.ExchangeRate;

import javax.xml.transform.Result;
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

    public Optional<ExchangeRate> findByCodes(String baseCurrencyCode, String targetCurrencyCode) throws SQLException {
        ExchangeRate resultExchangeRate = null;
        try {
            Optional<Currency> optBaseCurrency = currencyRepository.findByCode(baseCurrencyCode);
            if (optBaseCurrency.isPresent()) {
                Optional<Currency> optTargetCurrency = currencyRepository.findByCode(targetCurrencyCode);
                if (optTargetCurrency.isPresent()) {
                    final String query = "SELECT * FROM currency_schema.exchange_rates " +
                                        "WHERE base_currency_id = ? AND target_currency_id = ?;";
                    try (Connection connection = DriverManager.getConnection(this.dataSourceProps.getProperty("url"), this.dataSourceProps)) {
                        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                            preparedStatement.setLong(1, optBaseCurrency.get().getId());
                            preparedStatement.setLong(2, optTargetCurrency.get().getId());
                            preparedStatement.execute();

                            ResultSet resultSet = preparedStatement.getResultSet();
                            if (resultSet.next()) {
                                resultExchangeRate = new ExchangeRate
                                        (resultSet, optBaseCurrency.get(), optTargetCurrency.get());
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
        return Optional.ofNullable(resultExchangeRate);
    }

    @Override
    Optional<ExchangeRate> findById(Long id) throws SQLException {
        return Optional.empty();
    }

    @Override
    public void save(ExchangeRate entity) throws SQLException {
        final String query = "INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate) VALUES (?, ?, ?);";

        try (Connection connection = DriverManager.getConnection(this.dataSourceProps.getProperty("url"), this.dataSourceProps)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setLong(1, entity.getBaseCurrency().getId());
                preparedStatement.setLong(2, entity.getTargetCurrency().getId());
                preparedStatement.setBigDecimal(3, entity.getRate());
                preparedStatement.execute();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public void update(Long id, ExchangeRate entity) throws SQLException {
        final String query = "UPDATE exchange_rates SET rate = ? WHERE id = ?;";

        try (Connection connection = DriverManager.getConnection(this.dataSourceProps.getProperty("url"), this.dataSourceProps)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setBigDecimal(1, entity.getRate());
                preparedStatement.setLong(2, id);
                preparedStatement.execute();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    void delete(Long id) {

    }
}