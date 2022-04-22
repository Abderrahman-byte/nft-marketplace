package org.stibits.rnft.errors;

public class StorageUnacceptedMediaType extends Exception {
    private String mediaType;

    public StorageUnacceptedMediaType () {
        this.mediaType = "*/*";
    }

    public StorageUnacceptedMediaType (String mediaType) {
        this.mediaType = mediaType;
    }

    @Override
    public String getMessage() {
        return "Unaccepted media type " + this.mediaType ;
    }
}
