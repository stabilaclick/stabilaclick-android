package com.devband.stabilalib.utils;

import com.fasterxml.jackson.databind.util.StdConverter;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;

public class ByteStringToStringConverter extends StdConverter<String, String> {

    @Override
    public String convert(String value) {
        String ret;
        if (value != null) {
            try {
                ret = new String(Hex.decodeHex(value.toCharArray()), StandardCharsets.UTF_8);
            } catch (DecoderException e) {
                ret = "";
            }
        } else {
            ret = "";
        }
        return ret;
    }
}
