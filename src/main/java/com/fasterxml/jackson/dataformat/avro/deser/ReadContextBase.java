package com.fasterxml.jackson.dataformat.avro.deser;

import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.io.BinaryDecoder;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.dataformat.avro.*;

abstract class ReadContextBase
    extends AvroReadContext
{
    protected final static ScalarValueContext DECODER_BOOLEAN = new BooleanReader();
    protected final static ScalarValueContext DECODER_BYTES = new BytesReader();
    protected final static ScalarValueContext DECODER_DOUBLE = new DoubleReader();
    protected final static ScalarValueContext DECODER_FLOAT = new FloatReader();
    protected final static ScalarValueContext DECODER_INT = new IntReader();
    protected final static ScalarValueContext DECODER_NULL = new NullReader();
    protected final static ScalarValueContext DECODER_STRING = new StringReader();
    
    protected final AvroReadContext _parent;

    protected final AvroParserImpl _parser;
    
    protected ReadContextBase(int type, AvroReadContext parent,
            AvroParserImpl parser)
    {
        super(parent);
        _type = type;
        _parent = parent;
        _parser = parser;
    }

    /**
     * Helper method used for constructing a new context for specified
     * schema.
     * 
     * @param schema Schema that determines type of context needed
     */
    protected ReadContextBase createContext(Schema schema)
        throws IOException
    {
        switch (schema.getType()) {
        case ARRAY:
            return new ArrayContext(this, _parser, schema);
        case BOOLEAN:
            return DECODER_BOOLEAN;
        case BYTES: 
            return DECODER_BYTES;
        case DOUBLE: 
            return DECODER_DOUBLE;
        case ENUM: 
            // !!! TODO
            break;
        case FIXED: 
            // !!! TODO
            break;
        case FLOAT: 
            return DECODER_FLOAT;
        case INT: 
            return DECODER_INT;
        case LONG: 
            return DECODER_NULL;
        case MAP: 
            return new MapContext(this, _parser, schema);
        case NULL: 
            return DECODER_NULL;
        case RECORD:
            return new RecordContext(this, _parser, schema);
        case STRING: 
            return DECODER_STRING;
        case UNION:
            break;
        }
        throw new IllegalStateException("Unrecognized Avro Schema type: "+schema.getType());
    }    

    protected abstract boolean isStructured();

    /*
    /**********************************************************
    /* Simple leaf-value context implementations
    /**********************************************************
     */

    /**
     * Base class for simple scalar (non-structured) value
     * context implementations. These contexts are never
     * assigned to parser, and do not create a new scope.
     */
    protected abstract static class ScalarValueContext
        extends ReadContextBase
    {
        protected ScalarValueContext() {
            // no real type, not exposed to calling app, nor linked
            super(0, null, null);
        }

        @Override
        protected final boolean isStructured() {
            return false;
        }

        @Override
        protected void appendDesc(StringBuilder sb) {
            sb.append("?");
        }
    }

    protected final static class BooleanReader
        extends ScalarValueContext
    {
        @Override public JsonToken nextToken(BinaryDecoder dec) throws IOException {
            return dec.readBoolean() ? JsonToken.VALUE_TRUE : JsonToken.VALUE_FALSE;
        }
    }

    protected final static class BytesReader
        extends ScalarValueContext
    {
        @Override public JsonToken nextToken(BinaryDecoder dec) throws IOException {
//            byte[] bytes = dec.readBy
            // !!! TODO
            return null;
        }
    }

    protected final static class DoubleReader
        extends ScalarValueContext
    {
        @Override public JsonToken nextToken(BinaryDecoder dec) throws IOException {
            double v = dec.readDouble();
            // !!! TODO: callback
            return JsonToken.VALUE_NUMBER_FLOAT;
        }
    }

    protected final static class FloatReader
        extends ScalarValueContext
    {
        @Override public JsonToken nextToken(BinaryDecoder dec) throws IOException {
            float v = dec.readFloat();
            // !!! TODO: callback
            return JsonToken.VALUE_NUMBER_FLOAT;
        }
    }

    protected final static class IntReader
        extends ScalarValueContext
    {
        @Override public JsonToken nextToken(BinaryDecoder dec) throws IOException {
            int v = dec.readInt();
            // !!! TODO: callback
            return JsonToken.VALUE_NUMBER_INT;
        }
    }

    protected final static class LongReader
        extends ScalarValueContext
    {
        @Override public JsonToken nextToken(BinaryDecoder dec) throws IOException {
            long v = dec.readLong();
            // !!! TODO: callback
            return JsonToken.VALUE_NUMBER_INT;
        }
    }

    protected final static class NullReader
        extends ScalarValueContext
    {
        @Override public JsonToken nextToken(BinaryDecoder dec) throws IOException {
            return JsonToken.VALUE_NULL;
        }
    }

    protected final static class StringReader
        extends ScalarValueContext
    {
        @Override public JsonToken nextToken(BinaryDecoder dec) throws IOException {
            String str = dec.readString();
            return JsonToken.VALUE_STRING;
        }
    }
    
}
