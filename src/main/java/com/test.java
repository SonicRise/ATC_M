package com;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.*;
import java.util.stream.Collectors;

public class test {
    public static void main(String[] args) {
        Date today = new Date();

        GregorianCalendar gCalendar
                = new GregorianCalendar();
        gCalendar.setTime(today);
        XMLGregorianCalendar xmlCalendar = null;
        try {
            xmlCalendar
                    = DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(gCalendar);
        }
        catch (DatatypeConfigurationException ex) {
            System.out.println(ex);
        }

        System.out.println("test: " + xmlCalendar);

    }
}
