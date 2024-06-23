package com.mb.testsuithub;

/**************
 * Interface for communication between TestSuitHub and Node
 */

public interface TestBenchAPI {
    /******************************
        read from Node interfaces
     **********************/

    /**
     * Interface used to read battery level, value will be in % 0-100.<br>
     */
    public String readBatteryLevel() throws Exception;

    /******************************
     write to Node interfaces
     ******************************/

    /**
     * Interface used to lock the vehicle centrally.<br>
     */
    public void unlockVehicle() throws Exception;


}
