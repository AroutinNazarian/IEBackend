package org.ce.wp.service;

import java.net.URISyntaxException;

/**
 * @author Aroutin Nazarian
 * @since 22.01.23
 */
public interface RestClientService {

    int callApi(String uri) throws URISyntaxException;
}
