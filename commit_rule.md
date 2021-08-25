**기본적으로 프로젝트의 커밋 이력관리는 scripts/hyper-auth-changelog.sh을 통해 배포시에 CHANGELOG.md 에 남긴다.**

**개발한 커밋 이력중 CHANGELOG.md에 남겨야 할때는 다음과 같은 규칙을 따라야 한다.**


## 1. 기능추가 : [feat] 내용
#### ex) [feat] 유저 로그인 기능 추가

## 2. 기능변경 : [mod] 내용
#### ex) [mod] 유저 비밀번호 찾기 기능 로직 변경

## 3. ims 처리 : [ims][ims번호] ims 제목 혹은 문제 및 해결 요약
#### ex) [ims][202020] 유저 비밀번호 찾기 안되는 현상 해결

## 4. k8s 환경 crd 이력 변경시 : [crd] 내용
#### ex) [crd] UserCRD email 필드 spec에 추가 

## 5. 그외에 위에 해당하지 않는 사항 : [etc] 내용
#### ex) [etc] 인스톨 가이드 수정
