package education.currencyexchange.servlets;

import education.currencyexchange.repositories.ExchangeRateRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
        }
        else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad request");
        }
    }

    private Optional<HashMap<String, String>> checkParameters(HttpServletRequest req) {
        if (req.getParameter("from") != null && req.getParameter("from").length() == 3) {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("from", req.getParameter("name"));
            if (req.getParameter("to") != null && req.getParameter("to").length() == 3) {
                parameters.put("to", req.getParameter("to"));
                if (req.getParameter("amount") != null && !req.getParameter("amount").isEmpty()) {
                    parameters.put("amount", req.getParameter("amount"));
                    return Optional.of(parameters);
                }
            }
        }
        return Optional.empty();
    }
}
