package org.openbmp.api.parsed.message;
/*
 * Copyright (c) 2015-2016 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 */
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Format class for collector parsed messages (openbmp.parsed.collector)
 *
 * Schema Version: 1.2
 *
 */
public abstract class Base {
    /**
     * column field header names
     *      Will be the MAP key for fields, order matters and must match TSV order of fields.
     */
    protected String [] headerNames;

    protected List<Map<String, Object>> rowMap;

    protected Base() {
        headerNames = null;
        rowMap = new ArrayList<Map<String, Object>>();
    }

    private final Logger logger = LoggerFactory.getLogger(Base.class);

    /**
     * Parse TSV rows of data from message
     *
     * @param data          TSV data (MUST not include the headers)
     *
     * @return  True if error, False if no errors
     */
    public boolean parse(String data) {

        final CellProcessor[] processors = getProcessors();
        ICsvMapReader mapReader = null;

        try {
            mapReader = new CsvMapReader(new StringReader(data), CsvPreference.TAB_PREFERENCE);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }

        if (mapReader == null)
            return true;

        try {
            //System.out.println("Headers=" + Arrays.toString(headerNames));
            Map<String, Object> map;
            while( (map = mapReader.read(headerNames, processors)) != null )
            {
                rowMap.add(map);
                //System.out.println(String.format("lineNo=%s, rowNo=%s, Map=%s", mapReader.getLineNumber(),
                //        mapReader.getRowNumber(), map));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return true;

        } catch (org.supercsv.exception.SuperCsvException e) {
            e.printStackTrace();
            return true;

        } catch (Exception e){
            e.printStackTrace();
            return true;
        }

        return false;
    }

    /**
     * Processors used for each field.
     *
     * Order matters and must match the same order as defined in headerNames
     *
     * @return array of cell processors
     */
    protected abstract CellProcessor[] getProcessors();


    /**
     * Get rowMap as Json
     *
     * @return JSON String representing the parsed rowMap
     */
    public String toJson() {
        StringWriter swriter = new StringWriter();
        JsonFactory jfac = new JsonFactory();
        String json = "{}";

        try {
            JsonGenerator jgen = jfac.createJsonGenerator(swriter);
            jgen.setCodec(new ObjectMapper());

            // Start root object/array list
            jgen.writeStartArray();


            /*
             * Iterate the row map and write each entry as an object
             */
            for (int i=0; i < rowMap.size(); i++) {
                jgen.writeStartObject();

                for (Map.Entry<String, Object> record : rowMap.get(i).entrySet() ) {
                    jgen.writeObjectField(record.getKey(), record.getValue());
                }

                jgen.writeEndObject();
            }

            // end the root object
            jgen.writeEndArray();

            jgen.close();

            json = swriter.toString();

        } catch (JsonGenerationException e) {
            logger.error("Json exception: ", e);
        } catch (IOException e) {
            logger.error("Json io exception error: ", e);
        }

        return json;
    }

    /**
     * Get rowMap as Pretty Json
     *
     * @return Pretty formatted JSON String representing the parsed rowMap
     */
    public String toJsonPretty() {
        String json_raw = this.toJson();
        String json_pretty = "{}";

        ObjectMapper mapper = new ObjectMapper();
        try {
            Object json = mapper.readValue(json_raw, Object.class);

            json_pretty = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);

        } catch (IOException e) {
            logger.error("Json io exception error: ", e);
        }

        return json_pretty;
    }



    /**
     * Get the rowMap
     *
     * @return parsed rowMap is returned
     */
    public List<Map<String, Object>> getRowMap() {
        return rowMap;
    }
}