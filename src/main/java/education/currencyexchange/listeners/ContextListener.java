package education.currencyexchange.listeners;

import education.currencyexchange.repositories.CurrencyRepository;
import education.currencyexchange.services.JSONService;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        Properties dataSourceProps;

        try (InputStream input = ContextListener.class.getClassLoader().getResourceAsStream("dbconfig.properties")) {
            dataSourceProps = new Properties();
            dataSourceProps.load(input);
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        CurrencyRepository currencyRepository = new CurrencyRepository(dataSourceProps);
        JSONService jsonService = new JSONService();
        servletContext.setAttribute("currencyRepository", currencyRepository);
        servletContext.setAttribute("jsonService", jsonService);
    }
}
