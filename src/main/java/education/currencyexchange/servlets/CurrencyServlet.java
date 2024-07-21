package education.currencyexchange.servlets;

import education.currencyexchange.repositories.CurrencyRepository;
import education.currencyexchange.services.JSONService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    private CurrencyRepository currencyRepository;
    private JSONService jsonService;

    @Override
    public void init() throws ServletException {
        currencyRepository = (CurrencyRepository) getServletContext().getAttribute("currencyRepository");
        jsonService = (JSONService) getServletContext().getAttribute("jsonService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestCurrency = req.getPathInfo();
        if (checkPathInfo(requestCurrency)) {
            requestCurrency = requestCurrency.replaceFirst("/", "").toUpperCase();

        }
        else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad request, example: .../currency/EUR");
        }
    }

    private boolean checkPathInfo(String pathInfo) {
        return pathInfo != null && !pathInfo.equals("/");
    }
}