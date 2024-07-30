package education.currencyexchange.repositories;

import education.currencyexchange.models.Currency;

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
        List<Currency> currencyList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(this.dataSourceProps.getProperty("url"), this.dataSourceProps)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.execute();
                try (ResultSet resultSet = preparedStatement.getResultSet()) {
                    while (resultSet.next()) {
                        currencyList.add(new Currency(resultSet));
                    }
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        }

        return currencyList;
    }

    @Override
    public Optional<Currency> findById(Long id) throws SQLException {
        final String query = "SELECT * FROM currencies WHERE id = ?;";
        Currency resultCurrency = null;

        try (Connection connection = DriverManager.getConnection(this.dataSourceProps.getProperty("url"), this.dataSourceProps)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setLong(1, id);
                preparedStatement.execute();
                try (ResultSet resultSet = preparedStatement.getResultSet()) {
                    if (resultSet.next()) {
                        resultCurrency = new Currency(resultSet);
                    }
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("findByIdCurrency SQLException");
        }

        return Optional.ofNullable(resultCurrency);
    }

    public Optional<Currency> findByCode(String code) throws SQLException {
        final String query = "SELECT * FROM currencies WHERE code = ?;";
        Currency resultCurrency = null;

        try (Connection connection = DriverManager.getConnection(this.dataSourceProps.getProperty("url"), this.dataSourceProps)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, code);
                preparedStatement.execute();
                try (ResultSet resultSet = preparedStatement.getResultSet()) {
                    if (resultSet.next()) {
                        resultCurrency = new Currency(resultSet);
                    }
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("findByCodeCurrency SQLException");
        }

        return Optional.ofNullable(resultCurrency);
    }

    @Override
    public void save(Currency entity) throws SQLException {
        final String query = "INSERT INTO currencies (full_name, code, sign) VALUES (?, ?, ?);";

        try (Connection connection = DriverManager.getConnection(this.dataSourceProps.getProperty("url"), this.dataSourceProps)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, entity.getFullName());
                preparedStatement.setString(2, entity.getCode().toUpperCase());
                preparedStatement.setString(3, entity.getSign());
                preparedStatement.execute();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public void update(Long id, Currency entity) throws SQLException {
        final String query = "UPDATE currencies SET (full_name, code, sign) = (?, ?, ?) WHERE id = ?;";

        try (Connection connection = DriverManager.getConnection(this.dataSourceProps.getProperty("url"), this.dataSourceProps)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, entity.getFullName());
                preparedStatement.setString(2, entity.getCode());
                preparedStatement.setString(3, entity.getSign());
                preparedStatement.setLong(4, id);
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new SQLException(e.getMessage());
            }
        }
    }

    @Override
    public void delete(Long id) {

    }
}