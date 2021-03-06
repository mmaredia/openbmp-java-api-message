package org.openbmp.api.parsed.message;
/*
 * Copyright (c) 2015-2016 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 */

import org.openbmp.api.parsed.processor.ParseLongEmptyAsZero;
import org.openbmp.api.parsed.processor.ParseNullAsEmpty;
import org.openbmp.api.parsed.processor.ParseTimestamp;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * Format class for base_attribute parsed messages (openbmp.parsed.base_attribute)
 *
 * Schema Version: 1.2
 *
 */
public class BaseAttribute extends Base {

	    // Minimum set of headers each Object will have.
		String [] minimumHeaderNames =new String[]{MsgBusFields.ACTION.getName(),MsgBusFields.SEQUENCE.getName(),MsgBusFields.HASH.getName(),MsgBusFields.ROUTER_HASH.getName(),
												  MsgBusFields.ROUTER_IP.getName(),MsgBusFields.PEER_HASH.getName(),MsgBusFields.PEER_IP.getName(),MsgBusFields.PEER_ASN.getName(),
												  MsgBusFields.TIMESTAMP.getName(),MsgBusFields.ORIGIN.getName(),MsgBusFields.AS_PATH.getName(),MsgBusFields.AS_PATH_COUNT.getName(),
												  MsgBusFields.ORIGIN_AS.getName(),MsgBusFields.NEXTHOP.getName(),MsgBusFields.MED.getName(),MsgBusFields.LOCAL_PREF.getName(),
												  MsgBusFields.AGGREGATOR.getName(),MsgBusFields.COMMUNITY_LIST.getName(),MsgBusFields.EXT_COMMUNITY_LIST.getName(),
												  MsgBusFields.CLUSTER_LIST.getName(),MsgBusFields.ISATOMICAGG.getName(),MsgBusFields.IS_NEXTHOP_IPV4.getName(),MsgBusFields.ORIGINATOR_ID.getName()};
	
	
   
	/**
	 * base constructor to support backward compatibility. Will run on the {@link Base.DEFAULT_SPEC_VERSION} version.
	 * @param data
	 */
    public BaseAttribute(String data) {
        super();
        
        headerNames = minimumHeaderNames;

        parse(data);
    }
    
    
    /**
     * Handle the message by parsing it and storing the data in memory.
     *
     * @param data
     */
    public BaseAttribute(Float version, String data) {
        super();
        
        spec_version = version;
       
        //Headers are same upto version 1.2 for this Object. 
        //TODO:: If needed, add additional headers with later version. Refer to {@link LsLink} constructor.
        headerNames = minimumHeaderNames;

        parse(version, data);
    }
    
    

    /**
     * Processors used for each field.
     *
     * Order matters and must match the same order as defined in headerNames
     *
     * @return array of cell processors
     */
    protected CellProcessor[] getProcessors() {

        final CellProcessor[] processors = new CellProcessor[] {
                new NotNull(),                      // action
                new ParseLong(),                    // seq
                new NotNull(),                      // hash
                new NotNull(),                      // router hash
                new NotNull(),                      // router_ip
                new NotNull(),                      // peer_hash
                new NotNull(),                      // peer_ip
                new ParseLong(),                    // peer_asn
                new ParseTimestamp(),               // timestamp
                new ParseNullAsEmpty(),             // origin
                new ParseNullAsEmpty(),             // as_path
                new ParseLong(),                    // as_path_count
                new ParseLong(),                    // origin_as
                new ParseNullAsEmpty(),             // nexthop
                new ParseLong(),                    // med
                new ParseLong(),                    // local_pref
                new ParseNullAsEmpty(),             // aggregator
                new ParseNullAsEmpty(),             // community_list
                new ParseNullAsEmpty(),             // ext_community_list
                new ParseNullAsEmpty(),             // cluster_list
                new ParseLongEmptyAsZero(),         // isAtomicAgg
                new ParseLongEmptyAsZero(),         // isNexthopIPv4
                new ParseNullAsEmpty()              // originator_id
        };

        return processors;
    }
}
