package org.caesar.finalWork.domain.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.caesar.finalWork.domain.vo.JSONable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User{
    private Integer userid;
    private String usercode;
    private String userpwd;
    private String nickname;

}
