package servelet;

import mouble.Result;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class BaseServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        Result result = new Result();
        PrintWriter pw = resp.getWriter();

        try {
            result.setSuccess(true);
            result.setCode("200");
            result.setMessage("OK");
            result.setData(process(req));
        } catch (Exception e) {
            result.setCode("500xx");
            result.setMessage("服务器出错了");
        }
        pw.println(JSONUtil.serialize(result));
        pw.flush();
    }

    public Object process(HttpServletRequest req) throws Exception {
        return null;
    }
}
