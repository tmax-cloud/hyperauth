package com.tmax.hyperauth.caller;

public class Constants {
	// operator
	public static final String HYPERCLOUD4_OPERATOR_URL = "http://hypercloud4-operator-service.hypercloud4-system";
	public static final String SERVICE_NAME_USER_NEW_ROLE_CREATE = "user";

	// Webhook
	public static final String HYPERCLOUD4_WEBHOOK_URL = "https://hypercloud4-webhook-svc.hypercloud4-system.svc";
	public static final String SERVICE_NAME_AUDIT_AUTHENTICATION = "audit/authentication";

	// hyperauth
	public static final String SERVICE_NAME_LOGIN_AS_ADMIN = "auth/realms/master/protocol/openid-connect/token";
	public static final String SERVICE_NAME_USER_DETAIL = "auth/admin/realms/tmax/users/";
	public static final String SERVICE_NAME_REALM_DETAIL = "auth/admin/realms/";

	// Oauth 
//	public static final String HYPERAUTH_URL = "http://hyperauth.hyperauth";
	public static final String HYPERAUTH_URL = "http://hyperauth.hyperauth2";  //FIXME : for testauth

	// html
	public static final String REGISTER_MAIL_BODY = "<!DOCTYPE html>\n" +
			"<html>\n" +
			"\u200B\n" +
			"<head>\n" +
			"    <meta charset=\"UTF-8\">\n" +
			"    <link href='http://fonts.googleapis.com/earlyaccess/notosanskr.css' rel='stylesheet' type='text/css'>\n" +
			"    <title>인증번호 입력</title>\n" +
			"</head>\n" +
			"\u200B\n" +
			"<body style=\"font-size: 16px; font-family: 'Noto Sans KR', sans-serif;\">\n" +
			"    <div style=\"margin: 0 auto; width: 25.94rem;\">\n" +
			"        <figure style=\"margin: 0;\">\n" +
			"            <img src=\"data:image/png;base64,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\"\n" +
			"                alt=\"TmaxA&C\" />\n" +
			"        </figure>\n" +
			"        <div style=\"margin: 0.7rem 0 0 0; border: 1px solid #E5E7E9; border-top: 3px solid #042A54;\">\n" +
			"            <section style=\"margin: 3.31rem 2.19rem; line-height: 1.5rem;\">\n" +
			"                <header style=\"text-align: center;\">\n" +
			"                    <p style=\"margin: 0; font-size: 1rem; font-weight: bold; color: #000000;\">\n" +
			"                        <span style=\"color: #185692;\">인증번호</span>를 입력해 주세요.\n" +
			"                    </p>\n" +
			"                </header>\n" +
			"                <main style=\"margin-top: 2.75rem;\">\n" +
			"                    <h1\n" +
			"                        style=\"margin: 0; font-size: 1.25rem; font-weight: bold; line-height: 1.81rem; color: #0066CC; text-align: center;\">\n" +
			"                        [인증번호: %%verifyNumber%%]\n" +
			"                    </h1>\n" +
			"                    <p style=\"margin: 2.81rem 0 0; font-size: 0.88rem; color: #3E3E3E;\">\n" +
			"                        안녕하세요.<br />\n" +
			"                        TmaxA&C의 서비스를 이용해 주셔서 감사합니다.<br />\n" +
			"                        회원 가입 화면에서 상단의 인증번호를 입력해 주세요.<br />\n" +
			"                        감사합니다.\n" +
			"                    </p>\n" +
			"                </main>\n" +
			"            </section>\n" +
			"            <footer\n" +
			"                style=\"background-color: #e9e9e9; height: 1.88rem; font-size: 0.75rem; line-height: 1.88rem; text-align: center; color: #999999;\">\n" +
			"                Copyright 2020. Tmax A&C Corp. All rights reserved.\n" +
			"            </footer>\n" +
			"        </div>\n" +
			"    </div>\n" +
			"</body>\n" +
			"\u200B\n" +
			"</html>";

