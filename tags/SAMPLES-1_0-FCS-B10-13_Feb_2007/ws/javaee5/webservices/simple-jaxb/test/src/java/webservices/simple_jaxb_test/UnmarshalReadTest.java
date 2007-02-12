package webservices.simple_jaxb_test;

import org.junit.*;

import javax.naming.InitialContext;
import webservices.simple_jaxb.*;

import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class UnmarshalReadTest {

  @Test public void returnMessage() {
	try {
		System.out.println("name = [" + UnmarshalRead.getName() + "]");
		Assert.assertEquals(
			"Invalid return message",
			"Alice Smith",
			UnmarshalRead.getName());
	} catch(Exception e) {
		e.printStackTrace();
		Assert.fail("encountered exception in HelloStatelessEjbTest:returnMessage");
	}
  }

}
