package controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import routes.UserRoute;

import java.util.Map;

@RestController
public class UserController implements UserRoute {
    @Override
    public ResponseEntity<String> signup(Map<String, String> requestMap) {
        return null;
    }
}
