package com.amdocshelper.connections;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class Connections {
    private List<ConnectionData> connectionData = new ArrayList<ConnectionData>();

    public Connections() {
        connectionData.set(0, ConnectionData.builder()
                .connectionString("jdbc:oracle:thin:@//10.240.13.66:1521/orcl")
                .database("dkp1")
                .username("reporter")
                .password("ciuyrhvv")
                .build());
    }
}
