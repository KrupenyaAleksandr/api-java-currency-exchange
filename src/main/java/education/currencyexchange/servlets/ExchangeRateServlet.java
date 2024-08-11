package education.currencyexchange.servlets;

import education.currencyexchange.models.ExchangeRate;
import education.currencyexchange.repositories.ExchangeRateRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/exchange-rate/*")
public class ExchangeRateServlet extends HttpServlet {

    private ExchangeRateRepository exchangeRateRepository;

    @Override
    public void init() throws ServletException {
        exchangeRateRepository = (ExchangeRateRepository)
                getServletContext().getAttribute("exchangeRateRepository");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Optional<String> currencyCodes = checkPath(req.getPathInfo());
            if (currencyCodes.isPresent()) {
                Optional<ExchangeRate> optExchangeRate = exchangeRateRepository.findByCodes
                        (currencyCodes.get().substring(0, 3), currencyCodes.get().substring(3, 6));
                if (optExchangeRate.isPresent()) {
                    PrintWriter out = resp.getWriter();
                    out.print(optExchangeRate.get());
                    out.flush();
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Exchange rate not found");
                }
            }
            else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad request. Example: .../EURRUB");
            }
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Database is unavailable");
        }
    }

    private Optional<String> checkPath(String pathInfo) {
        if (pathInfo != null && !pathInfo.equals("/")) {
            if (pathInfo.replaceFirst("/", "").length() == 6) {
                return Optional.of(pathInfo.replaceFirst("/", "").toUpperCase());
            }
        }
        return Optional.empty();
    }
}