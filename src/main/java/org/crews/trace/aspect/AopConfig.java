package org.crews.trace.aspect;

import org.crews.trace.logtrace.LogTrace;
import org.crews.trace.logtrace.ThreadLocalLogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AopConfig {
	@Bean
	public LogTrace logTrace() {
		return new ThreadLocalLogTrace();
	}

	@Bean
	public LogTraceAspect logTraceAspect(LogTrace logTrace) {
		return new LogTraceAspect(logTrace);
	}

}
