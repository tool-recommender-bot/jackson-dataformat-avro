package com.fasterxml.dataformat.avro;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.avro.AvroFactory;

public class SimpleGenerationTest extends AvroTestBase
{
    public void testSimplest() throws Exception
    {
        Employee empl = new Employee();
        empl.name = "Bob";
        empl.age = 39;
        empl.emails = new String[] { "bob@aol.com", "bobby@gmail.com" };
        empl.boss = null;

        ObjectMapper mapper = new ObjectMapper(new AvroFactory());

        byte[] bytes = mapper.writer(_simpleSchema).writeValueAsBytes(empl);
        assertNotNull(bytes);
    }
}