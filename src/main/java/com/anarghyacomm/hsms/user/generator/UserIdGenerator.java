package com.anarghyacomm.hsms.user.generator;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class UserIdGenerator implements IdentifierGenerator {

    private static final String PREFIX = "OD_";

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) throws HibernateException {
        Long sequenceNumber = getNextSequenceNumber(session);

        // Generate the custom identifier
        return PREFIX + String.format("%01d", sequenceNumber);
    }

    @SuppressWarnings("deprecation")
	private Long getNextSequenceNumber(SharedSessionContractImplementor session) {
        try {
            // Use Hibernate's API to obtain the next sequence value
            Number nextValue = (Number) session.createNativeQuery("SELECT NEXTVAL('your_sequence_name')")
                    .uniqueResult();

            return nextValue.longValue();
        } catch (Exception e) {
            // Handle exceptions
            e.printStackTrace();
        }

        // Return a default value if unable to retrieve the sequence number
        return -1L;
    }
}
