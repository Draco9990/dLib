package dLib.properties.objects.interfaces;

public interface IPropertyPage {
    default String serializeEditedProperties(){
        return "";
    }

    default void applyEditedProperties(String serializedData) {
        // Default implementation does nothing
    }
}
