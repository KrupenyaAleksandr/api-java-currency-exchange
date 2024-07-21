package education.currencyexchange.repositories;

import education.currencyexchange.dto.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class CurrencyRepository extends CrudRepository<Currency> {

    public CurrencyRepository(Properties props) {
        this.dataSourceProps = props;
    }

    @Override
    public List<Currency> findAll() throws SQLException {
        final String query = "SELECT * FROM currencies;";
        List<Currency> listCurrency = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(this.dataSourceProps.getProperty("url"), this.dataSourceProps)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.execute();
                try (ResultSet resultSet = preparedStatement.getResultSet()) {
                    while (resultSet.next()) {
                        listCurrency.add(new Currency(resultSet));
                    }
                }
            }
            return listCurrency;
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("findAll SQLException");
        }
    }

    @Override
    public Optional<Currency> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public void save(Currency entity) {

    }

    @Override
    public void update(Currency entity) {

    }

    @Override
    public void delete(Long id) {

    }
}