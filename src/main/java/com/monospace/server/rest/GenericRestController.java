package com.monospace.server.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class GenericRestController {

    private final DataStore dataStore;

    @RequestMapping(value = "/{responseStatus}/**", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<?> postPut(@PathVariable int responseStatus, @RequestBody String body, HttpServletRequest request) {
        dataStore.save(request.getMethod(), request.getRequestURI(), body);
        return ResponseEntity.status(responseStatus).body("the record already exists");
    }

    @RequestMapping(value = "/{responseStatus}/**", method = {RequestMethod.GET, RequestMethod.DELETE})
    public ResponseEntity<?> getDelete(@PathVariable int responseStatus, HttpServletRequest request) {
        dataStore.save(request.getMethod(), request.getRequestURI(), request.getRequestURL() + Optional.ofNullable(request.getQueryString()).map(s -> "?" + s).orElse(""));
        return ResponseEntity.status(responseStatus).body(HttpStatus.valueOf(responseStatus));
    }

    @GetMapping(value = "/flush")
    @ResponseStatus(HttpStatus.OK)
    public void flush() {
        dataStore.flush();
    }

}
