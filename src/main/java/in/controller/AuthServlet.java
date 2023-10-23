package in.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import config.DBConnectionProvider;
import config.PropertyUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import model.Player;
import model.PlayerDTO;
import repository.*;
import service.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@RequiredArgsConstructor
@WebServlet("/auth")
public class AuthServlet extends HttpServlet {


    private final ObjectMapper objectMapper;

    public AuthServlet() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        Player player = new Player(1, "Pavel", "123", 0);
        resp.getOutputStream().write(this.objectMapper.writeValueAsBytes(player));
    }
//    @Override
//    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        System.out.println(1111);
//        resp.setContentType("application/json");
//        resp.setContentType("text/html;charset=UTF-8");

//        try (PrintWriter out = resp.getWriter()) {
//            try (BufferedReader reader = req.getReader()) {
//                StringBuffer body = new StringBuffer();
//                String line = null;
//
//                while ((line = reader.readLine()) != null) {
//                    body.append(line);
//                }
//                ObjectMapper objectMapper = new ObjectMapper();
//                PlayerDTO playerDTO = objectMapper.readValue(req.getInputStream(), PlayerDTO.class);
//                try {
//                    playerService.create(playerDTO.getName(), playerDTO.getPassword());
//                } catch (SQLException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }
//        resp.setContentType("application/json");
//        resp.setStatus(HttpServletResponse.SC_ACCEPTED);
//    }


}
