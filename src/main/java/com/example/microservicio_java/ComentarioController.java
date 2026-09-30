package com.example.microservicio_java;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@RestController
public class ComentarioController {

    private static final String SUPABASE_URL = System.getenv("SUPABASE_URL");
    private static final String SUPABASE_KEY = System.getenv("SUPABASE_KEY");

    private HttpHeaders headersBase() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", SUPABASE_KEY);
        headers.set("Authorization", "Bearer " + SUPABASE_KEY);
        headers.set("Content-Type", "application/json");
        headers.set("Prefer", "return=representation");
        return headers;
    }

    @PostMapping("/comentarios")
    public ResponseEntity<String> insertar(@RequestBody Map<String, Object> datos) {
        if (datos.get("nombre_cliente") == null || datos.get("comentario") == null) {
            return ResponseEntity.badRequest().body("{\"error\":\"Faltan datos obligatorios\"}");
        }
        RestTemplate rt = new RestTemplate();
        HttpEntity<Map<String, Object>> req = new HttpEntity<>(datos, headersBase());
        ResponseEntity<String> resp = rt.postForEntity(SUPABASE_URL + "/rest/v1/comentarios", req, String.class);
        return ResponseEntity.status(201).body(resp.getBody());
    }

    @PutMapping("/comentarios/{id}")
    public ResponseEntity<String> actualizar(@PathVariable int id, @RequestBody Map<String, Object> datos) {
        RestTemplate rt = new RestTemplate();
        HttpEntity<Map<String, Object>> req = new HttpEntity<>(datos, headersBase());
        ResponseEntity<String> resp = rt.exchange(
            SUPABASE_URL + "/rest/v1/comentarios?id=eq." + id, HttpMethod.PATCH, req, String.class);
        return ResponseEntity.ok(resp.getBody());
    }

    @DeleteMapping("/comentarios/{id}")
    public ResponseEntity<String> eliminar(@PathVariable int id) {
        RestTemplate rt = new RestTemplate();
        HttpEntity<Void> req = new HttpEntity<>(headersBase());
        rt.exchange(SUPABASE_URL + "/rest/v1/comentarios?id=eq." + id, HttpMethod.DELETE, req, String.class);
        return ResponseEntity.noContent().build();
    }
}
