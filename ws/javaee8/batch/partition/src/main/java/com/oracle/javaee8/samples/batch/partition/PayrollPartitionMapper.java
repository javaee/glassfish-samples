package com.oracle.javaee8.samples.batch.partition;

import javax.batch.api.partition.PartitionMapper;
import javax.batch.api.partition.PartitionPlan;
import javax.batch.api.partition.PartitionPlanImpl;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.context.JobContext;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Properties;

@Named("PayrollPartitionMapper")
public class PayrollPartitionMapper
    implements PartitionMapper {

    @Inject
    private JobContext jobContext;

    @EJB
    private SampleDataHolderBean bean;

    @Override
    public PartitionPlan mapPartitions() throws Exception {

        return new PartitionPlanImpl() {
            @Override
            public int getPartitions() {
                return 5;
            }

            @Override
            public Properties[] getPartitionProperties() {
                Properties jobParameters = BatchRuntime.getJobOperator().getParameters(jobContext.getExecutionId());

                String monthYear = (String) jobParameters.get("monthYear");
                int partitionSize = bean.getMaxEmployees() / getPartitions();
                                
                System.out.println("**[PayrollPartitionMapper] jobParameters: " + jobParameters
                    + "; executionId: " + jobContext.getExecutionId() + "; partitionSize = " + partitionSize);

                Properties[] props = new Properties[getPartitions()];
                for (int i=0; i<getPartitions(); i++) {
                    Properties partProps = new Properties();
                    partProps.put("monthYear", monthYear);
                    partProps.put("partitionNumber", i);
                    partProps.put("startEmpID", i * partitionSize);
                    partProps.put("endEmpID", (i + 1) * partitionSize);

                    props[i] = partProps;
                    System.out.println("**[PayrollPartitionMapper[" + i + "/" + getPartitions()
                                + "] : " + partProps);
                }

                return props;
            }
        };
    }

}
