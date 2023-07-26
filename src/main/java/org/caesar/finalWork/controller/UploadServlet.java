package org.caesar.finalWork.controller;

import org.caesar.finalWork.domain.vo.Response;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@MultipartConfig(
)
@WebServlet(urlPatterns = "/upload")
public class UploadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String rootPath = req.getSession().getServletContext().getRealPath("/");
        Part part = req.getPart("file");
        System.out.println(part.getSubmittedFileName());
        part.write(rootPath + "img\\" + part.getSubmittedFileName());
        System.out.println(rootPath + "img\\" + part.getSubmittedFileName());
        Response.OK("上传成功").doResponse(resp);
    }

}
