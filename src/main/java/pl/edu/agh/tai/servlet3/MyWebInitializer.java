package pl.edu.agh.tai.servlet3;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import javax.servlet.Filter;

import pl.edu.agh.tai.config.SpringSecurityConfig;
import pl.edu.agh.tai.config.SpringWebConfig;

public class MyWebInitializer extends
		AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { SpringWebConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { SpringSecurityConfig.class };
	}

	@Override
	protected Filter[] getServletFilters() {

		final CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
		encodingFilter.setEncoding(SpringWebConfig.CHARACTER_ENCODING);
		encodingFilter.setForceEncoding(true);

		final DelegatingFilterProxy springSecurityFilter = new DelegatingFilterProxy("springSecurityFilterChain");

		return new Filter[] { encodingFilter, springSecurityFilter };

	}
}
