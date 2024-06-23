package com.mb.testsuithub.client.model;

import java.util.List;

public class AccessGroups {
    private List<String> read;
    private List<String> use;
    private List<String> admin;

    public List<String> getRead() {
        return read;
    }

    public List<String> getUse() {
        return use;
    }

    public List<String> getAdmin() {
        return admin;
    }

}
