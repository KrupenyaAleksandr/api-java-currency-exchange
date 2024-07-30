package education.currencyexchange.servlets;

import education.currencyexchange.models.Currency;
import education.currencyexchange.repositories.CurrencyRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {

    private CurrencyRepository currencyRepository;

    @Override
    public void init() throws ServletException {
        currencyRepository = (CurrencyRepository) getServletContext().getAttribute("currencyRepository");
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
        Optional<String> optPathInfo = checkPath(req.getPathInfo());
        if (optPathInfo.isPresent()) {
            try {
                Optional<Currency> optCurrency = currencyRepository.findByCode(optPathInfo.get());
                if (optCurrency.isPresent()) {
                    PrintWriter out = resp.getWriter();
                    out.print(optCurrency.get().toString());
                    out.flush();
                }
                else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Currency not found");
                }
            }
            catch (SQLException e) {
                resp.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Database is unavailable");
            }
        }
        else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad request, example: .../currency/EUR");
        }
    }

    private void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<String> optPathInfo = checkPath(req.getPathInfo());
        if (optPathInfo.isPresent()) {
            try {
                Optional<Currency> optCurrency = currencyRepository.findByCode(optPathInfo.get());
                if (optCurrency.isPresent()) {
                    Currency patchedCurrency = patchCurrency(req, optCurrency.get());
                    currencyRepository.update(patchedCurrency.getId(), patchedCurrency);
                }
                else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Currency not found");
                }
            }
            catch (SQLException e) {
                resp.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Database is unavailable");
            }
        }
        else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad request, example: .../currency/EUR");
        }
    }

    private Optional<String> checkPath(String pathInfo) {
        if (pathInfo != null && !pathInfo.equals("/")) {
            return Optional.of(pathInfo.replaceFirst("/", "").toUpperCase());
        }
        return Optional.empty();
    }

    //TODO: read parameters from inputstream
    private Currency patchCurrency(HttpServletRequest req, Currency currency) {
        if (!req.getParameter("name").isEmpty()) {
            currency.setFullName(req.getParameter("name"));
        }
        if (!req.getParameter("code").isEmpty()) {
            currency.setCode(req.getParameter("code"));
        }
        if (!req.getParameter("sign").isEmpty()) {
            currency.setSign(req.getParameter("sign"));
        }
        return currency;
    }
}