package com.client.rest.api.client.controller;

import com.client.rest.api.client.dto.ClientDto;
import com.client.rest.api.client.dto.ResponseMsg;
import com.client.rest.api.client.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class ClientControllerTests {
    @MockBean
    private ClientService clientService;

    @Autowired
    public MockMvc mockMvc;

    @Test
    public void createdResponse() throws Exception {
        ClientDto clientDto = ClientDto.builder()
                .firstName("Test")
                .lastName("Testing")
                .mobileNumber("0607896540")
                .idNumber("9856231025789")
                .address("16 Road Street, Joburg South, Joburg 2006")
                .build();

        ResponseMsg responseMessage = ResponseMsg.builder()
                .httpStatus(HttpStatus.CREATED)
                .msg(HttpStatus.CREATED.getReasonPhrase())
                        .build();

        doReturn(responseMessage).when(clientService).createClient(any());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/create")
                .content(asJsonString(clientDto))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(content().string("Created"));
    }

    @Test
    public void returnUpdateResponse() throws Exception {
        ClientDto clientDto = ClientDto.builder()
                .firstName("Test")
                .lastName("Testing")
                .mobileNumber("0607896540")
                .idNumber("9856231025789")
                .address("16 Road Street, Joburg South, Joburg 2006")
                .build();

        ResponseMsg responseMessage = ResponseMsg.builder()
                .httpStatus(HttpStatus.CREATED)
                .msg(HttpStatus.CREATED.getReasonPhrase())
                .build();

        doReturn(responseMessage).when(clientService).updateClient(eq(2L), any());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/update/1")
                .content(asJsonString(clientDto))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(content().string("Created"));
    }

    @Test
    public void should_return_a_list_of_clients() throws Exception {
        doReturn(getClients()).when(clientService).searchClient(any());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/search?keyword=Sa")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    private List<ClientDto> getClients() {
        return Arrays.asList(
                ClientDto.builder()
                        .firstName("Sabelo")
                        .lastName("Khumalo")
                        .mobileNumber("0825340956")
                        .idNumber("9408125465084")
                        .address("129 Khumalo street, Mofolo South, Soweto 1801")
                        .build()
        );
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
