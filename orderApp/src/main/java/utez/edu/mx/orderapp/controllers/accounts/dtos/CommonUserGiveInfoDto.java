package utez.edu.mx.orderapp.controllers.accounts.dtos;

import lombok.Getter;
import lombok.Setter;
import utez.edu.mx.orderapp.models.accounts.CommonUser;
import utez.edu.mx.orderapp.utils.EncryptionService;

@Getter
@Setter
public class CommonUserGiveInfoDto {
    private String commonUserId;
    private String userName;
    private String userFirstLastName;
    private String userSecondLastName;
    private String userEmail;
    private String userProfilePicUrl;
    private String userCellphone;

    public CommonUserGiveInfoDto encryptFields(EncryptionService encryptionService) throws Exception {
        if (this.commonUserId != null) {
            this.commonUserId = encryptionService.encrypt(this.commonUserId);
        }
        if (this.userName != null) {
            this.userName = encryptionService.encrypt(this.userName);
        }
        if (this.userFirstLastName != null) {
            this.userFirstLastName = encryptionService.encrypt(this.userFirstLastName);
        }
        if (this.userSecondLastName != null) {
            this.userSecondLastName = encryptionService.encrypt(this.userSecondLastName);
        }
        if (this.userEmail != null) {
            this.userEmail = encryptionService.encrypt(this.userEmail);
        }
        if (this.userCellphone != null) {
            this.userCellphone = encryptionService.encrypt(this.userCellphone);
        }
        if (this.userProfilePicUrl != null) {
            this.userProfilePicUrl = encryptionService.encrypt(this.userProfilePicUrl);
        }
        return this;
    }

    public CommonUserGiveInfoDto(CommonUser commonUser) {
        this.commonUserId = String.valueOf(commonUser.getCommonUserId());
        this.userName = commonUser.getUserName();
        this.userFirstLastName = commonUser.getUserFirstLastName();
        this.userSecondLastName = commonUser.getUserSecondLastName();
        this.userEmail = commonUser.getUserEmail();
        this.userCellphone = commonUser.getUserCellphone();
        this.userProfilePicUrl = commonUser.getUserProfilePicUrl();
    }
}
