package TradeZone.config.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    private static final String UNAUTHORIZED_TEMPLATE_MESSAGE = "Unauthorized error. Message - {}";

    private static final String UNAUTHORIZED_ERROR_MESSAGE = "Error -> Unauthorized";

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException e) throws IOException {

        logger.error(UNAUTHORIZED_TEMPLATE_MESSAGE,e.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, UNAUTHORIZED_ERROR_MESSAGE);
    }
}
