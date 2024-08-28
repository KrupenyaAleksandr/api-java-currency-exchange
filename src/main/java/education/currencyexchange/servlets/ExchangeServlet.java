package education.currencyexchange.servlets;

import education.currencyexchange.models.ExchangeRate;
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
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Optional;

@WebServlet("/exchange/*")
public class ExchangeServlet extends HttpServlet {

    private ExchangeRateRepository exchangeRateRepository;

    @Override
    public void init() throws ServletException {
        exchangeRateRepository = (ExchangeRateRepository)
                getServletContext().getAttribute("exchangeRateRepository");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<HashMap<String, String>> optParameterMap = checkParameters(req);
        if (optParameterMap.isPresent()) {
            HashMap<String, String> parameterMap = optParameterMap.get();
            PrintWriter out = resp.getWriter();
            try {
                Optional<ExchangeRate> optExchangeRate =
                        exchangeRateRepository.findByCodes(parameterMap.get("from"), parameterMap.get("to"));
                if (optExchangeRate.isEmpty()) {
                    optExchangeRate = exchangeRateRepository.findByCodes(parameterMap.get("to"), parameterMap.get("from"));
                    if (optExchangeRate.isEmpty()) {
                        optExchangeRate = exchangeRateRepository.findByCodes("USD", parameterMap.get("from"));
                        if (optExchangeRate.isPresent()) {
                            ExchangeRate tmpExchangeRate = optExchangeRate.get();
                            optExchangeRate = exchangeRateRepository.findByCodes("USD", parameterMap.get("to"));
                            if (optExchangeRate.isPresent()) {
                                out.print(JSONService.getCrossExchangeJSON(
                                        tmpExchangeRate,
                                        optExchangeRate.get(),
                                        new BigDecimal(parameterMap.get("amount")),
                                        new BigDecimal(
                                                parameterMap.get("amount"))
                                                .divide(tmpExchangeRate.getRate(), 6, RoundingMode.FLOOR)
                                                .multiply(optExchangeRate.get().getRate())
                                                .setScale(3, RoundingMode.FLOOR)
                                        ));
                            }
                            else {
                                out.print("\"message\": \"Валютная пара USD-B для кросс-обмена не найдена\"");
                            }
                        }
                        else {
                            out.print("\"message\": \"Валютная пара USD-A для кросс-обмена не найдена\"");
                        }
                    }
                    else {
                        out.print(JSONService.getExchangeJSON(
                                optExchangeRate.get(),
                                new BigDecimal(parameterMap.get("amount")),
                                new BigDecimal(parameterMap.get("amount"))
                                        .divide(optExchangeRate.get().getRate(), 6, RoundingMode.FLOOR)
                                        .setScale(3, RoundingMode.FLOOR)
                                ));
                    }
                }
                else {
                    out.print(JSONService.getExchangeJSON(
                            optExchangeRate.get(),
                            new BigDecimal(parameterMap.get("amount")),
                            new BigDecimal(parameterMap.get("amount"))
                                    .multiply(optExchangeRate.get().getRate())
                                    .setScale(3, RoundingMode.FLOOR)
                            ));
                }
                out.flush();
            }
            catch (SQLException e) {
                resp.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Database unavailable");
                e.printStackTrace();
            }
        }
        else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad request");
        }
    }

    private Optional<HashMap<String, String>> checkParameters(HttpServletRequest req) {
        if (req.getParameter("from") != null && req.getParameter("from").length() == 3) {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("from", req.getParameter("from").toUpperCase());
            if (req.getParameter("to") != null && req.getParameter("to").length() == 3) {
                parameters.put("to", req.getParameter("to").toUpperCase());
                if (req.getParameter("amount") != null && !req.getParameter("amount").isEmpty()) {
                    parameters.put("amount", req.getParameter("amount"));
                    return Optional.of(parameters);
                }
            }
        }
        return Optional.empty();
    }
}
