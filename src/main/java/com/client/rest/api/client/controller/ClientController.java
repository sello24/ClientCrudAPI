package com.client.rest.api.client.controller;



import com.client.rest.api.client.dto.ClientDto;
import com.client.rest.api.client.dto.ResponseMsg;
import com.client.rest.api.client.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class ClientController {

    private final ClientService clientService;
    private final Logger logger = LoggerFactory.getLogger(ClientController.class);

    @Autowired
    public ClientController(final ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createClient(final @RequestBody ClientDto clientDto) {

        logger.debug("Request: {}", clientDto);
        ResponseMsg responseMsg = clientService.createClient(clientDto);
        logger.debug("Response: {}", responseMsg);

        return ResponseEntity.status(responseMsg.getHttpStatus()).body(responseMsg.getMsg());

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateClient(
            final @PathVariable(value = "id") long clientId,
            final @RequestBody ClientDto clientDto) {
        ResponseMsg responseMsg = clientService.updateClient(clientId, clientDto);

        return ResponseEntity
                .status(responseMsg.getHttpStatus())
                .body(responseMsg.getMsg());
    }

    @GetMapping("/search")
    public @ResponseBody
    List<ClientDto> searchClient(final @RequestParam String keyword) {
        return clientService.searchClient(keyword);
    }
}
