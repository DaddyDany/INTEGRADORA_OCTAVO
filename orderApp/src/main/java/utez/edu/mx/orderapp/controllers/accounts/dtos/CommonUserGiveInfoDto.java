package utez.edu.mx.orderapp.controllers.accounts.dtos;

import lombok.Getter;
import lombok.Setter;
import utez.edu.mx.orderapp.models.accounts.CommonUser;

@Getter
@Setter
public class CommonUserGiveInfoDto {
    private Long commonUserId;
    private String userName;
    private String userFirstLastName;
    private String userSecondLastName;
    private String userEmail;
    private String userCellphone;

    public CommonUserGiveInfoDto(CommonUser commonUser) {
        this.commonUserId = commonUser.getCommonUserId();
        this.userName = commonUser.getUserName();
        this.userFirstLastName = commonUser.getUserFirstLastName();
        this.userSecondLastName = commonUser.getUserSecondLastName();
        this.userEmail = commonUser.getUserEmail();
        this.userCellphone = commonUser.getUserCellphone();
    }
}
