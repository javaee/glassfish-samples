/*
 * Copyright 2004-2005 Sun Microsystems, Inc.  All rights reserved.
 * Use is subject to license terms.
 */

package enterprise.customer_cmp_ejb.persistence;

import java.io.Serializable;


/**
 * This is a dependent value class.
 *
 * @author	Rahul Biswas
 */
public class SubscriptionType implements Serializable {

    private String type;

    public static final String MAGAZINE = "Magazine";

    public static final String JOURNAL = "Journal";

    public static final String NEWS_PAPER = "News Paper";

    public static final String OTHER = "Other";

    private SubscriptionType (String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String toString() {
        return type;
    }
}
