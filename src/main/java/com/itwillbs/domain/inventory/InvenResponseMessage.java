package com.itwillbs.domain.inventory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvenResponseMessage {

    private boolean success;
    private String message;
	
	
}
