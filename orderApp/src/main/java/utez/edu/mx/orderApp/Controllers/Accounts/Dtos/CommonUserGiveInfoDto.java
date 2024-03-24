package utez.edu.mx.orderApp.Controllers.Accounts.Dtos;

import lombok.Getter;
import lombok.Setter;
import utez.edu.mx.orderApp.Models.Accounts.CommonUser;

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
