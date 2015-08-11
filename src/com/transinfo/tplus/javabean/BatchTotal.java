/**
* Copyright (c) 2007-2008 Trans-Info Pte Ltd. Singapore. All Rights Reserved.
* This work contains trade secrets and confidential material of
* Trans-Info Pte Ltd. Singapore and its use of disclosure in whole
* or in part without express written permission of
* Trans-Info Pte Ltd. Singapore. is prohibited.
* Date of Creation   : Feb 25, 2008
* Version Number     : 1.0
*                   Modification History:
* Date          Version No.         Modified By           Modification Details.
*/

package com.transinfo.tplus.javabean;

/**
 *
 * @author  Owner
 */
public class BatchTotal extends java.lang.Object {

	long BatchSaleTotalCount;
	double BatchSaleTotalAmount;
	long BatchRefundTotalCount;
	double BatchRefundTotalAmount;
    /** Creates a new instance of BatchTotal */
    public BatchTotal() {
    }

    /** Getter for property BatchTotalAmount.
     * @return Value of property BatchTotalAmount.
     *
     */
    public double getBatchSaleTotalAmount() {
        return BatchSaleTotalAmount;
    }

    /** Setter for property BatchTotalAmount.
     * @param BatchTotalAmount New value of property BatchTotalAmount.
     *
     */
    public void setBatchSaleTotalAmount(double BatchSaleTotalAmount) {
        this.BatchSaleTotalAmount = BatchSaleTotalAmount;
    }


    /** Getter for property BatchTotalCount.
     * @return Value of property BatchTotalCount.
     *
     */
    public long getBatchSaleTotalCount() {
        return BatchSaleTotalCount;
    }

    /** Setter for property BatchTotalCount.
     * @param BatchTotalCount New value of property BatchTotalCount.
     *
     */
    public void setBatchSaleTotalCount(long BatchSaleTotalCount) {
        this.BatchSaleTotalCount = BatchSaleTotalCount;
    }


    /** Getter for property BatchTotalCount.
     * @return Value of property BatchTotalCount.
     *
     */
    public long getBatchRefundTotalCount() {
        return BatchRefundTotalCount;
    }

    /** Setter for property BatchTotalCount.
     * @param BatchTotalCount New value of property BatchTotalCount.
     *
     */
    public void setBatchRefundTotalCount(long BatchRefundTotalCount) {
        this.BatchRefundTotalCount = BatchRefundTotalCount;
    }

    /** Getter for property BatchTotalAmount.
     * @return Value of property BatchTotalAmount.
     *
     */
    public double getBatchRefundTotalAmount() {
        return BatchRefundTotalAmount;
    }

    /** Setter for property BatchTotalAmount.
     * @param BatchTotalAmount New value of property BatchTotalAmount.
     *
     */
    public void setBatchRefundTotalAmount(double BatchRefundTotalAmount) {
        this.BatchRefundTotalAmount = BatchRefundTotalAmount;
    }



}
