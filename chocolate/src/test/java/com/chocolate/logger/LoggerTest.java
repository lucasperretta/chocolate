package com.chocolate.logger;

import org.junit.Test;

import static org.junit.Assert.*;

public class LoggerTest implements LoggerInterface {

    @Test public void tagDetection() {
        assertEquals("LoggerTest", getTag());
    }

}