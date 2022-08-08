package com;

import org.checkerframework.checker.units.qual.A;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class test2 {
    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
    private static DatatypeFactory datatypeFactory;

    static {
        try {
            datatypeFactory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
    }

    int i = 0;

    /*public test2(DatatypeFactory datatypeFactory) {
        this.datatypeFactory = datatypeFactory;
    }*/

    public static void main(String[] args) throws ParseException {

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = format.parse("1996-04-01 00:00:00");

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);

        String a = "1996-04-01T00:00:00.000+06:00";
        XMLGregorianCalendar xmlGregorianCalendar = datatypeFactory.newXMLGregorianCalendar(cal);
        System.out.println(xmlGregorianCalendar);

        System.out.println(xmlGregorianCalendar.toGregorianCalendar().toZonedDateTime().toLocalDate());
    }
}
