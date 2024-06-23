package com.mb.testsuithub;
//package com.daimler.testbenchgrid;
public enum TestBenchCapability {
    // provided by config.json and available for session requests
    VIN("vin"), TELEMATICS("telematics"), TELEMATICS_SOFTWARE("telematicsSoftware"), STAGE("stage"), USER("user"), MODULE("module"), MODULE_SOFTWARE("moduleSoftware"), ARCHITECTURE("architecture"), FUELTYPE(
            "fuelType"), LOCATION("location"), MODEL("model"),
    DEVICE_SERVICES("services"),LICENCE_PLATE("licencePlate"),
    // provided by config.json but only used internally
    ACCESSIBILITY("accessibility"), FEATURES("features"), CHANNEL("channel"),

    PRIORITIZER("prioritizer"),

    // calculated from other config values and available for session requests
    ASSIGNED("userAssigned"), CAN_REASSIGN("canReassign"), NODE("node"),

    // runtime cpability
    SIMULATION_STATUS("simulationStatus");


    public String key;

    private TestBenchCapability(String key) {
        this.key = key;
    }

    public enum StageCapability {

        INT("INT"), PREPROD("PREPROD"), PROD("PROD"), NONPROD("NONPROD");

        public String value;

        private StageCapability(String value) {
            this.value = value;
        }
    }

    /**
     * Possible Values for the module.
     */
    public enum ModuleCapability {

        SAMPLE_A("SAMPLE_A"), SAMPLE_B("SAMPLE_B"), SAMPLE_C("SAMPLE_C"), SAMPLE_D("SAMPLE_D"), SAMPLE_E("SAMPLE_D");

        public String value;

        private ModuleCapability(String value) {
            this.value = value;
        }
    }



    /**
     * Possible Values for the Architecture.
     */
    public enum ArchitectureCapability {

        STAR25("Star2.5"), STAR30("Star3.0"), STAR35("Star3.5");

        public String value;

        private ArchitectureCapability(String value) {
            this.value = value;
        }
    }

    /**
     * Possible Values for the Simulation status.
     */
    public enum SimulationStatusCapability {

        ONLINE("online"), OFFLINE("offline");

        public String value;

        private SimulationStatusCapability(String value) {
            this.value = value;
        }
    }

    /**
     * Default caps of openqa grid or sensitive caps (like target host, or etf session) which we don't want to expose.
     */
    public enum HiddenCapability {

        DOCKER_CONTAINER("dockerContainer"), TARGET_HOST("host"), TARGET_SEESION("session"), PLATFORM("platform"),
        PLATFORM_NAME("platformName"), SEL_PROTOCOL("seleniumProtocol"), CONFIG_UUID("server:CONFIG_UUID"),
        BENCH_RESET("benchReset"),ACCESS_GROUPS("accessGroups");

        public String key;

        private HiddenCapability(String key) {
            this.key = key;
        }

        /**
         * Check if the key is one of the hidden caps.
         */
        public static boolean exists(String key) {
            for (HiddenCapability capability : HiddenCapability.values()) {
                if (capability.key.equalsIgnoreCase(key)) {
                    return true;
                }
            }
            return false;
        }
    }


}
