package education.currencyexchange.servlets;

import education.currencyexchange.models.Currency;
import education.currencyexchange.repositories.CurrencyRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Optional;

@MultipartConfig
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
                    Optional<Currency> patchedCurrency = patchCurrency(req, optCurrency.get());
                    if (patchedCurrency.isPresent()) {
                        currencyRepository.update(patchedCurrency.get().getId(), patchedCurrency.get());
                    }
                    else {
                        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad request");
                    }
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

    private Optional<Currency> patchCurrency(HttpServletRequest req, Currency currency) {
        boolean currencyWasPatched = false;
        Optional<HashMap<String, String>> optParametersMap = readParametersForPatch(req);
        if (optParametersMap.isPresent()) {
            HashMap<String, String> parameterMap = optParametersMap.get();
            if (parameterMap.containsKey("name")) {
                currency.setFullName(parameterMap.get("name"));
                currencyWasPatched = true;
            }
            if (parameterMap.containsKey("code")) {
                if (parameterMap.get("code").length() == 3) {
                    currency.setCode(parameterMap.get("code"));
                    currencyWasPatched = true;
                }
            }
            if (parameterMap.containsKey("sign")) {
                currency.setSign(parameterMap.get("sign"));
                currencyWasPatched = true;
            }
        }
        if (!currencyWasPatched) {
            return Optional.empty();
        }
        else {
            return Optional.of(currency);
        }
    }

    private Optional<HashMap<String, String>> readParametersForPatch(HttpServletRequest req) {
        HashMap<String, String> parameters = null;
        String query;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()))) {
            if ((query = br.readLine()) != null) {
                if (!query.isEmpty()) {
                    parameters = new HashMap<>();
                    String[] pairs = query.split("&");

                    for (String pair : pairs) {
                        String[] keyValuePairs = pair.split("=");
                        if (keyValuePairs.length == 2) {
                            parameters.put(keyValuePairs[0], keyValuePairs[1]);
                        }
                    }

                    return Optional.of(parameters);
                } else {
                    return Optional.empty();
                }
            }
            else {
                return Optional.empty();
            }
        }
        catch (IOException e) {
            return Optional.empty();
        }
    }
}