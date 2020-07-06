package com.matsoft.exception;

import com.matsoft.LoggerInitializer;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class UrlNotValidExceptionTest {

    private static String DEFAULT_MESSAGE = "URL is not valid";
    private static String NOT_LEGAL_URL = "The Matrix has you";
    private UrlNotValidException e;

    @BeforeClass
    public static void initEnvironment() {
        LoggerInitializer.initLogger();
    }

    @After
    public void tearDown() {
        e = null;
    }

    @Test
    public void constructorDefault() {
        e = new UrlNotValidException();
        assertEquals(DEFAULT_MESSAGE, e.getMessage());
    }

    @Test
    public void constructorNull() {
        e = new UrlNotValidException(null);

        assertEquals(DEFAULT_MESSAGE, e.getMessage());
    }

    @Test
    public void constructorEmpty() {
        String expected = " this " + DEFAULT_MESSAGE;
        e = new UrlNotValidException("");
        assertEquals(expected, e.getMessage());
    }

    @Test
    public void constructorNotEmpty() {
        String expected = NOT_LEGAL_URL + " this " + DEFAULT_MESSAGE;
        e = new UrlNotValidException(NOT_LEGAL_URL);

        assertEquals(expected, e.getMessage());
    }
}