/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.javasimon.examples.jmx.custom;

import org.javasimon.jmx.SimonSuperMXBean;

/**
 *
 * @author gquintana
 */
public interface CustomStopwatchMXBean extends SimonSuperMXBean {

    long getCounter();

    long getLast();

    long getMax();

    double getMean();

    long getMin();

    double getStandardDeviation();

    long getTotal();
    
}