	public static final String PASSWORD_VERIFY_CODE_BODY = "<html>\n" +
			"<head>\n" +
			"  <link href=\"https://fonts.googleapis.com/css?family=Noto+Sans+KR&display=swap\" rel=\"stylesheet\">\n" +
			"</head>\n" +
			"<body>\n" +
			"  <div style=\"width: 415px; margin: 60px auto; text-align: center; font-family: Noto Sans KR;\">\n" +
			"    <div id=\"email-verification\"\n" +
			"      style=\"background: url('https://mail.tmax.co.kr/skins/icloud/skin/images/logo.svg'); background-size: cover; width: 41px; height: 14px;\">\n" +
			"    </div>\n" +
			"    <hr style=\"height: 3px; background-color: #042a54; border-width: 0; margin-bottom: 0px;\">\n" +
			"    </hr>\n" +
			"    <div style=\"border: 1px solid #E5E7E9;\">\n" +
			"      <div style=\"margin-top: 53px; display: block; font-size: 16px; font-weight: bold;\">\n" +
			"        <span style=\"color: #185692;\">비밀번호</span>를 재설정해주세요.\n" +
			"      </div>\n" +
			"      <div>\n" +
			"        <div style=\"margin-top: 44px; margin-bottom: 20px; display: block; font-size: 20px; font-weight: bold; color: #0066CC;\n" +
			"                    display: block;\">\n" +
			"        [인증번호 : %%VERIFY_CODE%%]\n" +
			"        </div>\n" +
			"      </div>\n" +
			"      <div style=\"text-align: left; color: #505050; font-size: 12px; white-space: pre-line; line-height: 24px; margin-bottom: 60px; padding-right: 35px; padding-left: 35px;\">\n" +
			"        안녕하세요.\n" +
			"        비밀번호 재설정 화면에서 상단의 인증번호를 입력해주세요.\n" +
			"        감사합니다.\n" +
			"      </div>\n" +
			"    </div>\n" +
			"    <div style=\"height: 54px; background-color: #F4F5F5; color: #999999; font-size: 12px; line-height: 54px; border: 1px solid #E5E7E9; border-top: 0px;\">\n" +
			"      Copyright 2020. Tmax Corp. All rights reserved.\n" +
			"    </div>\n" +
			"  </div>\n" +
			"</body>\n" +
			"</html>";
	public static final String LOGIN_VERIFY_OTP_BODY = "<html>\n" +
			"<head>\n" +
			"  <link href=\"https://fonts.googleapis.com/css?family=Noto+Sans+KR&display=swap\" rel=\"stylesheet\">\n" +
			"</head>\n" +
			"<body>\n" +
			"  <div style=\"width: 415px; margin: 60px auto; text-align: center; font-family: Noto Sans KR;\">\n" +
			"    <div id=\"email-verification\"\n" +
			"      style=\"background: url('https://mail.tmax.co.kr/skins/icloud/skin/images/logo.svg'); background-size: cover; width: 41px; height: 14px;\">\n" +
			"    </div>\n" +
			"    <hr style=\"height: 3px; background-color: #042a54; border-width: 0; margin-bottom: 0px;\">\n" +
			"    </hr>\n" +
			"    <div style=\"border: 1px solid #E5E7E9;\">\n" +
			"      <div style=\"margin-top: 53px; display: block; font-size: 16px; font-weight: bold;\">\n" +
			"        <span style=\"color: #185692;\">인증번호</span>를 입력해 주세요.\n" +
			"      </div>\n" +
			"      <div>\n" +
			"        <div style=\"margin-top: 44px; margin-bottom: 20px; display: block; font-size: 20px; font-weight: bold; color: #0066CC;\n" +
			"                    display: block;\">\n" +
			"        [인증번호 : %%VERIFY_CODE%%]\n" +
			"        </div>\n" +
			"      </div>\n" +
			"      <div style=\"text-align: left; color: #505050; font-size: 12px; white-space: pre-line; line-height: 24px; margin-bottom: 60px; padding-right: 35px; padding-left: 35px;\">\n" +
			"        안녕하세요.\n" +
			"        로그인 화면에서 상단의 인증번호를 입력해주세요.\n" +
			"        감사합니다.\n" +
			"      </div>\n" +
			"    </div>\n" +
			"    <div style=\"height: 54px; background-color: #F4F5F5; color: #999999; font-size: 12px; line-height: 54px; border: 1px solid #E5E7E9; border-top: 0px;\">\n" +
			"      Copyright 2020. Tmax Corp. All rights reserved.\n" +
			"    </div>\n" +
			"  </div>\n" +
			"</body>\n" +
			"</html>";
	public static final String ACCOUNT_WITHDRAWAL_REQUEST_BODY = "<html>\n" +
			"\n" +
			"<head>\n" +
			"    <link href=\"https://fonts.googleapis.com/css?family=Noto+Sans+KR&display=swap\" rel=\"stylesheet\">\n" +
			"</head>\n" +
			"\n" +
			"<body>\n" +
			"    <div style=\"width: 415px; margin: 60px auto; text-align: center; font-family: Noto Sans KR;\">\n" +
			"        <div id=\"email-verification\"\n" +
			"            style=\"background: url('https://mail.tmax.co.kr/skins/icloud/skin/images/logo.svg'); background-size: cover; width: 41px; height: 14px;\">\n" +
			"        </div>\n" +
			"        <hr style=\"height: 3px; background-color: #042a54; border-width: 0; margin-bottom: 0px;\">\n" +
			"        </hr>\n" +
			"        <div style=\"border: 1px solid #E5E7E9;\">\n" +
			"            <div style=\"margin-top: 53px; display: block; font-size: 16px; font-weight: bold;\">\n" +
			"                <span style=\"color: #185692;\">계정 탈퇴 신청</span>이 완료되었습니다.\n" +
			"            </div>\n" +
			"            <div\n" +
			"                style=\"text-align: left; color: #3e3e3e; font-size: 14px; white-space: pre-line; line-height: 24px; margin-bottom: 60px; padding-right: 35px; padding-left: 35px; width:345px;\">\n" +
			"                안녕하세요. Tmax통합서비스 관리자입니다.\n" +
			"                고객님의 계정 탈퇴 신청이 완료되었습니다.\n" +
			"\n" +
			"                계정 탈퇴 신청이 승인될 경우 더 이상 Tmax 통합 서비스를\n" +
			"                이용할 수 없으며 계정과 데이터가 삭제됩니다. 필요한 데이\n" +
			"                터를 아직 백업하지 않았다면, 고객센터로 문의주시기 바랍니\n" +
			"                다.\n" +
			"\n" +
			"                <div style=\"background-color: rgba( 218, 218, 218, 0.3 ); padding: 12px 15px; color:#3e3e3e\">콜센터:1800-5566(오전 9시-오후 6시)\n" +
			"                    E-Mail : TmaxOneAccount-Support@tmax.co.kr\n" +
			"                </div>\n" +
			"                감사합니다.\n" +
			"            </div>\n" +
			"        </div>\n" +
			"        <div\n" +
			"            style=\"height: 54px; background-color: #F4F5F5; color: #999999; font-size: 12px; line-height: 54px; border: 1px solid #E5E7E9; border-top: 0px;\">\n" +
			"            Copyright 2020. Tmax Corp. All rights reserved.\n" +
			"        </div>\n" +
			"    </div>\n" +
			"</body>\n" +
			"\n" +
			"</html>";

