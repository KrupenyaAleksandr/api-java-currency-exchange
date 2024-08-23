package education.currencyexchange.servlets;

import education.currencyexchange.models.Currency;
import education.currencyexchange.models.ExchangeRate;
import education.currencyexchange.repositories.CurrencyRepository;
import education.currencyexchange.repositories.ExchangeRateRepository;
import education.currencyexchange.services.JSONService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Optional;

@WebServlet("/exchange-rates")
public class ExchangeRatesServlet extends HttpServlet {

    private ExchangeRateRepository exchangeRateRepository;
    private CurrencyRepository currencyRepository;
    private JSONService jsonService;

    @Override
    public void init() throws ServletException {
        exchangeRateRepository = (ExchangeRateRepository) getServletContext().getAttribute("exchangeRateRepository");
        currencyRepository = (CurrencyRepository) getServletContext().getAttribute("currencyRepository");
        jsonService = (JSONService) getServletContext().getAttribute("jsonService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            PrintWriter out = resp.getWriter();
            out.print(jsonService.getAllExchangeRatesJSON(exchangeRateRepository.findAll()));
            out.flush();
        }
        catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Database is unavailable. " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<HashMap<String, String>> optParametersMap = checkParameters(req);
        if (optParametersMap.isPresent()) {
            HashMap<String, String> parametersMap = optParametersMap.get();
            try {
                Optional<Currency> optBaseCurrency = currencyRepository.findByCode(parametersMap.get("baseCurrencyCode"));
                if (optBaseCurrency.isPresent()) {
                    Optional<Currency> optTargetCurrency = currencyRepository.findByCode(parametersMap.get("targetCurrencyCode"));
                    if (optTargetCurrency.isPresent()) {
                        if (exchangeRateRepository.findByCodes(optBaseCurrency.get().getCode(), optTargetCurrency.get().getCode()).isEmpty()) {
                            exchangeRateRepository.save(
                                    new ExchangeRate(optBaseCurrency.get(), optTargetCurrency.get(), new BigDecimal(parametersMap.get("rate")))
                            );
                            doGet(req, resp);
                        }
                        else {
                            resp.sendError(HttpServletResponse.SC_CONFLICT, "Exchange rate already exists");
                        }
                    }
                    else {
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Target currency not found in database");
                    }
                }
                else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Base currency not found in database");
                }
            } catch (SQLException e) {
                resp.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, e.getMessage());
            }
        }
        else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad request");
        }
    }

    private Optional<HashMap<String, String>> checkParameters(HttpServletRequest req) {
        if (req.getParameter("baseCurrencyCode") != null && !req.getParameter("baseCurrencyCode").isEmpty()) {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("baseCurrencyCode", req.getParameter("baseCurrencyCode").toUpperCase());
            if (req.getParameter("targetCurrencyCode") != null && !req.getParameter("targetCurrencyCode").isEmpty()) {
                parameters.put("targetCurrencyCode", req.getParameter("targetCurrencyCode").toUpperCase());
                if (req.getParameter("rate") != null && !req.getParameter("rate").isEmpty()) {
                    parameters.put("rate", req.getParameter("rate"));
                    return Optional.of(parameters);
                }
            }
        }
        return Optional.empty();
    }
}
