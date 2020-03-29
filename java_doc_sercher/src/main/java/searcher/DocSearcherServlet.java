package searcher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebFault;
import java.io.IOException;

@WebServlet("/searcher")
public class DocSearcherServlet extends HttpServlet {
    private Searcher searcher = new Searcher();
    private Gson gson = new GsonBuilder().create();

    public DocSearcherServlet() throws IOException {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=utf-8");
        String query = req.getParameter("query");
        if (query == null || "".equals(query)) {
            resp.setStatus(404);
            resp.getWriter().write("query 参数非法！");
            return;
        }
        resp.getWriter().write(gson.toJson(searcher.search(query)));
    }
}
