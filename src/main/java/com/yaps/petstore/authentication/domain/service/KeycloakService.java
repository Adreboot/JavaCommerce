package com.yaps.petstore.authentication.domain.service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.yaps.petstore.authentication.domain.dto.UserDTO;

import reactor.core.publisher.Mono;

@Service
public class KeycloakService {

	private static final Logger LOGGER = LoggerFactory.getLogger(KeycloakService.class);

	@Value("${IAM.admin.client.username}")
	private String username;

	@Value("${IAM.admin.client.password}")
	private String password;

	@Value("${IAM.admin.client.grant_type}")
	private String grant_type;

	@Value("${IAM.admin.client.client_id}")
	private String client_id;

	@Value("${keycloak.realm}")
	private String realm;

	@Autowired
	WebClient keyCloakClient;
	
//	public String getUserToken() {
//		final String mname = "getUserToken";
//		LOGGER.debug("entering " + mname);
//		SimpleKeycloakAccount keycloakAccount = (SimpleKeycloakAccount)SecurityContextHolder.getContext().getAuthentication().getDetails();
//		return keycloakAccount!=null ? keycloakAccount.getKeycloakSecurityContext().getTokenString():null;
//	}

	public HttpStatus createKeycloakUser(UserDTO userDTO) throws WebClientResponseException {
		final String mname = "createKeycloakUser";
		LOGGER.debug("entering " + mname);
		String token = getAdminToken();
		CredentialRepresentation credentials = new CredentialRepresentation();
		credentials.setType("password");
		credentials.setTemporary(false);
		credentials.setValue(userDTO.getPassword());
		UserRepresentation user = new UserRepresentation();
		user.setFirstName(userDTO.getFirstname());
		user.setLastName(userDTO.getLastname());
		user.setEmail(userDTO.getEmail());
		user.setEnabled(true);
		user.setUsername(userDTO.getUsername());
		user.setCredentials(List.of(credentials));

		ResponseEntity<Void> resp = keyCloakClient
										.post()
										.uri("/admin/realms/" + realm + "/users")
										.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
										.header("Authorization", "Bearer " + token).body(Mono.just(user), UserRepresentation.class)
										.retrieve()
										.toBodilessEntity()
										.block();
		LOGGER.debug("exiting " + mname);
		return resp.getStatusCode();
	}

	public HttpStatus updateKeycloakUser(@Valid UserDTO userDTO) throws WebClientResponseException {
		final String mname = "updateKeycloakUser";
		LOGGER.debug("entering " + mname);
		String token = getAdminToken();
		String userId = getKeycloakUSerId(token, userDTO.getUsername());

		CredentialRepresentation credentials = new CredentialRepresentation();
		credentials.setType("password");
		credentials.setTemporary(false);
		credentials.setValue(userDTO.getPassword());
		UserRepresentation user = new UserRepresentation();
		user.setFirstName(userDTO.getFirstname());
		user.setLastName(userDTO.getLastname());
		user.setEmail(userDTO.getEmail());

		user.setCredentials(List.of(credentials));

		ResponseEntity<Void> resp = keyCloakClient
									.put()
									.uri("/admin/realms/" + realm + "/users/" + userId)
									.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
									.header("Authorization", "Bearer " + token).body(Mono.just(user), UserRepresentation.class)
									.retrieve()
									.toBodilessEntity()
									.block();
//		System.err.println(resp.getStatusCode());
		LOGGER.debug("exiting " + mname);
		return resp.getStatusCode();
	}

	public HttpStatus deleteKeycloakUser(String username) {
		final String mname = "deleteKeycloakAccount";
		LOGGER.debug("entering " + mname);
		String token = getAdminToken();
		String userId = getKeycloakUSerId(token, username);
		ResponseEntity<Void> resp = keyCloakClient
										.delete()
										.uri("/admin/realms/" + realm + "/users/" + userId)
										.header("Authorization", "Bearer " + token)
										.retrieve()
										.toBodilessEntity()
										.block();
		System.err.println(resp.getStatusCode());
		LOGGER.debug("exiting " + mname);
		return resp.getStatusCode();
	}

	public String getAdminToken() {
		final String mname = "getAdminToken";
		LOGGER.debug("entering " + mname);
		String admin = "username=" + username + "&password=" + password + "&grant_type=" + grant_type + "&client_id="
				+ client_id;
		byte[] postData = admin.getBytes(StandardCharsets.UTF_8);
		Map<String, String> result = keyCloakClient
										.post()
										.uri("/realms/Master/protocol/openid-connect/token")
										.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
										.body(Mono.just(postData), byte[].class)
										.retrieve()
										.bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
										.block();
		result.forEach((k, v) -> System.err.println(k + " : " + v));
		String token = result.get("access_token");
		LOGGER.debug("exiting " + mname);
		return token;
	}

	private String getKeycloakUSerId(String token, String username) {
		UserRepresentation[] ur = keyCloakClient
									.get()
									.uri("/admin/realms/" + realm + "/users?username=" + username)
									.header("Authorization", "Bearer " + token)
									.retrieve()
									.bodyToMono(UserRepresentation[].class)
									.block();
		try {
			return ur[0].getId();
		} catch (ArrayIndexOutOfBoundsException e) {}
		return "unknown";
	}
}
