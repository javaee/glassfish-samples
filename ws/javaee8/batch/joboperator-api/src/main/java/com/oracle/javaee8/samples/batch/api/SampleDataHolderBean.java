/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://oss.oracle.com/licenses/CDDL+GPL-1.1
 * or LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package com.oracle.javaee8.samples.batch.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 * @author makannan
 */
@Singleton
@Startup
public class SampleDataHolderBean {

    private Map<String, Set<PayrollInputRecord>> payrollInputRecords
            = new HashMap<>();

    private Map<String, Set<PayrollRecord>> payrollRegistry
            = new HashMap<>();
    
    @PostConstruct
    public void onApplicationStartup() {
        String[] monthYear = new String[] {"JAN-2013", "FEB-2013", "MAR-2013"};
        for (int monthIndex = 0; monthIndex < monthYear.length; monthIndex++) {
            Set<PayrollInputRecord> inputRecords = new HashSet<PayrollInputRecord>(5);
            for (int empID=1; empID<6; empID++) {
                PayrollInputRecord e = new PayrollInputRecord();
                e.setId(empID);
                int baseSalary = 10000 + empID*100 + monthIndex*100;
                e.setBaseSalary(baseSalary);
                inputRecords.add(e);
            }

            payrollInputRecords.put(monthYear[monthIndex], inputRecords);
        }

    }

    public String[] getAllMonthYear() {
        return payrollInputRecords.keySet().toArray(new String[0]);
    }

    public Set<PayrollInputRecord> getPayrollInputRecords(String monthYear) {
        return payrollInputRecords.get(monthYear);
    }
    
    public void addPayrollRecord(PayrollRecord r) {
        String monthYear = r.getMonthYear();
        Set<PayrollRecord> monthlyPayroll = payrollRegistry.get(monthYear);
        if (monthlyPayroll == null) {
            monthlyPayroll = new HashSet<>();
            payrollRegistry.put(monthYear, monthlyPayroll);
        }
        monthlyPayroll.add(r);
    }
    
    public Set<PayrollRecord> getPayrollRecords(String monthYear) {
        Set<PayrollRecord> empty = new HashSet<>();
        Set<PayrollRecord> records = payrollRegistry.get(monthYear);
        return records == null ? empty : records;
    }
}
