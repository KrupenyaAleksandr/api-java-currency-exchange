package education.currencyexchange.servlets;

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
        PrintWriter out = resp.getWriter();
        try {
            out.print(jsonService.getAllCurrenciesJSON(currencyRepository.findAll()));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        out.flush();
    }

}