package com.client.rest.api.client.service;

import com.client.api.entitiy.Client;

import com.client.rest.api.client.dto.ClientDto;
import com.client.rest.api.client.dto.ResponseMsg;
import com.client.rest.api.client.exception.ErrorStatusEnum;
import com.client.rest.api.client.exception.ValidationException;
import com.client.rest.api.client.repository.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ClientService {

    private final ClientRepository clientRepository;
    @Autowired
    private final ModelMapper modelMapper;
    private final ValidationService validationService;

    @Autowired
    public ClientService(final ClientRepository clientRepository,
                         ModelMapper modelMapper, ValidationService validationService) {
        this.clientRepository = clientRepository;
        this.modelMapper = modelMapper;
        this.validationService = validationService;
    }

    public ResponseMsg createClient(final ClientDto clientDto) {
        ResponseMsg responseMsg = validateRequest(clientDto, false);
        if (responseMsg.getHttpStatus().equals(HttpStatus.BAD_REQUEST)) {
            return responseMsg;
        }

        clientRepository.save(convertToEntity(clientDto));

        return ResponseMsg.builder()
                .httpStatus(HttpStatus.CREATED)
                .msg(HttpStatus.CREATED.getReasonPhrase())
                .build();
    }

    public ResponseMsg updateClient(final long id, final ClientDto clientDto) {
        Optional<Client> client = clientRepository.findById(id);

        if (!client.isPresent()) {
            return ResponseMsg.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .msg(ErrorStatusEnum.CLIENT_NOT_FOUND.status)
                    .build();
        }

        log.debug("Client:{}", client.get());

        ResponseMsg responseMsg = validateRequest(clientDto, true);
        if (responseMsg.getHttpStatus().equals(HttpStatus.BAD_REQUEST)) {
            return responseMsg;
        }

        client.get().setFirstName(clientDto.getFirstName());
        client.get().setLastName(clientDto.getLastName());
        client.get().setMobileNumber(clientDto.getMobileNumber());
        client.get().setIdNumber(clientDto.getIdNumber());
        client.get().setAddress(clientDto.getAddress());

        clientRepository.save(client.get());

        return ResponseMsg.builder()
                .httpStatus(HttpStatus.CREATED)
                .msg(HttpStatus.CREATED.getReasonPhrase())
                .build();
    }

    private ResponseMsg validateRequest(final ClientDto clientDto, boolean isUpdate) {
        try {
            Client clientById = clientRepository.findByIdNumber(clientDto.getIdNumber()).orElse(null);

            validationService.validateIdNumber(clientDto.getIdNumber(), clientById != null && isUpdate);
            validationService.validateFirstName(clientDto.getFirstName());
            validationService.validateLastName(clientDto.getLastName());

            if (!isUpdate) {
                Client client = clientRepository.findByMobileNumber(clientDto.getMobileNumber()).orElse(null);

                validationService.checkMobileNumberDuplicate(client != null);
            }

            return ResponseMsg.builder()
                    .httpStatus(HttpStatus.OK)
                    .msg(HttpStatus.OK.getReasonPhrase())
                    .build();
        } catch (ValidationException e) {
            return ResponseMsg.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .msg(e.getMessage())
                    .build();
        }
    }

    public List<ClientDto> searchClient(final String searchKeyword) {
        List<ClientDto> clients = new ArrayList<>();

        clientRepository.searchClient(searchKeyword).forEach(c -> {
            clients.add(convertToDto(c));
        });

        return clients;
    }

    private Client convertToEntity(final ClientDto clientDto) {
        return modelMapper.map(clientDto, Client.class);
    }

    private ClientDto convertToDto(final Client client) {
        return modelMapper.map(client, ClientDto.class);
    }
}
