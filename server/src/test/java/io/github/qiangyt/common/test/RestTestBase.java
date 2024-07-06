package io.github.qiangyt.common.test;

import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import io.github.qiangyt.common.json.JacksonHelper;
import io.github.qiangyt.common.rest.RestConfig;
import io.github.qiangyt.common.security.SecurityConfig;

import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * Base class for Controller unit tests, encapsulating some basic usage methods
 * for MockMVC.
 *
 * Purposes of Controller unit tests:
 * 1）Validate the correctness of URLs (including URL parameters).
 * 2）Validate JSON serialization and deserialization.
 * 3）Validate the correctness of HTTP status codes.
 * 4）Validate the passing of parameters/results between controller and service.
 *
 * Note: Although parameter validation is currently implemented at the
 * controller level,
 * it will be moved to the service layer in the future, so it is not covered in
 * these controller-level tests.
 */
@Disabled
@Execution(ExecutionMode.SAME_THREAD) // Controller tests do not support parallel execution
@Import({ RestConfig.class, SecurityConfig.class })
public class RestTestBase {

    @Autowired
    protected MockMvc mockMvc;

    String getToken() {
        var req = MockMvcRequestBuilders.post("/rest/v1/token").with(httpBasic("user", "password"));
        try {
            var result = this.mockMvc.perform(req).andExpect(status().isOk()).andReturn();
            return result.getResponse().getContentAsString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Verify that the API did not encounter an error and that the returned response
     * body (JSON only) matches the expected value.
     *
     * @param expectedResponseContent - The expected response content, can be null
     * @return
     */
    public ResultActions expectOk(ResultActions actions, Object expectedResponseContent) {
        try {
            var ra = actions.andExpect(status().isOk());
            if (expectedResponseContent == null) {
                return ra; // TBD: should we turn to return HTTP 204 (No Content)?
            }
            return ra.andExpect(content().string(JacksonHelper.to(expectedResponseContent)));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Create a GET request.
     *
     * @param urlTemplate - URL template (can use "{parameter}" as a placeholder for
     *                    parameters)
     * @param uriVars     - Parameter values corresponding to placeholders in the
     *                    URL template
     */
    public MockHttpServletRequestBuilder GET(String urlTemplate, Object... uriVars) {
        if (!urlTemplate.startsWith("/")) {
            urlTemplate = "/" + urlTemplate;
        }

        var token = getToken();

        return MockMvcRequestBuilders
                .get(urlTemplate, uriVars)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    /**
     * Create a POST request.
     *
     * @param requestContent - POST body (will be serialized into JSON)
     * @param urlTemplate    - URL template (can use "{parameter}" as a placeholder
     *                       for parameters)
     * @param uriVars        - Parameter values corresponding to placeholders in the
     *                       URL template
     */
    public MockHttpServletRequestBuilder POST(Object requestContent, String urlTemplate, Object... uriVars) {
        if (!urlTemplate.startsWith("/")) {
            urlTemplate = "/" + urlTemplate;
        }

        var token = getToken();

        return MockMvcRequestBuilders
                .post(urlTemplate, uriVars)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(JacksonHelper.to(requestContent));
    }

    /**
     * Send a GET request, then verify that the API did not encounter an error and
     * that the returned
     * response body (JSON only) matches the expected value.
     *
     * @param expectedResponseContent - The expected response content, can be null
     * @param urlTemplate             - URL template (can use "{parameter}" as a
     *                                placeholder for parameters)
     * @param uriVars                 - Parameter values corresponding to
     *                                placeholders in the URL template
     */
    public ResultActions getThenExpectOk(Object expectedResponseContent, String urlTemplate, Object... uriVars) {
        try {
            var req = GET(urlTemplate, uriVars);
            var actions = this.mockMvc.perform(req);
            return expectOk(actions, expectedResponseContent);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Send a POST request, then verify that the API did not encounter an error and
     * that the returned response body (JSON only) matches the expected value.
     *
     * @param requestContent          - POST body (will be serialized into JSON)
     * @param expectedResponseContent - The expected response content, can be null
     * @param urlTemplate             - URL template (can use "{parameter}" as a
     *                                placeholder for parameters)
     * @param uriVars                 - Parameter values corresponding to
     *                                placeholders in the URL template
     */
    public ResultActions postThenExpectOk(Object requestContent, Object expectedResponseContent, String urlTemplate,
            Object... uriVars) {
        try {
            var req = POST(requestContent, urlTemplate, uriVars);
            var actions = this.mockMvc.perform(req);
            return expectOk(actions, expectedResponseContent);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
