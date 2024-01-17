package com.musalasoft.drones.constants;

public interface Message {
    String DRONE_REGISTERED_SUCCESSFULLY = "Drone Registered Successfully";
    String DRONE_RETRIEVED_SUCCESSFULLY = "Drone(s) Retrieved Successfully";
    String MEDICATION_REGISTERED_SUCCESSFULLY = "Medication Registered Successfully";
    String MEDICATION_RETRIEVED_SUCCESSFULLY = "Medication(s) Retrieved Successfully";
    String INVALID_DRONE_WEIGHT_LIMIT = "Weight Must Be Between 0 And 500 Grams";
    String INVALID_MEDICATION_WEIGHT = "Weight Must Be Between 0 And 450 Grams";
    String WEIGHT_NOT_IN_RANGE = "Weight Does Not fall Into Any Model Range";
    String INVALID_BATTERY_CAPACITY = "Battery Capacity Must Be Between 0 and 100";
    String SERIAL_NUMBER_CHARACTERS = "Serial Number Must Not Exceed 100 Characters";
    String MED_NAME_ALLOWABLE_VALUES = "Name - Only Letters, Numbers, ‘-‘, ‘_’ Are Allowed";
    String MED_CODE_ALLOWABLE_VALUES = "Code - Only UpperCase Letters, Numbers, ‘_’ Are Allowed";
    String IMAGE_IS_REQUIRED = "Image Url Is Required";
    String CODE_IS_REQUIRED = "Code Is Required";
    String MEDICATION_ID_IS_REQUIRED = "At Least One Medication Id Is Required";
    String WEIGHT_IS_REQUIRED = "Weight Is Required";
    String NAME_IS_REQUIRED = "Name Is Required";
    String STATE_IS_REQUIRED = "State Is Required";
    String MODEL_IS_REQUIRED = "Model Is Required";
    String MEDICATION_EXISTS = "Medication With Code `%s` Exists";
    String ITEMS_LOADED_SUCCESSFULLY = "Medication Item(s) Loaded Successfully";
    String DRONE_ID_IS_REQUIRED = "DroneId Is Required";
    String DRONE_DOES_NOT_EXIST = "Drone With Id `%s` Does Not Exist";
    String WEIGHT_LIMIT_EXCEEDED = "Drone WeightLimit Is Exceeded by`%s`grams";
    String LOW_BATTERY = "Drone Battery Capacity Is Below 25%";
    String LOADED_ITEMS_WEIGHT_IS_REQUIRED = "Loaded Items Weight Is Required";
    String EVENT_LOGS_RETRIEVED_SUCCESSFULLY = "Event Logs Retrieved Successfully";
    String LOADED_IS_REQUIRED = "Loaded Is Required";
    String DRONE_BATTERY_LEVEL_RETRIEVED_SUCCESSFULLY = "Drone Battery Level Retrieved Successfully";
    String INVALID_STATE = "The State `%s` Is Invalid. Allowable Values: `%s`";
}
