package education.currencyexchange.servlets;

import education.currencyexchange.models.Currency;
import education.currencyexchange.repositories.CurrencyRepository;
import education.currencyexchange.services.JSONService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Optional;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

    private CurrencyRepository currencyRepository;
    private JSONService jsonService;

    @Override
    public void init() throws ServletException {
        currencyRepository = (CurrencyRepository) getServletContext().getAttribute("currencyRepository");
        jsonService = (JSONService) getServletContext().getAttribute("jsonService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            PrintWriter out = resp.getWriter();
            out.print(jsonService.getAllCurrenciesJSON(currencyRepository.findAll()));
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
            HashMap<String, String> currencyMap = optParametersMap.get();
            try {
                if (currencyRepository.findByCode(currencyMap.get("code")).isEmpty()) {
                    try {
                        currencyRepository.save(new Currency(currencyMap));
                        doGet(req, resp);
                    }
                    catch (SQLException e) {
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                        e.printStackTrace();
                    }
                }
                else {
                    resp.sendError(HttpServletResponse.SC_CONFLICT, "Currency is already exists");
                }
            } catch (SQLException e) {
                resp.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Database is unavailable");
                e.printStackTrace();
            }
        }
        else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad request. Missing requested field");
        }
    }

    private Optional<HashMap<String, String>> checkParameters(HttpServletRequest req) {
        if (req.getParameter("name") != null && !req.getParameter("name").isEmpty()) {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("name", req.getParameter("name"));
            if (req.getParameter("code") != null && req.getParameter("code").length() == 3) {
                parameters.put("code", req.getParameter("code"));
                if (req.getParameter("sign") != null && !req.getParameter("sign").isEmpty()) {
                    parameters.put("sign", req.getParameter("sign"));
                    return Optional.of(parameters);
                }
            }
        }
        return Optional.empty();
    }
}