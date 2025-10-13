//package net.ensah.gateway.sec;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.*;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
//import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.client.RestTemplate;
//
//
//import jakarta.servlet.http.HttpServletRequest;
//import java.net.URI;
//import java.util.Enumeration;
//
//
//@RestController
//@RequestMapping("/api")
//public class ProxyController {
//
//
//    @Autowired
//    private OAuth2AuthorizedClientManager authorizedClientManager; // pour refresh token
//
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//
//    // Exemple: proxy vers booking-service
//    @RequestMapping("/booking/**")
//    public ResponseEntity<byte[]> proxyToBooking(HttpServletRequest request, Authentication authentication) {
//// On récupère (ou on rafraîchit) le token utilisateur pour la registrationId 'keycloak'
//        OAuth2AuthorizeRequest authRequest = OAuth2AuthorizeRequest.withClientRegistrationId("keycloak")
//                .principal(authentication)
//                .build();
//
//
//        OAuth2AuthorizedClient client = authorizedClientManager.authorize(authRequest);
//        if (client == null || client.getAccessToken() == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//
//
//        String token = client.getAccessToken().getTokenValue();
//
//
//// Forward simple (ici on utilise host:port; en prod utilisez load balancer / discovery)
//        String forwardPath = request.getRequestURI().replaceFirst("/api/booking", "");
//        String target = "http://booking-service:8081" + forwardPath + (request.getQueryString() != null ? ("?" + request.getQueryString()) : "");
//
//
//        HttpHeaders headers = new HttpHeaders();
//        copyHeaders(request, headers);
//        headers.setBearerAuth(token);
//        headers.remove(HttpHeaders.COOKIE); // BFF pattern : ne pas forward cookies
//
//
//        HttpEntity<byte[]> httpEntity;
//        try {
//            byte[] body = request.getInputStream().readAllBytes();
//            httpEntity = new HttpEntity<>(body, headers);
//        } catch (Exception e) {
//            httpEntity = new HttpEntity<>(headers);
//        }
//
//
//        ResponseEntity<byte[]> resp = restTemplate.exchange(URI.create(target), HttpMethod.resolve(request.getMethod()), httpEntity, byte[].class);
//
//
//        return ResponseEntity.status(resp.getStatusCode()).headers(filterResponseHeaders(resp.getHeaders())).body(resp.getBody());
//    }
//
//
//    private void copyHeaders(HttpServletRequest request, HttpHeaders headers) {
//        Enumeration<String> names = request.getHeaderNames();
//        if (names == null) return;
//        while (names.hasMoreElements()) {
//            String name = names.nextElement();
//            if ("host".equalsIgnoreCase(name)) continue;
//            Enumeration<String> values = request.getHeaders(name);
//            while (values.hasMoreElements()) {
//                headers.add(name, values.nextElement());
//            }
//        }
//        headers.setAccept(MediaType.parseMediaTypes(request.getHeader("Accept") != null ? request.getHeader("Accept") : "*/*"));
//    }
//
//
//    private HttpHeaders filterResponseHeaders(HttpHeaders src) {
//        HttpHeaders h = new HttpHeaders();
//        src.forEach((k, v) -> {
//            if (!k.equalsIgnoreCase("transfer-encoding") && !k.equalsIgnoreCase("content-length")) {
//                h.put(k, v);
//            }
//        });
//        return h;
//    }
//}