package education.currencyexchange.servlets;

import education.currencyexchange.repositories.ExchangeRateRepository;
import education.currencyexchange.services.JSONService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/exchange-rates")
public class ExchangeRatesServlet extends HttpServlet {

    private ExchangeRateRepository exchangeRateRepository;
    private JSONService jsonService;

    @Override
    public void init() throws ServletException {
        exchangeRateRepository = (ExchangeRateRepository) getServletContext().getAttribute("exchangeRateRepository");
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
}
