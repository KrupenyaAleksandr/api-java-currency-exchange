package education.currencyexchange.servlets;

import education.currencyexchange.models.Currency;
import education.currencyexchange.models.ExchangeRate;
import education.currencyexchange.repositories.ExchangeRateRepository;
import education.currencyexchange.utils.Utils;

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

@WebServlet("/exchange-rate/*")
public class ExchangeRateServlet extends HttpServlet {

    private ExchangeRateRepository exchangeRateRepository;

    @Override
    public void init() throws ServletException {
        exchangeRateRepository = (ExchangeRateRepository)
                getServletContext().getAttribute("exchangeRateRepository");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        if (!method.equals("PATCH")) {
            super.service(req, resp);
        }
        else {
            this.doPatch(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<ExchangeRate> optExchangeRate = checkExchangeRateExists(req, resp);
        if (optExchangeRate.isPresent()) {
            PrintWriter out = resp.getWriter();
            out.print(optExchangeRate.get());
            out.flush();
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<ExchangeRate> optExchangeRate = checkExchangeRateExists(req, resp);
        if (optExchangeRate.isPresent()) {
            Optional<ExchangeRate> optPatchedExchangeRate = patchExchangeRate(req, optExchangeRate.get());
            if (optPatchedExchangeRate.isPresent()) {
                try {
                    exchangeRateRepository.update(optPatchedExchangeRate.get().getId(), optPatchedExchangeRate.get());
                    resp.sendRedirect(String.valueOf(req.getRequestURL()));
                } catch (SQLException e) {
                    resp.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Database is unavailable");
                }
            }
            else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad request");
            }
        }
    }

    private Optional<ExchangeRate> checkExchangeRateExists(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Optional<String> currencyCodes = Utils.checkExchangeRatePath(req.getPathInfo());
        if (currencyCodes.isPresent()) {
            try {
                Optional<ExchangeRate> optExchangeRate = exchangeRateRepository.findByCodes
                        (currencyCodes.get().substring(0, 3), currencyCodes.get().substring(3, 6));
                if (optExchangeRate.isPresent()) {
                    return optExchangeRate;
                }
                else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Exchange rate not found");
                }
            }
            catch (SQLException e) {
                resp.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Database is unavailable");
            }
        }
        else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad request");
        }
        return Optional.empty();
    }

    private Optional<ExchangeRate> patchExchangeRate(HttpServletRequest req, ExchangeRate exchangeRate) {
        Optional<HashMap<String, String>> optParameterMap = Utils.readParametersForPatch(req);
        if (optParameterMap.isPresent()) {
            HashMap<String, String> parameterMap = optParameterMap.get();
            if (parameterMap.containsKey("rate")) {
                exchangeRate.setRate(new BigDecimal(parameterMap.get("rate")));
                return Optional.of(exchangeRate);
            }
        }
        return Optional.empty();
    }
}