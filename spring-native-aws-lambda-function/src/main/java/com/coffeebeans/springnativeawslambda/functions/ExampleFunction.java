package com.coffeebeans.springnativeawslambda.functions;

import com.coffeebeans.springnativeawslambda.model.Request;
import com.coffeebeans.springnativeawslambda.model.Response;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExampleFunction implements Function<Request, Response> {

	@Override
	public Response apply(final Request request) {
		log.info("Converting request into a response...");

		final Response response = Response.builder()
				.name(request.getName())
				.saved(true)
				.build();

		log.info("Converted request into a response.");
		return response;
	}
}