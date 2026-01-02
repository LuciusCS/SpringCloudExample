package com.example.demo.bean.dto;

import lombok.Data;

@Data
public class StoreUpdateReq {
    private Long userId; // Ensuring we know which user is updating, though ideally from token
    private String name;
    private String description;
    private String logo;
}
