package com.thiagobfb.totvstestebackend.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class HeaderExposureFilterTest {

    @InjectMocks
    private HeaderExposureFilter headerExposureFilter;

    @Mock
    private FilterChain filterChain;

    @Mock
    private ServletRequest servletRequest;

    @Mock
    private HttpServletResponse servletResponse;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDoFilter() throws IOException, ServletException {
        ArgumentCaptor<String> headerNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> headerValueCaptor = ArgumentCaptor.forClass(String.class);

        headerExposureFilter.doFilter(servletRequest, servletResponse, filterChain);
        verify(servletResponse).addHeader(headerNameCaptor.capture(), headerValueCaptor.capture());
        verify(filterChain).doFilter(servletRequest, servletResponse);

        assertEquals("access-control-expose-headers", headerNameCaptor.getValue());
        assertEquals("location", headerValueCaptor.getValue());
    }
}