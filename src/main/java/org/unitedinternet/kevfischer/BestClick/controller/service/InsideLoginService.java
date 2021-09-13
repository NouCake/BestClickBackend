package org.unitedinternet.kevfischer.BestClick.controller.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.unitedinternet.kevfischer.BestClick.model.ProviderInformation;
import org.unitedinternet.kevfischer.BestClick.model.Ticket;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class InsideLoginService {

    private final RestTemplate template;

    public InsideLoginService(RestTemplateBuilder builder) {
        this.template = builder.build();
    }

    public ProviderInformation authentificateTicket(Ticket ticket) throws JsonProcessingException {
        String serviceUrl = "https://bestclick.azubi.server.lan/api/auth/login/inside?bestTicket="+ ticket.getTicketId();
        String url = "https://login.1and1.org/ims-sso/serviceValidate?service=" + URLEncoder.encode(serviceUrl, StandardCharsets.UTF_8);
        url += "&ticket=" + ticket.getProviderTicket();

        String response = null;
        XmlMapper mapper = new XmlMapper();
        try {
            response = template.getForObject(url, String.class);
        } catch (HttpClientErrorException e) {
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, e.getResponseBodyAsString());
        }
        JsonNode node = mapper.readValue(response, ObjectNode.class).get("authenticationSuccess");
        if(node == null) {
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, response);
        }

        ProviderInformation info = new ProviderInformation();
        info.setUsername(node.get("user").asText());
        node = node.get("attributes");
        info.setName(node.get("firstName").asText() + " " + node.get("lastName").asText());
        info.setEmail(node.get("email").asText());
        info.setProviderId(node.get("personId").asText());
        info.setPictureUrl("https://united-internet.org/profiles/personalpicture/" + node.get("personId").asText());

        return info;
    }

}
