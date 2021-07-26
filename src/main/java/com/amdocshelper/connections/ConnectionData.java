package com.amdocshelper.connections;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConnectionData {
    private String connectionString;
    private String database;
    private String username;
    private String password;
}
