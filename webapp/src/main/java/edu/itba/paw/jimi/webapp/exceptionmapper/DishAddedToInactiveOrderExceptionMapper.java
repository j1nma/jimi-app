package edu.itba.paw.jimi.webapp.exceptionmapper;

import edu.itba.paw.jimi.interfaces.exceptions.DishAddedToInactiveOrderException;
import edu.itba.paw.jimi.webapp.dto.ExceptionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class DishAddedToInactiveOrderExceptionMapper implements ExceptionMapper<DishAddedToInactiveOrderException> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DishAddedToInactiveOrderExceptionMapper.class);
	
	@Override
	public Response toResponse(final DishAddedToInactiveOrderException exception) {
		LOGGER.warn("Exception: {}", (Object[]) exception.getStackTrace());
		return Response.status(Response.Status.CONFLICT).entity(new ExceptionDTO(exception.getMessage())).type(MediaType.APPLICATION_JSON).build();
	}
}