	public static final String ACCOUNT_WITHDRAWAL_APPROVAL_BODY = "<html>\n" +
			"\n" +
			"<head>\n" +
			"    <link href=\"https://fonts.googleapis.com/css?family=Noto+Sans+KR&display=swap\" rel=\"stylesheet\">\n" +
			"</head>\n" +
			"\n" +
			"<body>\n" +
			"    <div style=\"width: 415px; margin: 60px auto; text-align: center; font-family: Noto Sans KR;\">\n" +
			"        <div id=\"email-verification\"\n" +
			"            style=\"background: url('https://mail.tmax.co.kr/skins/icloud/skin/images/logo.svg'); background-size: cover; width: 41px; height: 14px;\">\n" +
			"        </div>\n" +
			"        <hr style=\"height: 3px; background-color: #042a54; border-width: 0; margin-bottom: 0px;\">\n" +
			"        </hr>\n" +
			"        <div style=\"border: 1px solid #E5E7E9;\">\n" +
			"            <div style=\"margin-top: 53px; display: block; font-size: 16px; font-weight: bold;\">\n" +
			"                <span style=\"color: #185692;\">계정탈퇴</span>가 완료되었습니다.\n" +
			"            </div>\n" +
			"            <div\n" +
			"                style=\"text-align: left; color: #3e3e3e; font-size: 14px; white-space: pre-line; line-height: 24px; margin-bottom: 60px; padding-right: 35px; padding-left: 35px;\">\n" +
			"                안녕하세요. Tmax통합서비스 관리자입니다.\n" +
			"                고객님의 계정 탈퇴가 완료되었습니다.\n" +
			"\n" +
			"                그동안 Tmax 통합서비스를 이용해주신 회원님께 진심으로\n" +
			"                감사드립니다. 회원님의 소중한 충고로 더욱 성장할 수 있는\n" +
			"                Tmax가 되겠습니다.\n" +
			"\n" +
			"                감사합니다.\n" +
			"            </div>\n" +
			"        </div>\n" +
			"        <div\n" +
			"            style=\"height: 54px; background-color: #F4F5F5; color: #999999; font-size: 12px; line-height: 54px; border: 1px solid #E5E7E9; border-top: 0px;\">\n" +
			"            Copyright 2020. Tmax Corp. All rights reserved.\n" +
			"        </div>\n" +
			"    </div>\n" +
			"</body>\n" +
			"\n" +
			"</html>";

}
