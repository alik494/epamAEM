package epam.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
@Component(service = Servlet.class,property ={
        "sling.servlet.paths=/services/hello"
})
public class HelloServlet extends SlingSafeMethodsServlet {
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
       response.setStatus(HttpServletResponse.SC_OK);
       response.setContentType("text/html");
       try (PrintWriter writer=response.getWriter()){
           writer.write("<p>Hello from IDE</p>");
       }
    }

}
