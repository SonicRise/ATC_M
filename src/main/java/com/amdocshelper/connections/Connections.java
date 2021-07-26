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
        connectionData.add(0, ConnectionData.builder()
                .connectionString("jdbc:oracle:thin:@//10.240.13.66:1521/orcl")
                .database("dkp1")
                .username("reporter")
                .password("ciuyrhvv")
                .build());
        connectionData.add(1, ConnectionData.builder()
                .connectionString("jdbc:oracle:thin:@//10.72.237.102:1521/KZTUSG1")
                .database("kztusg1")
                .username("prd1usg1c")
                .password("prd1usg1c")
                .build());
    }
}
