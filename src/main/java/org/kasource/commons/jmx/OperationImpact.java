package org.kasource.commons.jmx;

import javax.management.MBeanOperationInfo;

public enum OperationImpact {
    ACTION      (MBeanOperationInfo.ACTION), 
    ACTION_INFO (MBeanOperationInfo.ACTION_INFO), 
    INFO        (MBeanOperationInfo.INFO), 
    UNKNOWN     (MBeanOperationInfo.UNKNOWN);

    private int impact;

    OperationImpact(int impact) {
        this.impact = impact;
    }

    public int getImpact() {
        return impact;
    }
}
