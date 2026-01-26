package org.cook.bank_system.config;

public abstract class JacksonIgnoreAvroMixIn{

    @com.fasterxml.jackson.annotation.JsonIgnore
    public abstract org.apache.avro.Schema getSchema();

}