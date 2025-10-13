package net.ensah.customerservice.api;

import net.ensah.customerservice.service.ICustomerService;
import net.ensah.events.CustomerEvent;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerRestController {

     private final ICustomerService service;

    public CustomerRestController(ICustomerService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> publishEvent(@RequestBody CustomerEvent event) {
        service.publishCustomerEvent(event);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

    @GetMapping("/index")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String index() {
        return "Customer Service";
    }
}
